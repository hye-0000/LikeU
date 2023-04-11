package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.fasterxml.jackson.annotation.OptBoolean;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeablePersonRepository extends JpaRepository<LikeablePerson, Long> {
    List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId);
    Optional<LikeablePerson> findByFromInstaMemberIdAndToInstaMemberId(Long fromInstaMemberId, Long toInstaMemberId);
    Optional<LikeablePerson> findByToInstaMemberUsername(String toName);
}
