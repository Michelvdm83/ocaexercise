package com.mvdmstudy.oca;

import com.mvdmstudy.oca.answer.AnswerDto;
import com.mvdmstudy.oca.exercise.OcaExerciseDto;
import com.mvdmstudy.oca.exercise.OcaExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
    private final OcaExerciseService ocaExerciseService;

    @Override
    public void run(String... args) {
        if (ocaExerciseService.findAll().isEmpty()) {
            String question = "Which of the following types is not a primitive?";
            var answer1 = new AnswerDto("String", true, null);
            var answer2 = new AnswerDto("int", false, null);
            var answer3 = new AnswerDto("double", false, null);

            ocaExerciseService.saveNew(new OcaExerciseDto(question, List.of(answer1, answer2, answer3)));
        }
    }
}
