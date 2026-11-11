package com.mygym.app.util;

public class PeopleConstants {

	public static final String ADMIN_CRUD_PATH = "/crud/admins";
	
	public static final String ADMIN_REQUEST_MAPPING = "/api/v1/admins";
	
	public final static String ID_ENPOINT = "/{id}";
	
	public final static String ADMIN_ID_ENDPOINT = "/adminid/{adminid}";
	
	public final static String LOGIN_ENPOINT = "/login";

	public final static String AUTHENTICATE_ENDPOINT = "/authenticate";
	
	public final static String PRIMARY_ADMIN_EXISTANCE_CHECK_ENDPOINT = "/primaryexists";
	
	public final static String CREATE_PRIMARY_ADMIN_ENDPOINT = "/createprimary";
	
	public final static String INACTIVE_ADMIN_ENDPOINT = "/inactiveadmins";
	
	public final static String UPDATE_ADMIN_STATUS_ENDPOINT = "/updatestatus/{id}";
	
	public static final String TRAINER_CRUD_PATH = "/crud/trainers";
	
	public static final String TRAINER_REQUEST_MAPPING = "/api/v1/trainers";
	
	public static final String TRAINER_GET_BRANCH__BY_ID = "/branchdetails/{trainerid}";
	
	public static final String TRAINER_UPDATE_APPROVAL = "/approve/{trainerid}";
	
	public static final String TRAINER_DISABLE_STATUS = "/disable/{trainerid}";
	
	public final static String ALL_INACTIVE_ENPOINT = "/allinactive";
	
	public static final String MEMBER_REQUEST_MAPPING = "/api/v1/members";
	
	public static final String MEMBER_CRUD_PATH = "/crud/members";
	
	public final static String MEMBER_UPDATE_ENDDATE_BY_ID = "/enddate/{id}";
	
	public final static String MEMBER_UPDATE_TRAINER_BY_ID = "/trainer/{id}";
	
	public final static String USERNAME_PLACEHOLDER_ENDPOINT = "/username/{username}";
	
	public final static String MEMBER_GET_BY_TRAINERID = "/bytrainerid/{trainerid}";
	
	public final static String MEMBER_UPDATE_FACE_REGIS = "/faceregis/{id}";
	
	public final static String MEMBER_UPDATE_APPROVAL = "/approve/{id}";
	
	public static final String RECORDS_CRUD_PATH = "/crud/my-records";
	
	public static final String RECORDS_REQUEST_MAPPING = "/api/v1/admins";
	
	public static final String RECORDS_GET_PREVIOUS = "/previous";
	
	public static final String BRANCH_CRUD_PATH = "/crud/branches";
	
	public static final String APPROVE_ID_PLACEHOLDER_ENDPOINT = "/approve/{id}";
	
	public static final String WORKOUT_CRUD_PATH = "/crud/workouts";

	public static final String MEMBER_REST_CLIENT_BEAN = "memberCrudClient";
	
	public static final String ADMIN_REST_CLIENT_BEAN = "adminCrudClient";
	
	public static final String TRAINER_REST_CLIENT_BEAN = "trainerCrudClient";
	
	public static final String WORKOUT_REST_CLIENT_BEAN = "workoutCrudClient";
	
	public static final String RECORDS_REST_CLIENT_BEAN = "recordsCrudClient";
	
	public static final String BRANCH_REST_CLIENT_BEAN = "branchCrudClient";
	
}
