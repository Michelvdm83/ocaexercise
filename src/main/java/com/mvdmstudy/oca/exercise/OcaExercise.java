package com.mvdmstudy.oca.exercise;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mvdmstudy.oca.answer.Answer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class OcaExercise {

    public OcaExercise(String question) {
        this.question = question;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String question;

    @OneToMany(mappedBy = "ocaExercise", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Answer> possibleAnswers = new HashSet<>();
}
