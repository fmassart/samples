package org.fred.samples;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet used for testing executor.
 */
public class ExecutorServlet extends HttpServlet {

	ExecutorService executor;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		final String ua = req.getHeader("User-Agent");
		System.out.println("User-Agent is:" + ua);

		executor.submit(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				System.out.println("Call in submit with user-agent:" + ua);
				return ua;
			}
		});

		PrintWriter writer = resp.getWriter();
		writer.write("Executor launched");
		writer.close();
	}

	@Override
	public void init() throws ServletException {
		executor = Executors.newFixedThreadPool(10);
	}

	@Override
	public void destroy() {
		executor.shutdown();
	}

}
