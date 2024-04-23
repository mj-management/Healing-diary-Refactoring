package com.ssafy.healingdiary.domain.club.repository;

import com.ssafy.healingdiary.domain.club.domain.Club;
import com.ssafy.healingdiary.domain.club.domain.ClubTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClubTagRepository extends JpaRepository<ClubTag, Long> {

    List<ClubTag> findByClub(Club club);
}
