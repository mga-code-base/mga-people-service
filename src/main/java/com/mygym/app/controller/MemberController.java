package com.mygym.app.controller;

import static com.mygym.app.util.PeopleConstants.ALL_INACTIVE_ENPOINT;
import static com.mygym.app.util.PeopleConstants.ID_ENPOINT;
import static com.mygym.app.util.PeopleConstants.LOGIN_ENPOINT;
import static com.mygym.app.util.PeopleConstants.MEMBER_GET_BY_TRAINERID;
import static com.mygym.app.util.PeopleConstants.MEMBER_REQUEST_MAPPING;
import static com.mygym.app.util.PeopleConstants.MEMBER_UPDATE_APPROVAL;
import static com.mygym.app.util.PeopleConstants.MEMBER_UPDATE_ENDDATE_BY_ID;
import static com.mygym.app.util.PeopleConstants.MEMBER_UPDATE_FACE_REGIS;
import static com.mygym.app.util.PeopleConstants.MEMBER_UPDATE_TRAINER_BY_ID;
import static com.mygym.app.util.PeopleConstants.USERNAME_PLACEHOLDER_ENDPOINT;

import java.util.Objects;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mygym.app.dto.LoginRequest;
import com.mygym.app.dto.MemberAuthResponse;
import com.mygym.app.models.Member;
import com.mygym.app.models.MemberWrapper;
import com.mygym.app.security.JwtUtil;
import com.mygym.app.service.MemberServices;
import com.mygym.app.validation.OnCreate;

@RestController
@RequestMapping(MEMBER_REQUEST_MAPPING)
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	private final MemberServices service;
	private final JwtUtil jwtUtil;

    public MemberController(MemberServices service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping
    public ResponseEntity<String> create(@Validated({Default.class, OnCreate.class}) @RequestBody Member member) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createMember(member));
    }
    
    @PostMapping(LOGIN_ENPOINT)
    public ResponseEntity<MemberAuthResponse> loginMember(@Valid @RequestBody LoginRequest request) {
        Member member = service.authenticate(request);
        if (Objects.nonNull(member) && !"-1".equals(member.memberId())) {
            String secureToken = jwtUtil.generateToken(request.username(), "MEMBER");
            return ResponseEntity.ok(new MemberAuthResponse(true, 
            		"Authentication successful", 
            		secureToken, 
            		member.memberId(),
            		member.trainerAssigned(),
            		member.isActive(),
            		member.isFacialRegCompleted()));
        }
        
        logger.warn("Failed security assertion routine match on backend processing");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new MemberAuthResponse(false, 
                		"Invalid verification credentials provided", 
                		null, null, null, null, null));
    }
    
    @PutMapping
    public String updateMember(@Valid @RequestBody Member member) {
    	return service.updateMember(member);
    }
    
    @PutMapping(MEMBER_UPDATE_ENDDATE_BY_ID)
    public Boolean updateMembershipEndDay(@PathVariable("id") String memberId, @RequestParam("duration") Integer duration) {
    	return service.updateMembershipEndDate(memberId, duration);
    }
    
    @PutMapping(MEMBER_UPDATE_TRAINER_BY_ID)
    public Boolean updateTrainerForMember(@PathVariable("id") String memberId, @RequestParam("trainerId") String trainerId) {
    	return service.updateTrainerForMember(memberId, trainerId);
    }
    
    @GetMapping
    public Member readById(@RequestParam String id) {
        return service.getMemberById(id);
    }
    
    @GetMapping(USERNAME_PLACEHOLDER_ENDPOINT)
    public Member readByUsername(@PathVariable("username") String username) {
        return service.getMemberByUsername(username);
    }
    
    @GetMapping(ALL_INACTIVE_ENPOINT)
    public MemberWrapper getAllInactive() {
        return service.getAllInactive();
    }
    
    @GetMapping(MEMBER_GET_BY_TRAINERID)
    public MemberWrapper findByTrainerId(@PathVariable("trainerid") String trainerId) {
        return service.findByTrainerId(trainerId);
    }
    
    @PutMapping(MEMBER_UPDATE_FACE_REGIS)
    public Boolean updateFaceRegistration(@PathVariable("id") String id) {
        return service.updateFaceRegistrationStatus(id);
    }
    
    @PutMapping(MEMBER_UPDATE_APPROVAL)
    public Boolean approveMembership(@PathVariable("id") String id, @RequestParam("duration") String duration) {
        return service.approveMembership(id, duration);
    }
    
    @DeleteMapping(ID_ENPOINT)
    public Boolean approveMembership(@PathVariable("id") String id) {
        return service.deleteMember(id);
    }
    
    
}
