package com.muscle.trainings.service;

import com.muscle.trainings.dto.UserTrainingHistoryDto;
import com.muscle.trainings.entity.UserTrainingHistory;
import com.muscle.trainings.repository.TrainingsRepository;
import com.muscle.trainings.repository.UserTrainingHistoryRepository;
import com.muscle.trainings.other.TrainingHistory;
import com.muscle.trainings.responses.UserTrainingHistoryResponse;
import com.muscle.user.dto.IronUserDto;
import com.muscle.user.repository.UserRepository;
import com.muscle.user.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserTrainingHistoryService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final TrainingsRepository trainingsRepository;
    private final UserTrainingHistoryRepository userTrainingHistoryRepository;

    public UserTrainingHistoryDto saveUserActivity(String header, Long trainingId) {
        return userTrainingHistoryRepository
                .save(UserTrainingHistory.builder()
                        .user(userRepository.findByUsername(jwtUtil.extractUsernameFromHeader(header))
                                .orElseThrow(() -> new IllegalStateException("User not found")))
                        .training(trainingsRepository.findTrainingById(trainingId)
                                .orElseThrow(() -> new IllegalStateException("Training not found")))
                        .date(LocalDateTime.now())
                        .build())
                .dto();
    }

    public UserTrainingHistoryResponse getUserTrainingHistory(String header) {
        IronUserDto user = userRepository.findByUsername(jwtUtil.extractUsernameFromHeader(header)).orElseThrow(() -> new IllegalStateException("User not found")).dto();
        List<UserTrainingHistory> userTrainingHistory = userTrainingHistoryRepository.findByUserId(user.getId());

        return UserTrainingHistoryResponse.builder()
                .user(user)
                .trainingHistory(userTrainingHistory.stream()
                        .map(userTrainingHistoryItem -> TrainingHistory.builder()
                                .id(userTrainingHistoryItem.getTraining().getId())
                                .name(userTrainingHistoryItem.getTraining().getName())
                                .image(userTrainingHistoryItem.getTraining().getImage())
                                .difficulty(userTrainingHistoryItem.getTraining().getDifficulty())
                                .points(userTrainingHistoryItem.getTraining().getPoints())
                                .date(userTrainingHistoryItem.getDate())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}