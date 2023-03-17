package org.example;

import java.io.File;

import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebApplicationServer {
	private static final Logger log = LoggerFactory.getLogger(WebApplicationServer.class);

	public static void main(String[] args) throws Exception {
		String webappDirLocation = "webapps/"; // --> 루트 디렉토리
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);

		// http://localhost:8080/ 으로 연결하면, webapps 디렉토리를 바라보기
		tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
		log.info("configuring app with baseDir: {}", new File("./" + webappDirLocation).getAbsolutePath());

		tomcat.start();
		tomcat.getServer().await();
	}
}
