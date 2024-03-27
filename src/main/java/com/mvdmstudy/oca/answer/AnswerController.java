package com.mvdmstudy.oca.answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oca/answers")
public class AnswerController {
    @Autowired
    AnswerRepository answerRepository;

    @GetMapping("/findall")
    public Iterable<Answer> findAll() {
        return answerRepository.findAll();
    }
}
