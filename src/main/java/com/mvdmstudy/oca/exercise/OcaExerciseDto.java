package com.mvdmstudy.oca.exercise;

import com.mvdmstudy.oca.answer.AnswerDto;

import java.util.List;

public record OcaExerciseDto(String question, List<AnswerDto> answers) {
    public static OcaExerciseDto from(OcaExercise ocaExercise) {
        List<AnswerDto> answers = ocaExercise.getPossibleAnswers().stream().map(AnswerDto::from).toList();
        return new OcaExerciseDto(ocaExercise.getQuestion(), answers);
    }

    public static OcaExerciseDto from(OcaExercisePatchDto ocaExercisePatchDto) {
        List<AnswerDto> answers = ocaExercisePatchDto.answers().stream().map(AnswerDto::from).toList();
        return new OcaExerciseDto(ocaExercisePatchDto.question(), answers);
    }
}
