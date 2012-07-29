package org.metricminer.controller;

import org.metricminer.infra.dao.UserDao;
import org.metricminer.infra.encryptor.Encryptor;
import org.metricminer.model.User;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Resource
public class UserController {

	private final Result result;
	private final Validator validator;
	private final Encryptor encryptor;
	private final UserDao dao;

	public UserController(Result result, Validator validator, Encryptor encryptor, UserDao dao) {
		this.result = result;
		this.validator = validator;
		this.encryptor = encryptor;
		this.dao = dao;
	}

	@Get("/login")
	public void loginForm(String message) {
		result.include("message", message);
	}
	
	@Post("/login")
	public void login(String email, String password) {
		dao.findByEmail(email);
	}

	@Get("/signup")
	public void userForm() {
	}

	@Post("/signup")
	public void registerUser(final User user) {
		if (!user.isValid()) {
			System.out.println(user);
			validator.add(new ValidationMessage("Password confirmation don't match.", "error"));
		}
		result.include("user", user);
		validator.onErrorRedirectTo(UserController.class).loginForm("You can login into MetricMiner now.");
		
		user.encryptPassword(encryptor);
		dao.save(user);
		
		result.redirectTo(IndexController.class).index();
	}
}
