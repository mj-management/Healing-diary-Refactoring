package com.ssafy.healingdiary.domain.club.repository;

import com.ssafy.healingdiary.domain.club.domain.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long>, ClubRepositoryCustom {

}
