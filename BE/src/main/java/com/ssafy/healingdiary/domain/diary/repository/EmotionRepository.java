package com.ssafy.healingdiary.domain.diary.repository;

import com.ssafy.healingdiary.domain.diary.domain.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion, Integer> {

}