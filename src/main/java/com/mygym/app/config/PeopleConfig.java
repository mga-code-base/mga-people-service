package com.mygym.app.config;

import static com.mygym.app.util.PeopleConstants.DEV_ADMIN_BASE_URL;
import static com.mygym.app.util.PeopleConstants.DEV_TRAINER_BASE_URL;
import static com.mygym.app.util.PeopleConstants.DEV_MEMBER_BASE_URL;
import static com.mygym.app.util.PeopleConstants.DEV_RECORDS_BASE_URL;
import static com.mygym.app.util.PeopleConstants.DEV_WORKOUT_BASE_URL;
import static com.mygym.app.util.PeopleConstants.DEV_BRANCH_BASE_URL;
import static com.mygym.app.util.PeopleConstants.PRD_ADMIN_BASE_URL;
import static com.mygym.app.util.PeopleConstants.PRD_TRAINER_BASE_URL;
import static com.mygym.app.util.PeopleConstants.PRD_MEMBER_BASE_URL;
import static com.mygym.app.util.PeopleConstants.PRD_RECORDS_BASE_URL;
import static com.mygym.app.util.PeopleConstants.PRD_WORKOUT_BASE_URL;
import static com.mygym.app.util.PeopleConstants.PRD_BRANCH_BASE_URL;
import static com.mygym.app.util.PeopleConstants.CONTENT_TYPE;
import static com.mygym.app.util.PeopleConstants.APP_JSON;
import static com.mygym.app.util.PeopleConstants.MEMBER_REST_CLIENT_BEAN;
import static com.mygym.app.util.PeopleConstants.ADMIN_REST_CLIENT_BEAN;
import static com.mygym.app.util.PeopleConstants.TRAINER_REST_CLIENT_BEAN;
import static com.mygym.app.util.PeopleConstants.RECORDS_REST_CLIENT_BEAN;
import static com.mygym.app.util.PeopleConstants.WORKOUT_REST_CLIENT_BEAN;
import static com.mygym.app.util.PeopleConstants.BRANCH_REST_CLIENT_BEAN;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class PeopleConfig {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(PeopleConfig.class);

    @Value("${app.environment}")
    private String env;

    @Bean(name = ADMIN_REST_CLIENT_BEAN)
    public RestClient restAdminClient() {
    	String baseUrl = env.contentEquals("DEV") ? DEV_ADMIN_BASE_URL : PRD_ADMIN_BASE_URL;
    	LOGGER.info("⚙️ Base URL set for Admin CRUD : " + baseUrl);
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(CONTENT_TYPE, APP_JSON)
                .build();
    }
    
    @Bean(name = TRAINER_REST_CLIENT_BEAN)
    public RestClient restTrainerClient() {
    	String baseUrl = env.contentEquals("DEV") ? DEV_TRAINER_BASE_URL : PRD_TRAINER_BASE_URL;
    	LOGGER.info("⚙️ Base URL set for Trainer CRUD : " + baseUrl);
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(CONTENT_TYPE, APP_JSON)
                .build();
    }
    
    @Bean(name = MEMBER_REST_CLIENT_BEAN)
    public RestClient restCrudClient() {
    	String baseUrl = env.contentEquals("DEV") ? DEV_MEMBER_BASE_URL : PRD_MEMBER_BASE_URL;
    	LOGGER.info("⚙️ Base URL set for Member CRUD : " + baseUrl);
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(CONTENT_TYPE, APP_JSON)
                .build();
    }
    
    @Bean(name = RECORDS_REST_CLIENT_BEAN)
    public RestClient recordsCrudClient() {
    	String baseUrl = env.contentEquals("DEV") ? DEV_RECORDS_BASE_URL : PRD_RECORDS_BASE_URL;
    	LOGGER.info("⚙️ Base URL set for Records CRUD : " + baseUrl);
    	return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(CONTENT_TYPE, APP_JSON)
                .build();
    }
    
    @Bean(name = WORKOUT_REST_CLIENT_BEAN)
    public RestClient workoutCrudClient() {
    	String baseUrl = env.contentEquals("DEV") ? DEV_WORKOUT_BASE_URL : PRD_WORKOUT_BASE_URL;
    	LOGGER.info("⚙️ Base URL set for Workout CRUD : " + baseUrl);
    	return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(CONTENT_TYPE, APP_JSON)
                .build();
    }
    
    @Bean(name = BRANCH_REST_CLIENT_BEAN)
    public RestClient branchCrudClient() {
    	String baseUrl = env.contentEquals("DEV") ? DEV_BRANCH_BASE_URL : PRD_BRANCH_BASE_URL;
    	LOGGER.info("⚙️ Base URL set for Branch CRUD : " + baseUrl);
    	return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(CONTENT_TYPE, APP_JSON)
                .build();
    }
}
