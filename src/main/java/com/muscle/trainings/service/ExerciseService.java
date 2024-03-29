package com.muscle.trainings.service;

import com.muscle.trainings.dto.ExerciseDto;
import com.muscle.trainings.entity.Exercise;
import com.muscle.trainings.mapper.ExerciseMapper;
import com.muscle.trainings.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseMapper mapper;
    private final ExerciseRepository exerciseRepository;

    public Page<Exercise> getPaginatedExercises(Pageable pageable, String query) {
        return exerciseRepository.findByNameContainsOrderByNameAsc(pageable, query);
    }

    public ExerciseDto saveExercise(ExerciseDto exerciseDto) {
        log.info("Saving new exercise {} to the database", exerciseDto.getName());

        return exerciseRepository.save(Exercise.builder()
            .name(exerciseDto.getName())
            .image(exerciseDto.getImage())
            .video(exerciseDto.getVideo())
            .build())
            .dto();
    }

    public ExerciseDto editExercise(ExerciseDto exerciseDto) {
        log.info("Editing exercise {} in the database", exerciseDto.getName());

        Exercise exerciseEntity = exerciseRepository.findExerciseById(exerciseDto.getId()).orElseThrow(() -> new IllegalStateException("Exercise not found!"));
        mapper.updateExerciseFromDto(exerciseDto, exerciseEntity);

        return exerciseRepository.save(exerciseEntity).dto();
    }

}
