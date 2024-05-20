package com.ssafy.healingdiary.domain.member.domain;

import com.ssafy.healingdiary.global.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "token")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "token_id"))
@AttributeOverride(name = "createdDate", column = @Column(name = "token_created_date"))
@AttributeOverride(name = "updatedDate", column = @Column(name = "token_updated_date"))
public class Token extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "expiration_date")
    private LocalDateTime expiration_date;



    }

