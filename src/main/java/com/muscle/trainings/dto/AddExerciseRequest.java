package com.muscle.trainings.dto;

import lombok.*;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AddExerciseRequest {
    private Long exerciseId;
    private Integer time;
    private Integer repetitions;
}
