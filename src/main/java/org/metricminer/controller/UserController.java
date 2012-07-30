package org.metricminer.controller;

import org.metricminer.infra.dao.UserDao;
import org.metricminer.infra.encryptor.Encryptor;
import org.metricminer.infra.interceptor.PublicAccess;
import org.metricminer.infra.session.UserSession;
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
	private final UserSession session;

	public UserController(Result result, Validator validator,
			Encryptor encryptor, UserDao dao, UserSession session) {
		this.result = result;
		this.validator = validator;
		this.encryptor = encryptor;
		this.dao = dao;
		this.session = session;
	}

	@PublicAccess
	@Get("/login")
	public void loginForm() {
	}

	@PublicAccess
	@Post("/login")
	public void login(String email, String password) {
		User user = dao.findByEmail(email);
		password = encryptor.encrypt(password);
		if (user != null && user.getPassword().equals(password)) {
			session.login(user);
			result.redirectTo(IndexController.class).index();
		} else {
			result.include("email", email);
			validator
					.add(new ValidationMessage(
							"The email or password you entered is incorrect.",
							"error"));
		}
		validator.onErrorRedirectTo(UserController.class).loginForm();
	}

	@PublicAccess
	@Get("/signup")
	public void userForm() {
	}

	@PublicAccess
	@Post("/signup")
	public void registerUser(final User user) {
		validator.validate(user);
		if (!user.isValid()) {
			validator.add(new ValidationMessage(
					"Password confirmation don't match.", "error"));
		}
		result.include("user", user);
		validator.onErrorRedirectTo(UserController.class).userForm();

		user.encryptPassword(encryptor);
		dao.save(user);

		result.include("message", "You can login into MetricMiner now.");
		result.redirectTo(UserController.class).loginForm();
	}
}
