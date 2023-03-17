package org.example.mvc;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.example.mvc.controller.Controller;
import org.example.mvc.controller.RequestMethod;
import org.example.mvc.view.JspViewResolver;
import org.example.mvc.view.View;
import org.example.mvc.view.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpServlet 을 상속함으로 이 클래스는 Servlet 이 된다.
 * @WebServlet("/") : 어떤 경로로 요청이 들어오더라도 이 클래스가 실행된다.
 */
@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
	private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
	private RequestMappingHandlerMapping rmhm;
	private List<ViewResolver> viewResolvers;

	@Override
	public void init() throws ServletException {
		rmhm = new RequestMappingHandlerMapping();
		rmhm.init();

		viewResolvers = Collections.singletonList(new JspViewResolver());
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("[DispatcherServlet] service started.");
		try {
			Controller handler = rmhm.findHandler(new HandlerKey(RequestMethod.valueOf(request.getMethod()), request.getRequestURI()));
			// redirect vs forward
			String viewName = handler.handleRequest(request, response);

			for (ViewResolver viewResolver : viewResolvers) {
				View view = viewResolver.resolveView(viewName);
				view.render(new HashMap<>(), request, response);
			}
		} catch (Exception e) {
			log.error("exception occurred: [{}]", e.getMessage(), e);
			throw new ServletException(e);
		}
	}
}
