package com.mvdmstudy.oca.answer;

public record AnswerDto(String text, boolean isCorrect, Long id) {
    public static AnswerDto from(Answer answer) {
        return new AnswerDto(answer.getText(), answer.isCorrect(), answer.getId());
    }

    public static AnswerDto from(AnswerBodyDto answerBodyDto) {
        return new AnswerDto(answerBodyDto.text(), answerBodyDto.isCorrect(), answerBodyDto.id());
    }
}
