package com.mygym.app.util;

public class PeopleConstants {

	public final static String CONTENT_TYPE = "Content-Type";
	
	public final static String APP_JSON = "application/json";

	public final static String DEV_ADMIN_BASE_URL = "http://mga-people-crud-service/crud/admins";
	
	public final static String PRD_ADMIN_BASE_URL = "http://mga-people-crud-service.default.svc.cluster.local:80/crud/admins";
	
	public static final String ADMIN_REQUEST_MAPPING = "/api/admins";
	
	public final static String ID_ENPOINT = "/{id}";
	
	public final static String ADMIN_ID_ENDPOINT = "/adminid/{adminid}";
	
	public final static String LOGIN_ENPOINT = "/login";

	public final static String AUTHENTICATE_ENDPOINT = "/authenticate";
	
	public final static String PRIMARY_ADMIN_EXISTANCE_CHECK_ENDPOINT = "/primaryexists";
	
	public final static String CREATE_PRIMARY_ADMIN_ENDPOINT = "/createprimary";
	
	public final static String INACTIVE_ADMIN_ENDPOINT = "/inactiveadmins";
	
	public final static String UPDATE_ADMIN_STATUS_ENDPOINT = "/updatestatus/{id}";
	
	public final static String DEV_TRAINER_BASE_URL = "http://mga-people-crud-service/crud/trainers";
	
	public final static String PRD_TRAINER_BASE_URL = "http://mga-people-crud-service.default.svc.cluster.local:80/crud/trainers";
	
	public static final String TRAINER_REQUEST_MAPPING = "/api/trainers";
	
	public static final String TRAINER_GET_BRANCH__BY_ID = "/branchdetails/{trainerid}";
	
	public static final String TRAINER_UPDATE_APPROVAL = "/approve/{trainerid}";
	
	public static final String TRAINER_DISABLE_STATUS = "/disable/{trainerid}";
	
	public final static String ALL_INACTIVE_ENPOINT = "/allinactive";
	
	public static final String MEMBER_REQUEST_MAPPING = "/api/members";
	
	public final static String DEV_MEMBER_BASE_URL = "http://mga-people-crud-service/crud/members";
	
	public final static String PRD_MEMBER_BASE_URL = "http://mga-people-crud-service.default.svc.cluster.local:80/crud/members";
	
	public final static String MEMBER_UPDATE_ENDDATE_BY_ID = "/enddate/{id}";
	
	public final static String MEMBER_UPDATE_TRAINER_BY_ID = "/trainer/{id}";
	
	public final static String USERNAME_PLACEHOLDER_ENDPOINT = "/username/{username}";
	
	public final static String MEMBER_GET_BY_TRAINERID = "/bytrainerid/{trainerid}";
	
	public final static String MEMBER_UPDATE_FACE_REGIS = "/faceregis/{id}";
	
	public final static String MEMBER_UPDATE_APPROVAL = "/approve/{id}";
	
	public final static String DEV_RECORDS_BASE_URL = "http://mga-records-crud-service/crud/my-records";
	
	public final static String PRD_RECORDS_BASE_URL = "http://mga-records-crud-service.default.svc.cluster.local:80/crud/my-records";
	
	public static final String RECORDS_REQUEST_MAPPING = "/api/admins";
	
	public static final String RECORDS_GET_PREVIOUS = "/previous";
	
	public final static String DEV_BRANCH_BASE_URL = "http://mga-resource-crud-service/crud/branches";
	
	public final static String PRD_BRANCH_BASE_URL = "http://mga-resource-crud-service.default.svc.cluster.local:80/crud/branches";
	
	public static final String APPROVE_ID_PLACEHOLDER_ENDPOINT = "/approve/{id}";
	
	public final static String DEV_WORKOUT_BASE_URL = "http://mga-records-crud-service/crud/workouts";
	
	public final static String PRD_WORKOUT_BASE_URL = "http://mga-records-crud-service.default.svc.cluster.local:80/crud/workouts";

	public static final String MEMBER_REST_CLIENT_BEAN = "memberCrudClient";
	
	public static final String ADMIN_REST_CLIENT_BEAN = "adminCrudClient";
	
	public static final String TRAINER_REST_CLIENT_BEAN = "trainerCrudClient";
	
	public static final String WORKOUT_REST_CLIENT_BEAN = "workoutCrudClient";
	
	public static final String RECORDS_REST_CLIENT_BEAN = "recordsCrudClient";
	
	public static final String BRANCH_REST_CLIENT_BEAN = "branchCrudClient";
	
}
