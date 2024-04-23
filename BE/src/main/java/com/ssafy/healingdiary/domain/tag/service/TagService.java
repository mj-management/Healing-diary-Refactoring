package com.ssafy.healingdiary.domain.tag.service;

import com.ssafy.healingdiary.domain.tag.dto.TagListResponse;
import com.ssafy.healingdiary.domain.tag.repository.TagRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    public List<TagListResponse> searchTagList(String tag) {
        List<TagListResponse> tagListResponse = tagRepository.findByContentStartingWith(tag)
            .stream()
            .map(TagListResponse::of)
            .collect(Collectors.toList());
        return tagListResponse;
    }
}
