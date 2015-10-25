package com.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/ManageThreadsServlet")
public class ManageThreadsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageThreadsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		EventsListener el = (EventsListener)request.getServletContext().getAttribute("EventsListener");
		String numOfThreadsToAdd = request.getParameter("numOfThreadsToAdd");
		String numOfThreadsToRemove = request.getParameter("numOfThreadsToRemove");
		if (numOfThreadsToAdd!=null) {
			int before = el.getTotNumberOfThreads();
			el.addThreads(new Integer(numOfThreadsToAdd));
			
			String log=numOfThreadsToAdd+" emulator threads succesfully added. They were: "+before+" Now they are:"+el.getTotNumberOfThreads();
			response.getWriter().println(log);
			System.out.println("Servlet invoked - "+log);
			
		}
		if (numOfThreadsToRemove!=null) {
			int before = el.getTotNumberOfThreads();
			int succesfullyRemoved = el.removeThreads(new Integer(numOfThreadsToRemove));
			String log = succesfullyRemoved+" emulator threads succesfully removed. They were: "+before+" Now they are: "+el.getTotNumberOfThreads();
			response.getWriter().println(log);
			System.out.println("Servlet invoked - "+log);
		}

	}



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
