package com.mvdmstudy.oca.answer;

import com.mvdmstudy.oca.exercise.OcaExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByOcaExercise(OcaExercise ocaExercise);
}
