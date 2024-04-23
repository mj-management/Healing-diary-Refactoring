package com.ssafy.healingdiary.domain.diary.repository;

import com.ssafy.healingdiary.domain.club.domain.Club;
import com.ssafy.healingdiary.domain.club.domain.ClubTag;
import com.ssafy.healingdiary.domain.diary.domain.DiaryTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DiaryTagRepository extends JpaRepository<DiaryTag, Long> {
}
