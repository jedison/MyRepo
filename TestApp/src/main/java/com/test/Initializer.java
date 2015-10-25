package com.test;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Initializer implements ServletContextListener {

	ThreadsController tc = new ThreadsController(this, System.currentTimeMillis());

	@Override
	public void contextDestroyed(ServletContextEvent context) {
		System.out.println("############## APPLICATION STOPPING "+tc.getMyId());
		tc.stopAsap();
		System.out.println("############## APPLICATION STOPPED");
	}

	@Override
	public void contextInitialized(ServletContextEvent context) {
		System.out.println("############## APPLICATION STARTING "+tc.getMyId());
		context.getServletContext().setAttribute("EventsListener", tc);
		tc.start();
		System.out.println("############## APPLICATION STARTED");
		
	}

}
