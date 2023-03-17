package org.example.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpServlet 을 상속함으로 이 클래스는 Servlet 이 된다.
 * @WebServlet("/") : 어떤 경로로 요청이 들어오더라도 이 클래스가 실행된다.
 */
@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
	private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		log.info("DispatcherServlet#service");
	}
}
