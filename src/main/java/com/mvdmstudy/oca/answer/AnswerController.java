package com.mvdmstudy.oca.answer;

import com.mvdmstudy.oca.WrongInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oca/answers")
@CrossOrigin
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping("{id}")
    public ResponseEntity<AnswerDto> findById(@PathVariable long id) {
        return answerService.findById(id).map(AnswerDto::from).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AnswerDto> create(@RequestBody AnswerBodyDto answer, UriComponentsBuilder ucb) {
        var ans = answerService.saveNew(answer);
        URI locationOfAnswer = ucb.path("/oca/answers/{id}").buildAndExpand(ans.id()).toUri();
        return ResponseEntity.created(locationOfAnswer).body(ans);
    }

    @PutMapping
    public ResponseEntity<?> replace(@RequestBody AnswerBodyDto answer) {
        return ResponseEntity.ok(answerService.replace(answer));
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> update(@RequestBody AnswerBodyDto answer, @PathVariable long id) {
        var idFromBody = answer.id();
        if (idFromBody != null && idFromBody != id)
            throw new WrongInputException("id from body and path should be equal");

        return ResponseEntity.ok(answerService.update(answer, id));
    }
}
