package com.muscle.trainings.service;

import com.muscle.trainings.dto.TrainingDto;
import com.muscle.trainings.entity.Training;
import com.muscle.trainings.mapper.TrainingMapper;
import com.muscle.trainings.repository.TrainingsRepository;
import com.muscle.trainings.responses.TrainingResponse;
import com.muscle.user.repository.UserRepository;
import com.muscle.user.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingsService {

    private final TrainingMapper mapper;
    private final JwtUtil jwtUtil;
    private final TrainingsRepository trainingsRepository;
    private final UserRepository userRepository;

    public List<TrainingResponse> getTrainingsByType(String type) {
        return trainingsRepository.findByType(type.toLowerCase().replaceAll(" ", "")).stream().map(training -> TrainingResponse.builder()
                .id(training.getId())
                .name(training.getName())
                .type(training.getType())
                .difficulty(training.getDifficulty())
                .image(training.getImage())
                .points(training.getPoints())
                .build()).collect(Collectors.toList());
    }

    public List<TrainingResponse> getTrainings() {
        return trainingsRepository.findAll().stream().map(Training::response).collect(Collectors.toList());
    }

    public Page<Training> getPaginatedTrainings(Pageable pageable, String query) {
        return trainingsRepository.findByNameContainsOrDifficultyContains(query, query, pageable);
    }

    public TrainingResponse saveTraining(TrainingDto trainingDto) {
        log.info("Saving new training {} to the database", trainingDto.getName());

        return trainingsRepository.save(Training.builder()
                .name(trainingDto.getName())
                .type(trainingDto.getType())
                .image(trainingDto.getImage())
                .difficulty(trainingDto.getDifficulty())
                .points(trainingDto.getPoints())
                .build())
                .detailedResponse();
    }

    public TrainingResponse editTraining(TrainingDto trainingDto) {
        log.info("Editing training {} in the database", trainingDto.getName());

        Training trainingEntity = trainingsRepository.findTrainingById(trainingDto.getId()).orElseThrow(() -> new IllegalStateException("Training not found!"));
        mapper.updateTrainingFromDto(trainingDto, trainingEntity);

        return trainingsRepository.save(trainingEntity).response();
    }
}
