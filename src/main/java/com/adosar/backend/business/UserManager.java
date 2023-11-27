package com.adosar.backend.business;

import com.adosar.backend.business.request.user.*;
import com.adosar.backend.business.response.user.GetAllUsersResponse;
import com.adosar.backend.business.response.user.GetUserByIdResponse;
import com.adosar.backend.business.response.user.LoginUserResponse;
import com.adosar.backend.business.response.user.UserQueryResponse;
import org.springframework.http.HttpStatus;

public interface UserManager {
	HttpStatus ActivateUser(ActivateUserRequest request);

	HttpStatus createNewUser(CreateNewUserRequest request);

	GetAllUsersResponse getAllUsers(GetAllUsersRequest request);

	GetUserByIdResponse getUserById(GetUserByIdRequest request);

	LoginUserResponse loginUser(LoginUserRequest request);

	HttpStatus RemoveUser(RemoveUserRequest request);

	UserQueryResponse getUsersByPartialData(UserQueryRequest request);
}
