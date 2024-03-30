package com.mvdmstudy.oca.exercise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Random;

@RestController
@RequestMapping("/oca/exercises")
public class OcaExerciseController {
    @Autowired
    OcaExerciseService ocaExerciseService;

    @GetMapping("/{id}")
    public ResponseEntity<OcaExerciseDto> getById(@PathVariable long id) {
        return ocaExerciseService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/find-all")
    public Iterable<OcaExerciseDto> findAll() {
        return ocaExerciseService.findAll().stream().map(OcaExerciseDto::from).toList();
    }

    @GetMapping("/find-random")
    public OcaExerciseDto findRandom() {
        var allExercises = ocaExerciseService.findAll().stream().map(OcaExerciseDto::from).toList();
        return allExercises.get(new Random().nextInt(allExercises.size()));
    }

    @PostMapping("/create")
    public ResponseEntity<OcaExerciseDto> create(@RequestBody OcaExerciseDto ocaExerciseDto, UriComponentsBuilder ucb) {
        var exercise = ocaExerciseService.save(ocaExerciseDto);
        URI locationOfNewExercise = ucb.path("/oca/exercises").buildAndExpand(exercise.getId()).toUri();
        return ResponseEntity.created(locationOfNewExercise).body(OcaExerciseDto.from(exercise));
        //return ocaExerciseService.save(ocaExerciseDto);
    }
}
