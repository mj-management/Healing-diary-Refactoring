package com.ssafy.healingdiary.domain.diary.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "emotion")
public class Emotion {
    @Id
    private int emotionCode;
    private String value;
}
