package com.mygym.app.controller;

import static com.mygym.app.util.PeopleConstants.ADMIN_REQUEST_MAPPING;
import static com.mygym.app.util.PeopleConstants.CREATE_PRIMARY_ADMIN_ENDPOINT;
import static com.mygym.app.util.PeopleConstants.ID_ENPOINT;
import static com.mygym.app.util.PeopleConstants.INACTIVE_ADMIN_ENDPOINT;
import static com.mygym.app.util.PeopleConstants.LOGIN_ENPOINT;
import static com.mygym.app.util.PeopleConstants.PRIMARY_ADMIN_EXISTANCE_CHECK_ENDPOINT;
import static com.mygym.app.util.PeopleConstants.UPDATE_ADMIN_STATUS_ENDPOINT;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mygym.app.dto.AuthResponse;
import com.mygym.app.dto.LoginRequest;
import com.mygym.app.models.Admin;
import com.mygym.app.models.AdminWrapper;
import com.mygym.app.security.JwtUtil;
import com.mygym.app.service.AdminServices;


@RestController
@RequestMapping(ADMIN_REQUEST_MAPPING)
public class AdminController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
	private final JwtUtil jwtUtil;
	private final AdminServices service;

    public AdminController(AdminServices service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping
    public ResponseEntity<Boolean> create(@RequestBody Admin admin) {
    	if(service.createAdmin(admin).getAdminId() == "-1")
    		return ResponseEntity.status(HttpStatus.CONFLICT).body(false);
    	else
    		return ResponseEntity.status(HttpStatus.CREATED).body(true);
    }
    
    @GetMapping(PRIMARY_ADMIN_EXISTANCE_CHECK_ENDPOINT)
    public Short checkPrimaryAdminExists() {
        LOGGER.info("Reached Check Primary Admin Exists Endpoint");
        return service.primaryAdminCheck() ;
    }

    @PostMapping(CREATE_PRIMARY_ADMIN_ENDPOINT)
    public ResponseEntity<Object> createPrimaryAdmin(@RequestParam String password) {
    	if(service.createPrimaryAdmin(password))
    		return ResponseEntity.status(HttpStatus.CREATED).build();
    	return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PostMapping(LOGIN_ENPOINT)
    public ResponseEntity<AuthResponse> loginAdmin(@RequestBody LoginRequest request) {        
        Admin adminDetails = service.findCredByUsername(request.username());
        String storedPassword = Objects.nonNull(adminDetails) ? adminDetails.getPassword() : null;
        if (storedPassword != null && !storedPassword.isEmpty() && storedPassword.equals(request.password())) {
            // Generate standard tamper-proof session token for user tracking
            String secureToken = jwtUtil.generateToken(request.username(), "ADMIN");
            return ResponseEntity.ok(new AuthResponse(adminDetails.getIsActive(), "Authentication successful", 
            		secureToken, "ADMIN", adminDetails.getAdminId(), adminDetails.getBranch()));
        } else if (Objects.nonNull(adminDetails) && Objects.nonNull(adminDetails.getIsActive()) 
        		&& !adminDetails.getIsActive()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(false, "Account Disabled", null, null, null, null));
        }
        LOGGER.warn("Failed security assertion routine match on backend processing");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse(false, "Invalid verification credentials provided", null, null, null, null));
    }


    @GetMapping(ID_ENPOINT)
    public Admin readByAdminId(@PathVariable String id) {
        return service.getAdminById(id);
    }

    @PutMapping(ID_ENPOINT)
    public Admin update(@PathVariable String id, @RequestBody Admin admin) {
        return service.updateAdmin(id, admin);
    }
    
    @GetMapping(INACTIVE_ADMIN_ENDPOINT)
    public AdminWrapper findInactiveAdmins(){
    	return service.findInactiveAdmins();
    }
    
    @PutMapping(UPDATE_ADMIN_STATUS_ENDPOINT)
    public ResponseEntity<Void> updateAdminStatus(@PathVariable(name = "id") String adminId) {
    	
    	return service.updateAdminStatus(adminId);
    }
    
    @DeleteMapping(ID_ENPOINT)
    public Boolean deleteByAdminId(@PathVariable(name = "id") String adminId) {
    	return service.deleteByAdminId(adminId);
    }
}
