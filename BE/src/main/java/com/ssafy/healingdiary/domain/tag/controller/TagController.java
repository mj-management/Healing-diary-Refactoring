package com.ssafy.healingdiary.domain.tag.controller;

import com.ssafy.healingdiary.domain.tag.dto.TagListResponse;
import com.ssafy.healingdiary.domain.tag.service.TagService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    @GetMapping
    public List<TagListResponse> searchTagList(@RequestParam String tag) {
        return tagService.searchTagList(tag);
    }
}
