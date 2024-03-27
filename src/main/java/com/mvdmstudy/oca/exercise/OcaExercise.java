package com.mvdmstudy.oca.exercise;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mvdmstudy.oca.answer.Answer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
public class OcaExercise {

    public OcaExercise(String question) {
        this.question = question;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String question;


    @OneToMany(mappedBy = "ocaExercise")
    @JsonBackReference
    private final Set<Answer> possibleAnswers = new HashSet<>();
}