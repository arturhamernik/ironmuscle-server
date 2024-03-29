package com.muscle.trainings.dto;

import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDto {
    private Long id;
    private String name;
    private String type;
    private String image;
    private String difficulty;
    private Integer points;
}
