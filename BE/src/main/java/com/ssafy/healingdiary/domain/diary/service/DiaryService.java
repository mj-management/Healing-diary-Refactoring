package com.ssafy.healingdiary.domain.diary.service;

import static com.ssafy.healingdiary.global.error.ErrorCode.CLUB_NOT_FOUND;
import static com.ssafy.healingdiary.global.error.ErrorCode.DIARY_NOT_FOUND;
import static com.ssafy.healingdiary.global.error.ErrorCode.ENTITY_NOT_FOUND;
import static com.ssafy.healingdiary.global.error.ErrorCode.MEMBER_NOT_FOUND;
import static com.ssafy.healingdiary.global.error.ErrorCode.RECORD_NOT_FOUND;

import com.google.gson.Gson;
import com.ssafy.healingdiary.domain.club.domain.Club;
import com.ssafy.healingdiary.domain.club.repository.ClubRepository;
import com.ssafy.healingdiary.domain.diary.domain.Diary;
import com.ssafy.healingdiary.domain.diary.domain.DiaryTag;
import com.ssafy.healingdiary.domain.diary.domain.Emotion;
import com.ssafy.healingdiary.domain.diary.dto.CalendarResponse;
import com.ssafy.healingdiary.domain.diary.dto.DiaryDetailResponse;
import com.ssafy.healingdiary.domain.diary.dto.DiarySimpleResponse;
import com.ssafy.healingdiary.domain.diary.dto.EmotionResponse;
import com.ssafy.healingdiary.domain.diary.dto.EmotionStatisticResponse;
import com.ssafy.healingdiary.domain.diary.repository.DiaryRepository;
import com.ssafy.healingdiary.domain.diary.repository.DiaryTagRepository;
import com.ssafy.healingdiary.domain.diary.repository.EmotionRepository;
import com.ssafy.healingdiary.domain.member.domain.Member;
import com.ssafy.healingdiary.domain.member.repository.MemberRepository;
import com.ssafy.healingdiary.domain.tag.domain.Tag;
import com.ssafy.healingdiary.domain.tag.repository.TagRepository;
import com.ssafy.healingdiary.global.error.CustomException;
import com.ssafy.healingdiary.global.error.ErrorCode;
import com.ssafy.healingdiary.infra.speech.ClovaClient;
import com.ssafy.healingdiary.infra.speech.ClovaSpeechClient;
import com.ssafy.healingdiary.infra.speech.ClovaSpeechClient.NestRequestEntity;
import com.ssafy.healingdiary.infra.storage.StorageClient;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final EmotionRepository emotionRepository;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final TagRepository tagRepository;
    private final DiaryTagRepository diaryTagRepository;
    private final ClovaSpeechClient clovaSpeechClient;
    private final ClovaClient clovaClient;
    private final Gson gson;
    private final RedisTemplate<String, ?> redisTemplate;
    private final StorageClient storageClient;

    public Slice<DiarySimpleResponse> getDiaryList(
        Boolean all,
        Long memberId,
        Long clubId,
        String keyword,
        String tag,
        Integer year,
        Integer month,
        Integer day,
        Pageable pageable) {

        Slice<DiarySimpleResponse> slice = diaryRepository.findByOption(all,memberId,clubId,keyword,tag,year,month,day,pageable);
        return slice;
    }


    public DiaryDetailResponse getDiaryDetail(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(()->new CustomException(DIARY_NOT_FOUND));
        DiaryDetailResponse diaryDetailResponse = DiaryDetailResponse.of(diary);
        return diaryDetailResponse;
    }

    public void deleteDiary(Long memberId, Long diaryId) throws IOException {
        Diary diary = diaryRepository.findById(diaryId)
            .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));
        if(!memberId.equals(diary.getMember().getId())){
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        String imageUrl = diary.getDiaryImageUrl();
        diaryRepository.deleteById(diaryId);
        storageClient.deleteFile(imageUrl);
    }

    public List<CalendarResponse> getCalendar(Long memberId, int year, int month) {
        List<CalendarResponse> calendarList = diaryRepository.getEmotionByMonthOfYear(memberId, year, month);
        return calendarList;
    }

    public List<EmotionStatisticResponse> getEmotionStatistics(Long memberId, Integer year, Integer month) {
        List<EmotionStatisticResponse> emotionList = diaryRepository.countEmotion(memberId, year, month);
        return emotionList;
    }

    public Map<String, Object> analyzeDiary(MultipartFile rec) throws IOException{

        String recordUrl = storageClient.uploadFile(rec);

        NestRequestEntity requestEntity = new NestRequestEntity();
        String speech = clovaSpeechClient.url(recordUrl, requestEntity);
        Map<String, Object> sttMap = gson.fromJson(speech, Map.class);

        String content = sttMap.get("text").toString();
        String analysis = clovaClient.analyze(content);
        Map<String, Object> analysisMap = gson.fromJson(analysis, Map.class);
        Map<String, Object> documentMap = gson.fromJson(analysisMap.get("document").toString(), Map.class);
        Map<String, Double> confidence = gson.fromJson(documentMap.get("confidence").toString(), Map.class);

        double negative = confidence.get("negative");
        double positive = confidence.get("positive");
        double neutral = confidence.get("neutral");

        int emotionCode = 0;
        if(negative >= 80) {
            emotionCode = 1;
        } else if(positive >= 80){
            emotionCode = 5;
        } else if (neutral >= 60 || Math.abs(negative-positive) < 10) {
            emotionCode = 3;
        } else if (negative > positive) {
            emotionCode = 2;
        } else if (negative < positive) {
            emotionCode = 4;
        }
        Emotion emotion = emotionRepository.findById(emotionCode)
            .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
        EmotionResponse emotionResponse = EmotionResponse.of(emotion);

        //redis
        ValueOperations<String, String> valueOperations = (ValueOperations<String, String>) redisTemplate.opsForValue();
        valueOperations.set(recordUrl, content);
        redisTemplate.expire(recordUrl, 7, TimeUnit.DAYS);

        Map<String, Object> result = new HashMap<>();
        result.put("emotion", emotionResponse);
        result.put("recordUrl", recordUrl);
        return result;
    }

    public List<DiarySimpleResponse> getRecommendedDiaryList(Long memberId, Integer num) {
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new CustomException(MEMBER_NOT_FOUND));
        return diaryRepository.findByDiseaseAndRegion(member, num);
    }

    public Map<String, Object> createDiary(Long memberId, String recordUrl, Integer emotionCode, Long clubId, List<String> tags, MultipartFile image) throws IOException {
        if(!redisTemplate.hasKey(recordUrl)){
            throw new CustomException(RECORD_NOT_FOUND);
        }

        ValueOperations<String, String> valueOperations = (ValueOperations<String, String>) redisTemplate.opsForValue();
        String content = valueOperations.get(recordUrl);
        String imageUrl = storageClient.uploadFile(image);
        Member member = memberRepository.getReferenceById(memberId);

        Club club = null;
        if(clubId!=null) {
            try {
                club = clubRepository.getReferenceById(clubId);
            }catch (EntityNotFoundException e){
                throw new CustomException(CLUB_NOT_FOUND);
            }
        }
        Emotion emotion = emotionRepository.getReferenceById(emotionCode);

        Diary diary = Diary.builder()
            .member(member)
            .club(club)
            .emotion(emotion)
            .diaryImageUrl(imageUrl)
            .recordUrl(recordUrl)
            .content(content)
            .build();

        List<DiaryTag> tagList = tags
            .stream()
            .map(tagContent -> {
                Tag tag = tagRepository.findByContentLike(tagContent);
                if(tag==null) {
                    tag = Tag.builder()
                        .content(tagContent)
                        .build();
                }
                DiaryTag diaryTag = DiaryTag.builder()
                    .diary(diary)
                    .tag(tag)
                    .build();
                return diaryTag;
            })
            .collect(Collectors.toList());

        diary.setDiaryTag(tagList);
        Diary savedDiary = diaryRepository.save(diary);

        Map<String, Object> map = new HashMap<>();
        map.put("diaryId", savedDiary.getId());
        return map;
    }
}
