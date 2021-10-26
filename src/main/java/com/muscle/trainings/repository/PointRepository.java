package com.muscle.trainings.repository;

import com.muscle.trainings.entity.Point;
import com.muscle.trainings.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    Optional<Point> findByUserUsername(String username);

    @Query(value = "SELECT * FROM POINT p ORDER BY p.points DESC LIMIT 100;", nativeQuery = true)
    List<Point> findFirstHundred();
}