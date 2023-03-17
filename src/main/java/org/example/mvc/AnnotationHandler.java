package org.example.mvc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationHandler {
	private final Class<?> clazz;
	private final Method targetMethod;

	public AnnotationHandler(Class<?> clazz, Method targetMethod) {
		this.clazz = clazz;
		this.targetMethod = targetMethod;
	}

	public String handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
		Object targetHandler = declaredConstructor.newInstance();

		return (String) targetMethod.invoke(targetHandler, request, response);
	}
}
