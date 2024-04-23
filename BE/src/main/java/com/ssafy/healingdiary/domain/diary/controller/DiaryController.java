package com.ssafy.healingdiary.domain.diary.controller;


import com.ssafy.healingdiary.domain.diary.dto.CalendarResponse;
import com.ssafy.healingdiary.domain.diary.dto.DiaryDetailResponse;
import com.ssafy.healingdiary.domain.diary.dto.DiarySimpleResponse;
import com.ssafy.healingdiary.domain.diary.dto.EmotionStatisticResponse;
import com.ssafy.healingdiary.domain.diary.service.DiaryService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
@RequestMapping("/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping
    public Slice<DiarySimpleResponse> getDiaryList(
        Authentication authentication,
        @RequestParam boolean all,
        @RequestParam(required = false) Long clubId,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String tag,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer day,
        Pageable pageable
    ){
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Long memberId = Long.parseLong(principal.getUsername());
        return diaryService.getDiaryList(all, memberId, clubId, keyword, tag, year, month, day, pageable);
    }

    @GetMapping("/recommendation")
    public List<DiarySimpleResponse> getRecommendedDiaryList(
        Authentication authentication,
        @RequestParam(required = false, defaultValue = "5") int num
    ){
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Long memberId = Long.parseLong(principal.getUsername());
        return diaryService.getRecommendedDiaryList(memberId, num);
    }

    @GetMapping("/{diaryId}")
    public DiaryDetailResponse getDiaryDetail(
        @PathVariable Long diaryId){
        return diaryService.getDiaryDetail(diaryId);
    }

    @PostMapping("/analyze")
    public Map<String, Object> analyzeDiary(
        @RequestPart MultipartFile rec
    ) throws IOException{
        return diaryService.analyzeDiary(rec);
    }

    @PostMapping
    public Map<String, Object> createDiary(
        Authentication authentication,
        @RequestParam String recordUrl,
        @RequestParam Integer emotionCode,
        @RequestParam(required = false) Long clubId,
        @RequestParam List<String> tags,
        @RequestPart MultipartFile image
    ) throws IOException {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Long memberId = Long.parseLong(principal.getUsername());
        return diaryService.createDiary(memberId, recordUrl, emotionCode, clubId, tags, image);
    }

    @DeleteMapping("/{diaryId}")
    public void deleteDiary(
        Authentication authentication,
        @PathVariable Long diaryId) throws IOException {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Long memberId = Long.parseLong(principal.getUsername());
        diaryService.deleteDiary(memberId, diaryId);
    }

    @GetMapping("/calendar")
    public List<CalendarResponse> getCalendar(
        Authentication authentication,
        @RequestParam int year,
        @RequestParam int month)
    {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Long memberId = Long.parseLong(principal.getUsername());
        return diaryService.getCalendar(memberId, year, month);
    }

    @GetMapping("/emotion")
    public List<EmotionStatisticResponse> getEmotionStatistics(
        Authentication authentication,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month)
    {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Long memberId = Long.parseLong(principal.getUsername());
        return diaryService.getEmotionStatistics(memberId, year, month);
    }

}
