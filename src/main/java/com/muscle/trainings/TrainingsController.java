package com.muscle.trainings;

import com.muscle.trainings.dto.*;
import com.muscle.trainings.other.TrainingDetails;
import com.muscle.trainings.responses.TrainingResponse;
import com.muscle.trainings.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/training")
public class TrainingsController {

    private final TrainingsService trainingsService;
    private final TrainingExerciseService trainingExerciseService;

    /**
     * Get template trainings
     * @return
     */
    @GetMapping("/template")
    List<TrainingResponse> getTemplateTrainings() {
        return trainingsService.getTemplateTrainings();
    }

    /**
     * Show all trainings
     * @return
     */
    @GetMapping()
    List<TrainingResponse> getTrainings() {
        return trainingsService.getTrainings();
    }

    /**
     * Show training including exercises
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    TrainingDetails getTrainingDetails(@PathVariable Long id) {
        return trainingExerciseService.getTrainingDetails(id);
    }

    /**
     * Create training
     * @param header
     * @param trainingDto
     * @return
     */
    @PostMapping()
    TrainingResponse createTraining(@RequestHeader("Authorization") String header, @RequestBody TrainingDto trainingDto) {
        return trainingsService.saveTraining(header, trainingDto);
    }

    /**
     * Edit training data
     * @param trainingDto
     * @return
     */
    @PutMapping()
    TrainingResponse editTraining(@RequestBody TrainingDto trainingDto) {
        return trainingsService.editTraining(trainingDto);
    }

    /**
     * Add exercise to training
     * @param id
     * @param exercises
     * @return
     */
    @PostMapping("/{id}/exercises")
    void addTrainingExercises(@PathVariable Long id, @RequestBody List<AddExerciseRequest> exercises) {
        trainingExerciseService.addExercises(id, exercises);
    }

    /**
     * Edit training exercises
     * @param id
     * @param exercises
     */
    @PutMapping("/{id}/exercises")
    void editTrainingExercises(@PathVariable Long id, @RequestBody List<AddExerciseRequest> exercises) {
        trainingExerciseService.editExercises(id, exercises);
    }

    //TODO: Request o nowy trening - done
    //TODO: Wysylanie requestow do trenera z najmniejszym oblozeniem??? innym kryterium??? - done
    //TODO: GET pojedynczego requesta - done
    //TODO: Zastanowic sie nad iloscia zwracanych danych z endpointow - done
    //TODO: Zabezpieczenie endpointów - done
    //TODO: Jak zbierac punkty uzytkownikow???
}
