package com.mvdmstudy.oca.exercise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oca/exercises")
public class OcaExerciseController {
    @Autowired
    OcaExerciseService ocaExerciseService;

    @GetMapping("/findall")
    public Iterable<OcaExerciseDto> findAll() {
        return ocaExerciseService.findAll().stream().map(OcaExerciseDto::from).toList();
    }

    @PostMapping("/create")
    public OcaExerciseDto create(@RequestBody OcaExerciseDto ocaExerciseDto) {
        return ocaExerciseService.save(ocaExerciseDto);
    }
}
