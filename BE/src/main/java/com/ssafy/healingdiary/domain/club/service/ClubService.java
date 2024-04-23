package com.ssafy.healingdiary.domain.club.service;

import static com.ssafy.healingdiary.global.error.ErrorCode.CLUB_NOT_FOUND;
import static com.ssafy.healingdiary.global.error.ErrorCode.MEMBER_NOT_FOUND;

import com.ssafy.healingdiary.domain.club.domain.Club;
import com.ssafy.healingdiary.domain.club.domain.ClubMember;
import com.ssafy.healingdiary.domain.club.domain.ClubTag;
import com.ssafy.healingdiary.domain.club.dto.ClubApprovalResponse;
import com.ssafy.healingdiary.domain.club.dto.ClubDetailResponse;
import com.ssafy.healingdiary.domain.club.dto.ClubInvitationResponse;
import com.ssafy.healingdiary.domain.club.dto.ClubJoinResponse;
import com.ssafy.healingdiary.domain.club.dto.ClubLeaveResponse;
import com.ssafy.healingdiary.domain.club.dto.ClubListResponse;
import com.ssafy.healingdiary.domain.club.dto.ClubMemberResponse;
import com.ssafy.healingdiary.domain.club.dto.ClubRegisterRequest;
import com.ssafy.healingdiary.domain.club.dto.ClubRegisterResponse;
import com.ssafy.healingdiary.domain.club.dto.ClubSimpleResponse;
import com.ssafy.healingdiary.domain.club.dto.ClubUpdateResponse;
import com.ssafy.healingdiary.domain.club.dto.InvitationRegisterRequest;
import com.ssafy.healingdiary.domain.club.dto.InvitationRegisterResponse;
import com.ssafy.healingdiary.domain.club.repository.ClubMemberRepository;
import com.ssafy.healingdiary.domain.club.repository.ClubRepository;
import com.ssafy.healingdiary.domain.club.repository.ClubTagRepository;
import com.ssafy.healingdiary.domain.member.domain.Member;
import com.ssafy.healingdiary.domain.member.domain.Notice;
import com.ssafy.healingdiary.domain.member.domain.NoticeType;
import com.ssafy.healingdiary.domain.member.repository.MemberRepository;
import com.ssafy.healingdiary.domain.member.repository.NoticeRepository;
import com.ssafy.healingdiary.domain.tag.domain.Tag;
import com.ssafy.healingdiary.domain.tag.repository.TagRepository;
import com.ssafy.healingdiary.global.error.CustomException;
import com.ssafy.healingdiary.global.error.ErrorCode;
import com.ssafy.healingdiary.infra.storage.S3StorageClient;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubTagRepository clubTagRepository;
    private final MemberRepository memberRepository;
    private final NoticeRepository noticeRepository;
    private final TagRepository tagRepository;
    private final S3StorageClient s3Service;
    @Value("${default-image-s3}")
    private String DEFAULT_IMAGE_S3;

    public Slice<ClubSimpleResponse> getClubListByTag(
        String id,
        boolean all,
        String keyword,
        String tagContent,
        Pageable pageable) {

        Long memberId = null;
        if (!all) {
            memberId = Long.parseLong(id);
        }
        Slice<ClubSimpleResponse> clubSimpleResponseList = clubRepository.findByOption(all,
            memberId,
            keyword, tagContent, pageable);
        clubSimpleResponseList.stream().forEach((response -> {
            Club club = clubRepository.findById(response.getClubId()).get();
            List<String> tags = clubTagRepository.findByClub(club).stream()
                .map((clubTag -> {
                    return clubTag.getTag().getContent();
                }))
                .collect(Collectors.toList());
            response.setTags(tags);
        }));

        return clubSimpleResponseList;
    }

    public Slice<ClubInvitationResponse> getInvitationList(Long clubId, Pageable pageable) {
        Long hostId = clubRepository.findById(clubId)
            .orElseThrow(()->new CustomException(CLUB_NOT_FOUND))
            .getHost().getId(); // 방장 ID
        Slice<ClubInvitationResponse> clubInvitationResponseList = clubMemberRepository.findDistinctByClubIdNot(
            clubId, hostId, pageable);
        return clubInvitationResponseList;
    }

    public ClubRegisterResponse registClub(String id,
        String name,
        String description,
        List<String> tagList,
        MultipartFile file) throws IOException {
        Member member = memberRepository.findById(Long.parseLong(id)).orElseThrow(()-> new CustomException(MEMBER_NOT_FOUND));
        String imageUrl = s3Service.uploadFile(file);
        Club club = ClubRegisterRequest.toEntity(name, description, member, imageUrl);
        List<ClubTag> tags = tagList
            .stream()
            .map((tagContent) -> {
                Tag tag = tagRepository.findByContentLike(tagContent);
                if (tag == null) {
                    tag = Tag.builder()
                        .content(tagContent)
                        .build();
                    tagRepository.save(tag);
                }
                ClubTag clubTag = ClubRegisterRequest.toEntity(club, tag);
                return clubTag;
            })
            .collect(Collectors.toList());
        club.setClubTag(tags);
        Club savedClub = clubRepository.save(club);
        clubMemberRepository.save(ClubMember.toEntityWithHost(club, member));
        return ClubRegisterResponse.of(savedClub.getId());
    }

    public ClubUpdateResponse updateClub(Long clubId,
        String name,
        String description,
        List<String> tagList, MultipartFile file)
        throws IOException {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));
        List<ClubTag> tags = tagList
            .stream()
            .map((tagContent) -> {
                Tag tag = tagRepository.findByContentLike(tagContent);
                if (tag == null) {
                    tag = Tag.builder()
                        .content(tagContent)
                        .build();
                    tagRepository.save(tag);
                }
                ClubTag clubTag = ClubRegisterRequest.toEntity(club, tag);
                return clubTag;
            })
            .collect(Collectors.toList());
        String imageUrl = null;
        if (!file.isEmpty()) {
            String preImg = club.getClubImageUrl();
            if (preImg != null && !preImg.startsWith(this.DEFAULT_IMAGE_S3)) {
                s3Service.deleteFile(preImg);
            }
            imageUrl = s3Service.uploadFile(file);
        } else {
            imageUrl = club.getClubImageUrl();
        }
        club.updateClub(name, description, tags, imageUrl);
        Club savedClub = clubRepository.save(club);
        return ClubUpdateResponse.of(savedClub.getId());
    }

    public InvitationRegisterResponse registInvitation(Long clubId,
        InvitationRegisterRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));

        ClubMember clubMember = clubMemberRepository.findByClubAndMember(club, member);
        if (clubMember == null) {
            clubMemberRepository.save(InvitationRegisterRequest.toEntity(club, member));
            noticeRepository.save(Notice.toEntity(member, clubId, NoticeType.CLUB_INVITATION));
        } else {
            return InvitationRegisterResponse.of("이미 초대된 사용자입니다.");
        }
        return InvitationRegisterResponse.of(clubId);
    }

    public ClubLeaveResponse leaveClub(Long clubId, Long memberId) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        ClubMember clubMember = clubMemberRepository.findByClubAndMember(club, member);
        clubMemberRepository.delete(clubMember);
        return ClubLeaveResponse.of(memberId);
    }

    public ClubApprovalResponse approveClub(Long clubMemberId) {
        ClubMember clubMember = clubMemberRepository.findById(clubMemberId)
            .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
        clubMember.approve();
        clubMemberRepository.save(clubMember);

        Long clubId = clubMember.getClub().getId();

        return ClubApprovalResponse.of(clubId, clubMember.getMember().getId());
    }

    public ClubJoinResponse joinClub(String id, Long clubId) {
        Member member = memberRepository.findById(Long.parseLong(id))
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));
        ClubMember clubMember = clubMemberRepository.findByClubAndMember(club, member);
        if (clubMember == null) {
            clubMemberRepository.save(InvitationRegisterRequest.toEntity(club, member));
            noticeRepository.save(Notice.toEntity(club.getHost(), club.getHost().getId(),
                NoticeType.CLUB_REGISTRATION));
        } else {
            return ClubJoinResponse.of("이미 신청한 소모임입니다.");
        }
        return ClubJoinResponse.of(clubId);
    }

    public Slice<ClubMemberResponse> getClubMemberList(Long clubId, Pageable pageable) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));
        Slice<ClubMember> list = clubMemberRepository.findByClubAndIsApproved(club, true, pageable);
        List<ClubMemberResponse> clubMemberResponses = list.stream()
            .map((clubMember ->
                ClubMemberResponse.of(clubMember.getId(), clubMember.getMember())
            ))
            .collect(Collectors.toList());
        return new SliceImpl<>(clubMemberResponses, pageable, list.hasNext());
    }

    public void deleteClub(Long clubId) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));
        club.getClubMember().stream().forEach((clubMember -> {
            clubMemberRepository.delete(clubMember);
        }));
        club.getClubTag().stream().forEach(clubTag -> {
            clubTagRepository.delete(clubTag);
        });
        clubRepository.delete(club);
    }

    public ClubDetailResponse getDetailClub(String memberId, Long clubId) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));
        List<String> tags = club.getClubTag().stream()
            .map((clubTag -> {
                return clubTag.getTag().getContent();
            }))
            .collect(Collectors.toList());
        boolean isHost = club.getHost().getId() == Long.parseLong(memberId);
        ClubDetailResponse clubDetailResponse = ClubDetailResponse.of(club, isHost, tags);
        return clubDetailResponse;
    }

    public Slice<ClubListResponse> recommendClubList(String memberId, Pageable pageable) {
        Member member = memberRepository.getReferenceById(Long.parseLong(memberId));
        Slice<ClubListResponse> list = clubRepository.findUnionList(member, pageable);
        list.stream().forEach((response -> {
            Club club = clubRepository.findById(response.getClubId()).get();
            List<String> tags = clubTagRepository.findByClub(club).stream()
                .map((clubTag -> {
                    return clubTag.getTag().getContent();
                }))
                .collect(Collectors.toList());
            response.setTags(tags);
        }));
        return list;
    }

    public Slice<ClubMemberResponse> applicationList(Long clubId, Pageable pageable) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));
        Slice<ClubMember> list = clubMemberRepository.findByClubAndIsApprovedNot(club, true,
            pageable);
        List<ClubMemberResponse> clubMemberResponses = list.stream()
            .map((clubMember ->
                ClubMemberResponse.of(clubMember.getId(), clubMember.getMember())
            ))
            .collect(Collectors.toList());
        return new SliceImpl<>(clubMemberResponses, pageable, list.hasNext());
    }
}
