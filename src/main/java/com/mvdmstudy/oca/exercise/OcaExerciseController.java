package com.mvdmstudy.oca.exercise;

import com.mvdmstudy.oca.ExerciseSyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/oca/exercises")
public class OcaExerciseController {
    private final OcaExerciseService ocaExerciseService;

    @GetMapping("/{id}")
    public ResponseEntity<OcaExerciseDto> getById(@PathVariable long id) {
        return ocaExerciseService.findById(id).map(OcaExerciseDto::from).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Iterable<OcaExerciseDto> findAll() {
        return ocaExerciseService.findAll().stream().map(OcaExerciseDto::from).toList();
    }

    @GetMapping("/find-random")
    public OcaExerciseDto findRandom() {
        var allExercises = ocaExerciseService.findAll().stream().map(OcaExerciseDto::from).toList();
        return allExercises.get(new Random().nextInt(allExercises.size()));
    }

    @PostMapping
    public ResponseEntity<OcaExerciseDto> create(@RequestBody OcaExerciseDto ocaExerciseDto, UriComponentsBuilder ucb) {
        var exercise = ocaExerciseService.saveNew(ocaExerciseDto);
        URI locationOfNewExercise = ucb.path("/oca/exercises").buildAndExpand(exercise.getId()).toUri();
        return ResponseEntity.created(locationOfNewExercise).body(OcaExerciseDto.from(exercise));
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> update(@RequestBody OcaExerciseDto ocaExerciseDto, @PathVariable long id) {
        var possibleExercise = ocaExerciseService.findById(id);
        if (possibleExercise.isEmpty()) return ResponseEntity.notFound().build();

        var updatedExercise = possibleExercise.get();

        var newQuestion = ocaExerciseDto.question();
        if (newQuestion != null) updatedExercise.setQuestion(newQuestion);

        ocaExerciseService.update(updatedExercise);
        return ResponseEntity.ok(ocaExerciseDto);
    }

    @PutMapping
    public ResponseEntity<?> replace(@RequestBody OcaExercise ocaExercise) {
        var exerciseId = ocaExercise.getId();
        if (exerciseId == null) throw new ExerciseSyntaxException("PUT needs the id of the exercise in the body!");

        var oldExercise = ocaExerciseService.findById(exerciseId);
        if (oldExercise.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(OcaExerciseDto.from(ocaExerciseService.replace(oldExercise.get(), ocaExercise)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        if (ocaExerciseService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            ocaExerciseService.delete(id);
            return ResponseEntity.noContent().build();
        }
    }
}
