package com.muscle.trainings.entity;

import com.muscle.trainings.dto.TrainingDto;
import com.muscle.user.entity.IronUser;
import lombok.*;

import javax.persistence.*;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter

@Entity
@Data
public class Training {

    @Id
    @SequenceGenerator(
            name = "training_sequence",
            sequenceName = "training_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "training_sequence"
    )
    private Long id;
    private String name;
    private String image;
    private String difficulty;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "creator_id"
    )
    private IronUser creator;
    private Integer points;

    public TrainingDto dto() {
        return TrainingDto.builder()
                .id(this.id)
                .name(this.name)
                .image(this.image)
                .difficulty(this.difficulty)
                .creator(this.creator.dto())
                .points(this.points)
                .build();
    }
}
