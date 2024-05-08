package com.mvdmstudy.oca.answer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Setter
public class Answer {
    @Id
    @GeneratedValue
    private Long id;

    private String text;
    @JsonProperty("isCorrect")
    private boolean isCorrect;

    @ManyToOne
    @JsonBackReference
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Answer)) return false;
        return (((Answer) obj).text.equals(this.text)) && ((Answer) obj).isCorrect == this.isCorrect && ((Answer) obj).ocaExercise.equals(this.ocaExercise);
    }
}
