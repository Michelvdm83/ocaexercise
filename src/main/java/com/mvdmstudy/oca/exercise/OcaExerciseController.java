package com.mvdmstudy.oca.exercise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oca/exercises")
public class OcaExerciseController {
    @Autowired
    OcaExerciseRepository ocaExerciseRepository;

    @GetMapping("/findall")
    public Iterable<OcaExerciseDto> findAll(){
        return ocaExerciseRepository.findAll().stream().map(OcaExerciseDto::from).toList();
    }
}
