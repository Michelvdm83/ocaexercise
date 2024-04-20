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
public class AnswerController {

    private final AnswerRepository answerRepository;

    @GetMapping("/findall")
    public Iterable<AnswerDto> findAll() {
        return answerRepository.findAll().stream().map(AnswerDto::from).toList();
    }

    @GetMapping("{id}")
    public ResponseEntity<AnswerDto> findById(@PathVariable long id) {
        return answerRepository.findById(id).map(AnswerDto::from).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AnswerDto> create(@RequestBody Answer answer, UriComponentsBuilder ucb) {
        var ans = answerRepository.save(answer);
        URI locationOfAnswer = ucb.path("/oca/answers/{id}").buildAndExpand(ans.getId()).toUri();
        return ResponseEntity.created(locationOfAnswer).body(AnswerDto.from(ans));
    }

    @PutMapping
    public ResponseEntity<?> replace(@RequestBody Answer answer) {
        var answerID = answer.getId();
        if (answerID == null) throw new WrongInputException("PUT needs id of answer in body");

        var possibleAnswer = answerRepository.findById(answerID);
        if (possibleAnswer.isEmpty()) return ResponseEntity.notFound().build();
        answerRepository.save(answer);
        return ResponseEntity.ok(AnswerDto.from(answer));
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> update(@RequestBody Answer answer, @PathVariable long id) {
        var idFromBody = answer.getId();
        if (idFromBody != null && idFromBody != id)
            throw new WrongInputException("id from body and path should be equal");

        var possibleAnswer = answerRepository.findById(id);
        if (possibleAnswer.isEmpty()) return ResponseEntity.notFound().build();
        var original = possibleAnswer.get();

        var answerText = answer.getText();
        if (answerText != null) original.setText(answerText);
        var answerCorrect = answer.isCorrect();
        if (answerCorrect != original.isCorrect()) original.setCorrect(answerCorrect);

        answerRepository.save(original);
        return ResponseEntity.ok(AnswerDto.from(original));
    }
}
