package com.mygym.app.service;

import static com.mygym.app.util.PeopleConstants.ADMIN_ID_ENDPOINT;
import static com.mygym.app.util.PeopleConstants.ADMIN_REST_CLIENT_BEAN;
import static com.mygym.app.util.PeopleConstants.APPROVE_ID_PLACEHOLDER_ENDPOINT;
import static com.mygym.app.util.PeopleConstants.BRANCH_REST_CLIENT_BEAN;
import static com.mygym.app.util.PeopleConstants.CREATE_PRIMARY_ADMIN_ENDPOINT;
import static com.mygym.app.util.PeopleConstants.ID_ENPOINT;
import static com.mygym.app.util.PeopleConstants.INACTIVE_ADMIN_ENDPOINT;
import static com.mygym.app.util.PeopleConstants.UPDATE_ADMIN_STATUS_ENDPOINT;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.mygym.app.models.Admin;
import com.mygym.app.models.AdminWrapper;

@Service
public class AdminServices {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminServices.class);
	private RestClient restAdminClient;
	private RestClient restBranchClient;

	public AdminServices(@Qualifier(ADMIN_REST_CLIENT_BEAN) RestClient restAdminClient,
			@Qualifier(BRANCH_REST_CLIENT_BEAN) RestClient restBranchClient) {
		this.restAdminClient = restAdminClient;
		this.restBranchClient = restBranchClient;
	}

	public Boolean isPrimaryAdminActive() {
		Admin primaryAdmin = getAdminById("0");
		return primaryAdmin.getAdminId() == "0";
	}

	public Boolean createPrimaryAdmin(String password) {
		
		LOGGER.info("⚙️ Base URL set for Admin CRUD : " + restAdminClient.get()
	    .uri("").exchange((request, response) -> request.getURI().toString()));
		return restAdminClient.post()
	            .uri(uriBuilder -> uriBuilder
	                    .path(CREATE_PRIMARY_ADMIN_ENDPOINT)
	                    .queryParam("password", password)
	                    .build())
	            .retrieve()
	            .body(Boolean.class);
	}

	public Admin createAdmin(Admin admin) {
		Admin newAdmin = restAdminClient.post()
				.body(admin)
				.retrieve()
				.body(Admin.class);
		if(!(Objects.nonNull(newAdmin) 
				&& Objects.nonNull(newAdmin.getAdminId()) 
				&& newAdmin.getAdminId().equals(Long.valueOf(-1)))); 
		return newAdmin;
	}
	
	public Admin getAdminById(String adminId) {
		return restAdminClient.get()
				.uri(ID_ENPOINT, adminId)
				.retrieve()
				.body(Admin.class);
		
	}
	
	public Short primaryAdminCheck() {
		try {
			Admin admin = restAdminClient.get()
					.uri(ID_ENPOINT, 0)
					.retrieve()
					.body(Admin.class);
			LOGGER.info("Fetching Primary Admin Details " + admin.getAdminId());
			if(admin.getAdminId().equals("0"))
				return 1;
			else
				return 0;
		} catch (Exception e) {
			LOGGER.error("Fetching Primary Admin Details threw Error: " + e.getMessage());
			return -1;
		}
		
	}

	public Admin updateAdmin(String id, Admin admin) {
		return restAdminClient.put()
				.uri(ID_ENPOINT, id)
				.body(admin)
				.retrieve()
				.body(Admin.class);
	}

	public Admin findCredByUsername(String username) {
		return restAdminClient.get()
	            .uri(uriBuilder -> uriBuilder
	                    .queryParam("username", username)
	                    .build())
	            .retrieve()
	            .body(Admin.class);
	}

    public AdminWrapper findInactiveAdmins(){
       	AdminWrapper admins = restAdminClient.get()
				.uri(INACTIVE_ADMIN_ENDPOINT)
				.retrieve()
				.body(AdminWrapper.class);
       	List<Admin> listOfAdminsWithoutPrimary = admins.getAllAdmins().stream()
       			.filter(admin -> !(admin.getUsername().matches("admin") 
       					&& admin.getAdminId().matches("0"))).toList();
       	admins.setAllAdmins(listOfAdminsWithoutPrimary);
       	return admins;
    }
    
    public Boolean deleteByAdminId(String adminId) {
    	Boolean deleteAdminRes = restAdminClient.delete()
				.uri(ID_ENPOINT, adminId)
				.retrieve()
				.body(Boolean.class);
    	
    	Boolean deleteBranchRes = restBranchClient.delete()
				.uri(ADMIN_ID_ENDPOINT, adminId)
				.retrieve()
				.body(Boolean.class);
    	return deleteAdminRes && deleteBranchRes;
    }

	public ResponseEntity<Void> updateAdminStatus(String adminId) {
		Boolean updateAdminRes = restAdminClient.put()
				.uri(UPDATE_ADMIN_STATUS_ENDPOINT, adminId)
				.retrieve()
				.body(Boolean.class);
		Boolean updateBranchRes = restBranchClient.put()
				.uri(APPROVE_ID_PLACEHOLDER_ENDPOINT, adminId)
				.retrieve()
				.body(Boolean.class);
    	if(updateAdminRes && updateBranchRes)
    		return ResponseEntity.ok().build();
    	return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}
}
