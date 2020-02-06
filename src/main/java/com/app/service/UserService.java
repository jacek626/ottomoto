package com.app.service;

import com.app.entity.User;
import com.app.utils.Result;

public interface UserService {
	  Result saveUser(User user);
	  Result sentEmailWithAccountActivationLink(User user);
}
