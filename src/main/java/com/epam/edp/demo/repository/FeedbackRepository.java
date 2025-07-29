package com.epam.edp.demo.repository;

import com.epam.edp.demo.model.entity.Feedback;
import com.epam.edp.demo.repository.custom.FeedbackRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, String>, FeedbackRepositoryCustom {

    Page<Feedback> findByLocationId(String locationId, Pageable pageable);

    List<Feedback> findByIdIn(List<String> ids);
}
