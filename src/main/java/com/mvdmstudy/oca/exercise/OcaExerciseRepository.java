package com.mvdmstudy.oca.exercise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OcaExerciseRepository extends JpaRepository<OcaExercise, Long> {
}
