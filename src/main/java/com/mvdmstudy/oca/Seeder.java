package com.mvdmstudy.oca;

import com.mvdmstudy.oca.answer.Answer;
import com.mvdmstudy.oca.answer.AnswerRepository;
import com.mvdmstudy.oca.exercise.OcaExercise;
import com.mvdmstudy.oca.exercise.OcaExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
    private final OcaExerciseRepository ocaExerciseRepository;
    private final AnswerRepository answerRepository;

    @Override
    public void run(String... args) {
        if (ocaExerciseRepository.count() == 0) {
            var question1 = new OcaExercise("Which of the following types is not a primitive?");
            ocaExerciseRepository.save(question1);

            var answer1 = new Answer("String", true, question1);
            var answer2 = new Answer("int", false, question1);
            var answer3 = new Answer("double", false, question1);
            answerRepository.saveAll(List.of(answer1, answer2, answer3));
        }
    }
}
