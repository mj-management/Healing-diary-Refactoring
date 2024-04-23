package com.ssafy.healingdiary.domain.diary.repository;

import com.ssafy.healingdiary.domain.diary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long>, DiaryRepositoryCustom {}
