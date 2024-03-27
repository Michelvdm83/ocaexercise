package com.mvdmstudy.oca.answer;

public record AnswerDto(String text, boolean isCorrect) {
    public static AnswerDto from(Answer answer){
        return new AnswerDto(answer.getText(), answer.isCorrect());
    }
}
