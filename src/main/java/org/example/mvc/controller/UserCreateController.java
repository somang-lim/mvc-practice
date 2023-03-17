package org.example.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.example.mvc.model.User;
import org.example.mvc.repository.UserRepository;

public class UserCreateController implements Controller {

	@Override
	public String handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// user 추가
		UserRepository.save(new User(request.getParameter("userId"), request.getParameter("name")));

		return "redirect:/users";
	}
}
