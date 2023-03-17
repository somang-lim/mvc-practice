package org.example.mvc;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.example.mvc.controller.RequestMethod;
import org.example.mvc.view.JspViewResolver;
import org.example.mvc.view.ModelAndView;
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
	private List<HandlerMapping> handlerMappings;
	private List<HandlerAdapter> handlerAdapters;
	private List<ViewResolver> viewResolvers;

	@Override
	public void init() {
		RequestMappingHandlerMapping rmhm = new RequestMappingHandlerMapping();
		rmhm.init();

		AnnotationHandlerMapping ahm = new AnnotationHandlerMapping("org.example");
		ahm.initialize();

		handlerMappings = List.of(rmhm, ahm);
		handlerAdapters = List.of(new SimpleControllerHandlerAdapter(), new AnnotationHandlerAdapter());
		viewResolvers = Collections.singletonList(new JspViewResolver());
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		log.info("[DispatcherServlet] service started.");

		String requestURI = request.getRequestURI();
		RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

		// 1. handler mapping
		Object handler = handlerMappings.stream()
			.filter(hm -> hm.findHandler(new HandlerKey(requestMethod, requestURI)) != null)
			.map(hm -> hm.findHandler(new HandlerKey(requestMethod, requestURI)))
			.findFirst()
			.orElseThrow(() -> new ServletException("No handler for [" + requestMethod + ", " + requestURI + "]"));

		try {
			// 2. handler adapter
			HandlerAdapter handlerAdapter = handlerAdapters.stream()
				.filter(ha -> ha.supports(handler))
				.findFirst()
				.orElseThrow(() -> new ServletException("No adapter for handler [" + handler + "]"));

			// 3-4. handler adapter --> controller, viewName
			ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

			// 5. view resolver
			for (ViewResolver viewResolver : viewResolvers) {
				View view = viewResolver.resolveView(modelAndView.getViewName());
				view.render(modelAndView.getModel(), request, response);
			}
		} catch (Exception e) {
			log.error("exception occurred: [{}]", e.getMessage(), e);
			throw new ServletException(e);
		}
	}
}
