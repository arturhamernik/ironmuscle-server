package com.muscle.trainings;

import com.muscle.trainings.dto.*;
import com.muscle.trainings.entity.Training;
import com.muscle.trainings.other.CreateTrainingDto;
import com.muscle.trainings.other.TrainingDetails;
import com.muscle.trainings.responses.TrainingResponse;
import com.muscle.trainings.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.muscle.user.util.JwtUtil.generateErrorBody;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/training")
public class TrainingsController {

    private final TrainingsService trainingsService;
    private final TrainingExerciseService trainingExerciseService;

    /**
     * Get all trainings
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/all")
    ResponseEntity<Map<String, Object>> getTrainings(@RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "100") Integer size,
                                                    @RequestParam(defaultValue = "") String query) {
        Pageable paging = PageRequest.of(page, size);
        Page<Training> trainingPage = trainingsService.getPaginatedTrainings(paging, query);

        List<TrainingResponse> trainingsList = trainingPage.getContent()
                .stream().map(Training::response).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("trainings", trainingsList);
        response.put("currentPage", trainingPage.getNumber());
        response.put("totalItems", trainingPage.getTotalElements());
        response.put("totalPages", trainingPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    /**
     * Show training including exercises
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    ResponseEntity<TrainingDetails> getTrainingDetails(@PathVariable Long id) {
        return ResponseEntity.ok(trainingExerciseService.getTrainingDetails(id));
    }

    /**
     * Create training
     * @param trainingDto
     * @return
     */
    @PostMapping()
    ResponseEntity<TrainingResponse> createTraining(@RequestBody CreateTrainingDto trainingDto) {
        return ResponseEntity.ok(trainingsService.saveTraining(trainingDto));
    }

    /**
     * Edit training data
     * @param trainingDto
     * @return
     */
    @PutMapping()
    ResponseEntity<?> editTraining(@RequestBody TrainingDto trainingDto) {
        try {
            return ResponseEntity.ok(trainingsService.editTraining(trainingDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(generateErrorBody(400, e));
        }
    }

    /**
     * Add exercise to training
     * @param id
     * @param exercises
     * @return
     */
    @PostMapping("/{id}/exercises")
    ResponseEntity<?> addTrainingExercises(@PathVariable Long id, @RequestBody List<AddExerciseRequest> exercises) {
        try {
            trainingExerciseService.addExercises(id, exercises);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(generateErrorBody(400, e));
        }
    }

    /**
     * Edit training exercises
     * @param id
     * @param exercises
     */
    @PutMapping("/{id}/exercises")
    ResponseEntity<?> editTrainingExercises(@PathVariable Long id, @RequestBody List<AddExerciseRequest> exercises) {
        try {
            trainingExerciseService.editExercises(id, exercises);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(generateErrorBody(400, e));
        }
    }
}
