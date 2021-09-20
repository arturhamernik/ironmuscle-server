package com.muscle.trainings.entity;

import com.muscle.trainings.dto.UserTrainingHistoryDto;
import com.muscle.user.entity.IronUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter

@Entity
@Data
public class UserTrainingHistory {

    @Id
    @SequenceGenerator(
            name = "user_training_history_sequence",
            sequenceName = "user_training_history_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_training_history_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private IronUser user;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "training_id"
    )
    private Training training;
    private LocalDateTime date;

    public UserTrainingHistoryDto dto(){
        return UserTrainingHistoryDto.builder()
                .id(this.id)
                .user(this.user.dto())
                .training(this.training.dto())
                .date(this.date)
                .build();

    }
}