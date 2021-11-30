package com.muscle.trainings.dto;

import com.muscle.user.dto.IronUserDto;
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
    private byte[] image;
    private String difficulty;
    private Integer points;
}
