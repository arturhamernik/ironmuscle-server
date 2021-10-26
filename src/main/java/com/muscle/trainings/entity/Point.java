package com.muscle.trainings.entity;

import com.muscle.trainings.responses.PointResponse;
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
public class Point {
    @Id
    @SequenceGenerator(
            name = "point_sequence",
            sequenceName = "point_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "point_sequence"
    )
    private Long id;

    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private IronUser user;
    private Integer points;

    public PointResponse response() {
        return PointResponse.builder()
                .id(this.id)
                .points(this.points)
                .user(this.user.response())
                .build();
    }
}