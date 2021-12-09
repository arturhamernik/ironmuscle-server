package com.muscle.trainings.responses;

import com.muscle.user.response.IronUserResponse;
import lombok.*;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TrainingRequestResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String difficulty;
    private String bodyPart;
    private LocalDateTime created_at;
    private LocalDateTime resolved_at;
    private IronUserResponse user;
    private IronUserResponse trainer;
    private TrainingResponse training;
}
