package com.mygym.app.service;

import static com.mygym.app.util.PeopleConstants.AUTHENTICATE_ENDPOINT;
import static com.mygym.app.util.PeopleConstants.ALL_INACTIVE_ENPOINT;
import static com.mygym.app.util.PeopleConstants.ID_ENPOINT;
import static com.mygym.app.util.PeopleConstants.TRAINER_DISABLE_STATUS;
import static com.mygym.app.util.PeopleConstants.TRAINER_REST_CLIENT_BEAN;
import static com.mygym.app.util.PeopleConstants.TRAINER_UPDATE_APPROVAL;
import static com.mygym.app.util.PeopleConstants.USERNAME_PLACEHOLDER_ENDPOINT;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestClient;

import com.mygym.app.models.Branch;
import com.mygym.app.dto.LoginRequest;
import com.mygym.app.models.Trainer;
import com.mygym.app.models.TrainerWrapper;

@Service
public class TrainerService {

	private RestClient restClient;

	private RestClient restBranchClient;

	public TrainerService(@Qualifier(TRAINER_REST_CLIENT_BEAN) RestClient restClient, @Qualifier("branchCrudClient") RestClient restBranchClient) {
		this.restClient = restClient;
		this.restBranchClient = restBranchClient;
	}

	public String createTrainer(Trainer trainer) {
		String trainerId = restClient.post()
				.body(trainer)
				.retrieve()
				.body(String.class);
		return trainerId;
	}

	public String updateTrainer(Trainer trainer) {
		String trainerId = restClient.put()
				.uri(ID_ENPOINT, trainer.trainerId())
				.body(trainer)
				.retrieve()
				.body(String.class);
		return trainerId;
	}

	public Trainer authenticate(LoginRequest request) {
		return restClient.post()
				.uri(AUTHENTICATE_ENDPOINT)
				.body(request)
				.retrieve()
				.body(Trainer.class);
	}

	public TrainerWrapper findInactiveTrainers(){
		return restClient.get()
				.uri(ALL_INACTIVE_ENPOINT)
				.retrieve()
				.body(TrainerWrapper.class);
	}

	public Trainer readById(@PathVariable String id) {
		return restClient.get()
				.uri(ID_ENPOINT, id)
				.retrieve()
				.body(Trainer.class);
	}

	public Trainer readByUsername(@PathVariable String username) {
		return restClient.get()
				.uri(USERNAME_PLACEHOLDER_ENDPOINT, username)
				.retrieve()
				.body(Trainer.class);
	}

	public Branch findTrainerBranch(String trainerId) {
		Trainer trainer = readById(trainerId);
		if(Objects.nonNull(trainer) && Objects.nonNull(trainer.branchId()))
			return restBranchClient.get()
					.uri(ID_ENPOINT, trainer.branchId())
					.retrieve()
					.body(Branch.class);
		else
			return Branch.notFound();
	}

	public TrainerWrapper findInactiveTrainersInBranch(String branchId) {
		TrainerWrapper trainerWrapper = findInactiveTrainers();
		return new TrainerWrapper(
				trainerWrapper.allTrainers().stream().filter(trainer -> 
				branchId.equals(trainer.branchId())).toList());
	}

	public Boolean approveTrainer(String trainerId) {
		return restClient.put()
		.uri(TRAINER_UPDATE_APPROVAL, trainerId)
		.retrieve()
		.body(Boolean.class);
	}

	public Boolean disableTrainer(String trainerId) {
		return restClient.put()
		.uri(TRAINER_DISABLE_STATUS, trainerId)
		.retrieve()
		.body(Boolean.class);
	}
	
	public Boolean deleteTrainer(String trainerId) {
		return restClient.delete()
		.uri(ID_ENPOINT, trainerId)
		.retrieve()
		.body(Boolean.class);
	}

}
