package com.mvdmstudy.oca.answer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mvdmstudy.oca.exercise.OcaExercise;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class Answer {
    @Id
    @GeneratedValue
    private Long id;

    @Setter
    private String text;
    @Setter
    private boolean isCorrect;

    @ManyToOne
    @JsonBackReference
    @Setter
    private OcaExercise ocaExercise;

    public Answer(String text, boolean isCorrect, OcaExercise ocaExercise) {
        this.text = text;
        this.isCorrect = isCorrect;
        this.ocaExercise = ocaExercise;
    }

    public Answer(AnswerDto answerDto) {
        this.text = answerDto.text();
        this.isCorrect = answerDto.isCorrect();
    }
}
