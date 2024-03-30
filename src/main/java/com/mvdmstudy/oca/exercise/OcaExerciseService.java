package com.mvdmstudy.oca.exercise;

import com.mvdmstudy.oca.answer.Answer;
import com.mvdmstudy.oca.answer.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OcaExerciseService {
    private final OcaExerciseRepository ocaExerciseRepository;
    private final AnswerRepository answerRepository;

    public OcaExercise save(OcaExerciseDto ocaExerciseDto) {
        var answers = ocaExerciseDto.answers().stream().map(Answer::new).toList();
        if (answers.isEmpty() || answers.size() < 2)
            throw new IllegalArgumentException("not enough answers");
        if (!(answers.stream().anyMatch(Answer::isCorrect) && answers.stream().anyMatch(answer -> !answer.isCorrect()))) {
            throw new IllegalArgumentException("need at least 1 correct and 1 incorrect answer");
        }

        OcaExercise exercise = ocaExerciseRepository.save(new OcaExercise(ocaExerciseDto.question()));
        answers.forEach(a -> a.setOcaExercise(exercise));
        answerRepository.saveAll(answers);
        exercise.setPossibleAnswers(Set.copyOf(answers));
        
        return exercise;
    }

    public List<OcaExercise> findAll() {
        return ocaExerciseRepository.findAll();
    }

    public Optional<OcaExerciseDto> findById(long id) {
        return ocaExerciseRepository.findById(id).map(OcaExerciseDto::from);
    }
}
