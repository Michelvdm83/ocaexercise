package com.mvdmstudy.oca.answer;

public record AnswerBodyDto(String text, Boolean isCorrect, Long id, Long exerciseId) {
}
