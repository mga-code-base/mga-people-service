package com.mygym.app.service;

import static com.mygym.app.util.PeopleConstants.AUTHENTICATE_ENDPOINT;
import static com.mygym.app.util.PeopleConstants.ALL_INACTIVE_ENPOINT;
import static com.mygym.app.util.PeopleConstants.ID_ENPOINT;
import static com.mygym.app.util.PeopleConstants.MEMBER_GET_BY_TRAINERID;
import static com.mygym.app.util.PeopleConstants.MEMBER_REST_CLIENT_BEAN;
import static com.mygym.app.util.PeopleConstants.MEMBER_UPDATE_FACE_REGIS;
import static com.mygym.app.util.PeopleConstants.TRAINER_REST_CLIENT_BEAN;
import static com.mygym.app.util.PeopleConstants.USERNAME_PLACEHOLDER_ENDPOINT;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.mygym.app.dto.LoginRequest;
import com.mygym.app.models.Member;
import com.mygym.app.models.MemberWrapper;
import com.mygym.app.models.Trainer;

@Service
public class MemberServices {

	private RestClient restMemberClient;
	private RestClient restTrainerClient;

	public MemberServices(@Qualifier(MEMBER_REST_CLIENT_BEAN) RestClient restMemberClient, 
			@Qualifier(TRAINER_REST_CLIENT_BEAN) RestClient restTrainerClient) {
		this.restMemberClient = restMemberClient;
		this.restTrainerClient = restTrainerClient;
	}
	
	public String createMember(Member member) {
		String memberId = restMemberClient.post()
				.body(member)
				.retrieve()
				.body(String.class);
		return memberId;
	}

	public String updateMember(Member member) {
		String trainerId = restMemberClient.put()
				.uri(ID_ENPOINT, member.memberId())
				.body(member)
				.retrieve()
				.body(String.class);
		return trainerId;
	} 

	public Member getMemberById(String memberId) {
		return restMemberClient.get()
				.uri(ID_ENPOINT, memberId)
				.retrieve()
				.body(Member.class);
	}

	public Member authenticate(LoginRequest request) {
		return restMemberClient.post()
				.uri(AUTHENTICATE_ENDPOINT)
				.body(request)
				.retrieve()
				.body(Member.class);
	}

	public Member getMemberByUsername(String username) {
		return restMemberClient.get()
				.uri(USERNAME_PLACEHOLDER_ENDPOINT, username)
				.retrieve()
				.body(Member.class);
	}

	public MemberWrapper getAllInactive() {
		return restMemberClient.get()
				.uri(ALL_INACTIVE_ENPOINT)
				.retrieve()
				.body(MemberWrapper.class);
	}

	public MemberWrapper findInactiveMembers(){
		MemberWrapper inactive = getAllInactive();
		if (Objects.isNull(inactive)) {
			return MemberWrapper.empty();
		}
		return new MemberWrapper(inactive.allMembers().stream()
				.filter(member -> Objects.isNull(member.membershipStartDate()) && Objects.isNull(member.membershipEndDate()))
				.toList());
	}

	public MemberWrapper findByTrainerId(String trainerId) {
		return restMemberClient.get()
				.uri(MEMBER_GET_BY_TRAINERID, trainerId)
				.retrieve()
				.body(MemberWrapper.class);
	}

	public Boolean updateFaceRegistrationStatus(String id) {
		return restMemberClient.put()
				.uri(MEMBER_UPDATE_FACE_REGIS, id)
				.retrieve()
				.body(Boolean.class);
	}

	public Boolean approveMembership(String id, String duration) {
		return restMemberClient.put()
				.uri(uriBuilder -> uriBuilder
						.path("/approve/"+id)
						.queryParam("duration", duration)
						.build())
				.retrieve()
				.body(Boolean.class);
	}

	public Boolean deleteMember(String id) {
		return restMemberClient.delete()
				.uri(ID_ENPOINT, id)
				.retrieve()
				.body(Boolean.class);
	}

	public Boolean updateMembershipEndDate(String memberId, Integer duration) {
		return restMemberClient.put()
				.uri(uriBuilder -> uriBuilder
						.path("/enddate/" + memberId)
						.queryParam("duration", duration)
						.build())
				.retrieve()
				.body(Boolean.class);
	}

	public Boolean updateTrainerForMember(String memberId, String trainerId) {
		Trainer trainer = restTrainerClient.get()
							.uri(ID_ENPOINT, trainerId)
							.retrieve()
							.body(Trainer.class);
		if(Objects.nonNull(trainer) && !"-1".equals(trainer.trainerId()) && trainer.isActive())
			return restMemberClient.put()
				.uri(uriBuilder -> uriBuilder
						.path("/trainer/" + memberId)
						.queryParam("trainerId", trainerId)
						.build())
				.retrieve()
				.body(Boolean.class);
		else 
			return false;
	}
}
