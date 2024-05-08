package com.mvdmstudy.oca.exercise;

import com.mvdmstudy.oca.answer.AnswerBodyDto;

import java.util.List;

public record OcaExercisePatchDto(Long id, String question, List<AnswerBodyDto> answers) {
}
