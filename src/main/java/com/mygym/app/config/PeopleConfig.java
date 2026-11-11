package com.mygym.app.config;

import static com.mygym.app.util.PeopleConstants.ADMIN_CRUD_PATH;
import static com.mygym.app.util.PeopleConstants.ADMIN_REST_CLIENT_BEAN;
import static com.mygym.app.util.PeopleConstants.BRANCH_CRUD_PATH;
import static com.mygym.app.util.PeopleConstants.BRANCH_REST_CLIENT_BEAN;
import static com.mygym.app.util.PeopleConstants.MEMBER_CRUD_PATH;
import static com.mygym.app.util.PeopleConstants.MEMBER_REST_CLIENT_BEAN;
import static com.mygym.app.util.PeopleConstants.RECORDS_CRUD_PATH;
import static com.mygym.app.util.PeopleConstants.RECORDS_REST_CLIENT_BEAN;
import static com.mygym.app.util.PeopleConstants.TRAINER_CRUD_PATH;
import static com.mygym.app.util.PeopleConstants.TRAINER_REST_CLIENT_BEAN;
import static com.mygym.app.util.PeopleConstants.WORKOUT_CRUD_PATH;
import static com.mygym.app.util.PeopleConstants.WORKOUT_REST_CLIENT_BEAN;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.mygym.app.client.DownstreamClients;

@Configuration
public class PeopleConfig {

    private static final String PEOPLE_CRUD = "people-crud";
    private static final String RECORDS_CRUD = "records-crud";
    private static final String RESOURCE_CRUD = "resource-crud";

    private final DownstreamClients clients;
    private final String peopleCrudUrl;
    private final String recordsCrudUrl;
    private final String resourceCrudUrl;

    public PeopleConfig(DownstreamClients clients,
            @Value("${mga.clients.people-crud-url}") String peopleCrudUrl,
            @Value("${mga.clients.records-crud-url}") String recordsCrudUrl,
            @Value("${mga.clients.resource-crud-url}") String resourceCrudUrl) {
        this.clients = clients;
        this.peopleCrudUrl = peopleCrudUrl;
        this.recordsCrudUrl = recordsCrudUrl;
        this.resourceCrudUrl = resourceCrudUrl;
    }

    @Bean(name = ADMIN_REST_CLIENT_BEAN)
    public RestClient restAdminClient() {
        return clients.create(PEOPLE_CRUD, peopleCrudUrl + ADMIN_CRUD_PATH);
    }

    @Bean(name = TRAINER_REST_CLIENT_BEAN)
    public RestClient restTrainerClient() {
        return clients.create(PEOPLE_CRUD, peopleCrudUrl + TRAINER_CRUD_PATH);
    }

    @Bean(name = MEMBER_REST_CLIENT_BEAN)
    public RestClient restCrudClient() {
        return clients.create(PEOPLE_CRUD, peopleCrudUrl + MEMBER_CRUD_PATH);
    }

    @Bean(name = RECORDS_REST_CLIENT_BEAN)
    public RestClient recordsCrudClient() {
        return clients.create(RECORDS_CRUD, recordsCrudUrl + RECORDS_CRUD_PATH);
    }

    @Bean(name = WORKOUT_REST_CLIENT_BEAN)
    public RestClient workoutCrudClient() {
        return clients.create(RECORDS_CRUD, recordsCrudUrl + WORKOUT_CRUD_PATH);
    }

    @Bean(name = BRANCH_REST_CLIENT_BEAN)
    public RestClient branchCrudClient() {
        return clients.create(RESOURCE_CRUD, resourceCrudUrl + BRANCH_CRUD_PATH);
    }
}
