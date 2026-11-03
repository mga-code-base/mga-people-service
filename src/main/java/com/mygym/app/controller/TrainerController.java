package com.mygym.app.controller;

import static com.mygym.app.util.PeopleConstants.ALL_INACTIVE_ENPOINT;
import static com.mygym.app.util.PeopleConstants.ID_ENPOINT;
import static com.mygym.app.util.PeopleConstants.LOGIN_ENPOINT;
import static com.mygym.app.util.PeopleConstants.TRAINER_DISABLE_STATUS;
import static com.mygym.app.util.PeopleConstants.TRAINER_GET_BRANCH__BY_ID;
import static com.mygym.app.util.PeopleConstants.TRAINER_REQUEST_MAPPING;
import static com.mygym.app.util.PeopleConstants.TRAINER_UPDATE_APPROVAL;
import static com.mygym.app.util.PeopleConstants.USERNAME_PLACEHOLDER_ENDPOINT;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mygym.app.dto.AuthResponse;
import com.mygym.app.dto.LoginRequest;
import com.mygym.app.models.Branch;
import com.mygym.app.models.Trainer;
import com.mygym.app.models.TrainerWrapper;
import com.mygym.app.security.JwtUtil;
import com.mygym.app.service.TrainerService;

@RestController
@RequestMapping(TRAINER_REQUEST_MAPPING)
public class TrainerController {

	private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);
	private final TrainerService service;
	private final JwtUtil jwtUtil;

    public TrainerController(TrainerService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping(LOGIN_ENPOINT)
    public ResponseEntity<AuthResponse> loginTrainer(@RequestBody LoginRequest request) {
        Trainer trainer = service.authenticate(request);
        boolean authenticated = Objects.nonNull(trainer) && !"-1".equals(trainer.trainerId());
        if (authenticated && trainer.isActive()) {
            String secureToken = jwtUtil.generateToken(request.username(), "TRAINER");
            return ResponseEntity.ok(new AuthResponse(trainer.isActive(), "Authentication successful", 
            		secureToken, "TRAINER", trainer.trainerId(), trainer.branchId()));
        } else if (authenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(false, "Account Disabled", null, null, null, null));
        }
        logger.warn("Failed security assertion routine match on backend processing");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse(false, "Invalid verification credentials provided", null, null, null, null));
    }
    
    @PostMapping
    public String create(@RequestBody Trainer trainer) {
        return service.createTrainer(trainer);
    }
    
    @PutMapping
    public String updateTrainer(Trainer trainer) {
    	return service.updateTrainer(trainer);
    }
    
    @GetMapping(ALL_INACTIVE_ENPOINT + "/{branchId}")
    public TrainerWrapper findInactiveTrainers(@PathVariable("branchId") String branchId){
    	return service.findInactiveTrainersInBranch(branchId);
    }
    
    @GetMapping(ID_ENPOINT)
    public Trainer readById(@PathVariable("id") String id) {
        return service.readById(id);
    }
    
    @GetMapping(USERNAME_PLACEHOLDER_ENDPOINT)
    public Trainer readByUsername(@PathVariable("username") String username) {
        return service.readByUsername(username);
    }
    
    @GetMapping(TRAINER_GET_BRANCH__BY_ID)
    public Branch findTrainerBranch(@PathVariable("trainerid") String trainerId) {
    	return service.findTrainerBranch(trainerId);
    }
    
    @PutMapping(TRAINER_UPDATE_APPROVAL)
    public ResponseEntity<Boolean> approveTrainer(@PathVariable("trainerid") String trainerId) {
    	if(service.approveTrainer(trainerId))
    		return ResponseEntity.ok(true);
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }
    
    @PutMapping(TRAINER_DISABLE_STATUS)
    public Boolean disableTrainer(@PathVariable("trainerid") String trainerId) {
    	return service.disableTrainer(trainerId);
    }
    
    @DeleteMapping(ID_ENPOINT)
    public ResponseEntity<Boolean> deleteTrainer(@PathVariable("id") String trainerId) {
    	if(service.deleteTrainer(trainerId))
    		return ResponseEntity.ok(true);
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }
}
