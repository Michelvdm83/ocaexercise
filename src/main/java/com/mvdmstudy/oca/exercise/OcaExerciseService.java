package com.mvdmstudy.oca.exercise;

import com.mvdmstudy.oca.ExerciseSyntaxException;
import com.mvdmstudy.oca.answer.Answer;
import com.mvdmstudy.oca.answer.AnswerDto;
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

    private void checkForValidExercise(OcaExerciseDto ocaExerciseDto) {
        var answers = ocaExerciseDto.answers();
        if (answers.isEmpty() || answers.size() < 2) throw new ExerciseSyntaxException("Not enough answers!");
        if (!(answers.stream().anyMatch(AnswerDto::isCorrect) && answers.stream().anyMatch(answer -> !answer.isCorrect()))) {
            throw new ExerciseSyntaxException("An Exercise needs at least 1 correct and 1 incorrect answer");
        }
    }

    public OcaExercise saveNew(OcaExerciseDto ocaExerciseDto) {
        checkForValidExercise(ocaExerciseDto);


        OcaExercise exercise = ocaExerciseRepository.save(new OcaExercise(ocaExerciseDto.question()));

        var answers = ocaExerciseDto.answers().stream().map(Answer::new).toList();
        answers.forEach(a -> a.setOcaExercise(exercise));
        answerRepository.saveAll(answers);
        exercise.setPossibleAnswers(Set.copyOf(answers));

        return exercise;
    }

    public OcaExercise update(OcaExercise updatingExercise, OcaExercisePatchDto updatedExerciseFields) {
        var allAnswers = updatingExercise.getPossibleAnswers();
        var updateAnswers = updatedExerciseFields.answers();

        var newQuestion = updatedExerciseFields.question();
        if (newQuestion != null) updatingExercise.setQuestion(newQuestion);

        updateAnswers.forEach(a -> {
            if (a.id() == null) {
                if (a.isCorrect() == null || a.text() == null)
                    throw new ExerciseSyntaxException("New answers need text and isCorrect both present");
                Answer newAnswer = new Answer();
                newAnswer.setText(a.text());
                newAnswer.setCorrect(a.isCorrect());
                newAnswer.setOcaExercise(updatingExercise);
                allAnswers.add(newAnswer);
            } else {
                var possibleOldAnswer = allAnswers.stream().filter(answer -> answer.getId().equals(a.id())).findFirst();
                if (possibleOldAnswer.isPresent()) {
                    var oldAnswer = possibleOldAnswer.get();
                    var newText = a.text();
                    if (newText != null) oldAnswer.setText(newText);
                    if (a.isCorrect() != null) oldAnswer.setCorrect(a.isCorrect());
                } else
                    throw new ExerciseSyntaxException("Answer with id not linked to exercise present, id's should never be changed!");
            }
        });
        checkForValidExercise(OcaExerciseDto.from(updatingExercise));

        //answerRepository.saveAll(originalAnswers);//with CascadeType.ALL is this necessary?
        ocaExerciseRepository.save(updatingExercise);
        return updatingExercise;
    }

    public OcaExercise replace(OcaExercise oldExercise, OcaExercise newExercise) {
        checkForValidExercise(OcaExerciseDto.from(newExercise));
        var oldAnswers = answerRepository.findByOcaExercise(oldExercise);
        answerRepository.deleteAll(oldAnswers);

        var newAnswers = newExercise.getPossibleAnswers();
        newAnswers.forEach(a -> a.setOcaExercise(newExercise));
        answerRepository.saveAll(newAnswers);
        newExercise.setPossibleAnswers(newAnswers);

        ocaExerciseRepository.save(newExercise);
        return newExercise;
    }

    public void delete(long id) {
        ocaExerciseRepository.deleteById(id);
    }

    public OcaExerciseDto deleteAnswerFromExercise(long answerId, OcaExercise exercise) {
        var answersFromExercise = exercise.getPossibleAnswers();
        var possibleAnswer = answersFromExercise.stream().filter(a -> a.getId() == answerId).findFirst();
        if (possibleAnswer.isEmpty())
            throw new ExerciseSyntaxException("answer with id:" + answerId + " is not linked to exercise");
        answerRepository.deleteById(answerId);
        answersFromExercise.remove(possibleAnswer.get());
        return OcaExerciseDto.from(exercise);
    }

    public List<OcaExercise> findAll() {
        return ocaExerciseRepository.findAll();
    }

    public Optional<OcaExercise> findById(long id) {
        return ocaExerciseRepository.findById(id);
    }
}
