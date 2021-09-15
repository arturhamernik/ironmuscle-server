package com.muscle.trainings.service;

import com.muscle.trainings.dto.AddExerciseRequest;
import com.muscle.trainings.responses.ExerciseDetails;
import com.muscle.trainings.dto.TrainingExerciseDto;
import com.muscle.trainings.responses.TrainingDetails;
import com.muscle.trainings.entity.Exercise;
import com.muscle.trainings.entity.Training;
import com.muscle.trainings.entity.TrainingExercise;
import com.muscle.trainings.repository.ExerciseRepository;
import com.muscle.trainings.repository.TrainingExerciseRepository;
import com.muscle.trainings.repository.TrainingsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class TrainingExerciseService {

    TrainingExerciseRepository trainingExerciseRepository;
    TrainingsRepository trainingsRepository;
    ExerciseRepository exerciseRepository;


    public void addExercises(Long trainingId, List<AddExerciseRequest> exercises) {
        exercises.forEach(exercise -> save(trainingId, exercise));
    }

    public TrainingExerciseDto save(Long trainingId, AddExerciseRequest addExerciseRequest) {
        log.info("Saving new training exercise {} to the database", addExerciseRequest.getExerciseId());

        Training training = trainingsRepository.findTrainingById(trainingId).orElseThrow(() -> new IllegalStateException("Training not found!"));
        Exercise exercise = exerciseRepository.findExerciseById(addExerciseRequest.getExerciseId()).orElseThrow(() -> new IllegalStateException("Exercise not found!"));

        return trainingExerciseRepository.save(TrainingExercise.builder()
                .training(training)
                .exercise(exercise)
                .time(addExerciseRequest.getTime())
                .repetitions(addExerciseRequest.getRepetitions())
                .build())
                .dto();
    }

    public TrainingDetails getTrainingDetails(Long trainingId) {
        List<TrainingExercise> trainingExerciseList = trainingExerciseRepository.findByTrainingId(trainingId);

        if(trainingExerciseList.isEmpty()) {
            return TrainingDetails.builder().build();
        }

        return TrainingDetails.builder()
                .training(trainingExerciseList.get(1).getTraining().dto())
                .exercises(trainingExerciseList.stream().map(trainingExercise -> ExerciseDetails.builder()
                        .id(trainingExercise.getExercise().getId())
                        .name(trainingExercise.getExercise().getName())
                        .gif(trainingExercise.getExercise().getGif())
                        .video(trainingExercise.getExercise().getVideo())
                        .time(trainingExercise.getTime())
                        .repetitions(trainingExercise.getRepetitions())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    public void editExercises(Long trainingId, List<AddExerciseRequest> exercises) {

        trainingExerciseRepository.deleteByTrainingId(trainingId);
        addExercises(trainingId, exercises);
    }
}
