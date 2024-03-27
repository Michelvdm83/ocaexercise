package com.mvdmstudy.oca.answer;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mvdmstudy.oca.exercise.OcaExercise;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
public class Answer {
    @Id
    @GeneratedValue
    private Long id;

    private String text;
    private boolean isCorrect;

    @ManyToOne
    @Setter
    @JsonManagedReference
    private OcaExercise ocaExercise;

    public Answer(String text, boolean isCorrect, OcaExercise ocaExercise){
        this.text = text;
        this.isCorrect = isCorrect;
        this.ocaExercise = ocaExercise;
    }
}
