package com.mvdmstudy.oca.answer;

import com.mvdmstudy.oca.ExerciseSyntaxException;
import com.mvdmstudy.oca.WrongInputException;
import com.mvdmstudy.oca.exercise.OcaExerciseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.SyntaxException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final OcaExerciseRepository ocaExerciseRepository;

    public Optional<Answer> findById(long id) {
        return answerRepository.findById(id);
    }

    public AnswerDto saveNew(AnswerBodyDto answerBodyDto) {
        var idFromBody = answerBodyDto.id();
        if (idFromBody != null) throw new SyntaxException("id is created by database, should not be in body of POST");

        var exerciseId = answerBodyDto.exerciseId();
        if (exerciseId == null)
            throw new NullPointerException("exercise id can't be null, a new answer needs to belong to an exercise");
        var possibleExercise = ocaExerciseRepository.findById(exerciseId);
        if (possibleExercise.isEmpty())
            throw new EntityNotFoundException("exercise with id:" + exerciseId + " not found");

        if (answerBodyDto.isCorrect() == null || answerBodyDto.text() == null || answerBodyDto.text().isBlank()) {
            throw new SyntaxException("An answer needs text and isCorrect");
        }

        var answer = new Answer(answerBodyDto.text(), answerBodyDto.isCorrect(), possibleExercise.get());
        answerRepository.save(answer);

        return AnswerDto.from(answer);
    }

    public AnswerDto replace(AnswerBodyDto answerBodyDto) {
        var answerID = answerBodyDto.id();
        if (answerID == null) throw new WrongInputException("PUT needs id of answer in body");
        var possibleAnswer = answerRepository.findById(answerID);
        if (possibleAnswer.isEmpty()) throw new EntityNotFoundException("answer with id:" + answerID + " not found");

        if (answerBodyDto.isCorrect() == null || answerBodyDto.text() == null || answerBodyDto.text().isBlank()) {
            throw new SyntaxException("An answer needs text and isCorrect");
        }

        Answer answer = possibleAnswer.get();
        if (!answerBodyDto.exerciseId().equals(answer.getOcaExercise().getId())) {
            throw new SyntaxException("answer should remain to belong to the same exercise");
        }

        var possibleExercise = ocaExerciseRepository.findById(answerBodyDto.exerciseId());
        if (possibleExercise.isEmpty()) throw new EntityNotFoundException();

        var exercise = possibleExercise.get();
        Set<Answer> answers = exercise.getPossibleAnswers();

        answers.removeIf(a -> a.getId().equals(answerID));
        answer.setText(answerBodyDto.text());
        answer.setCorrect(answerBodyDto.isCorrect());
        answers.add(answer);

        if (answer.isCorrect() != answerBodyDto.isCorrect()) {
            if (!(answers.stream().anyMatch(Answer::isCorrect) && answers.stream().anyMatch(ans -> !ans.isCorrect()))) {
                throw new ExerciseSyntaxException("An Exercise needs to keep at least 1 correct and 1 incorrect answer");
            }
        }

        answerRepository.save(answer);
        return AnswerDto.from(answer);
    }

    public AnswerDto update(AnswerBodyDto answerBodyDto, long id) {
        var possibleAnswer = answerRepository.findById(id);
        if (possibleAnswer.isEmpty()) throw new EntityNotFoundException("answer with id:" + id + " not found");
        Answer original = possibleAnswer.get();

        var answerText = answerBodyDto.text();
        if (answerText != null) original.setText(answerText);
        var answerCorrect = answerBodyDto.isCorrect();

        var possibleExercise = ocaExerciseRepository.findById(answerBodyDto.exerciseId());
        if (possibleExercise.isEmpty()) throw new EntityNotFoundException();

        var exercise = possibleExercise.get();
        Set<Answer> answers = exercise.getPossibleAnswers();

        if (answerCorrect != null) original.setCorrect(answerCorrect);

        answers.removeIf(a -> a.getId().equals(id));
        answers.add(original);

        if (!(answers.stream().anyMatch(Answer::isCorrect) && answers.stream().anyMatch(ans -> !ans.isCorrect()))) {
            throw new ExerciseSyntaxException("An Exercise needs to keep at least 1 correct and 1 incorrect answer");
        }

        answerRepository.save(original);
        return AnswerDto.from(original);
    }
}
