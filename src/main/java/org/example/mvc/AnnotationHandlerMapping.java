package org.example.mvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.example.mvc.annotation.Controller;
import org.example.mvc.annotation.RequestMapping;
import org.example.mvc.controller.RequestMethod;
import org.reflections.Reflections;

public class AnnotationHandlerMapping implements HandlerMapping {
	private final Object[] basePackage;
	private Map<HandlerKey, AnnotationHandler> handlers = new HashMap<>();

	public AnnotationHandlerMapping(Object... basePackage) {
		this.basePackage = basePackage;
	}

	public void initialize() {
		Reflections reflections = new Reflections(basePackage);

		// HomeController
		Set<Class<?>> clazzesWithControllerAnnotation = reflections.getTypesAnnotatedWith(Controller.class, true);

		clazzesWithControllerAnnotation.forEach(clazz ->
				Arrays.stream(clazz.getDeclaredMethods()).forEach(declaredMethod -> {
					RequestMapping requestMappingAnnotation = declaredMethod.getDeclaredAnnotation(RequestMapping.class);

					// @RequestMapping(value = "/", method = RequestMethod.GET)
					Arrays.stream(getRequestMethods(requestMappingAnnotation))
						.forEach(requestMethod -> handlers.put(
							new HandlerKey(requestMethod, requestMappingAnnotation.value()), new AnnotationHandler(clazz, declaredMethod)
						));
				})
			);
	}

	private RequestMethod[] getRequestMethods(RequestMapping requestMappingAnnotation) {
		return requestMappingAnnotation.method();
	}

	@Override
	public Object findHandler(HandlerKey handlerKey) {
		return handlers.get(handlerKey);
	}
}
