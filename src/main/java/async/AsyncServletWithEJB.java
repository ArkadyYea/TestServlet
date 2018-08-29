package async;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/asyncServ2", asyncSupported=true)
public class AsyncServletWithEJB extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Inject
	EJBWithAsyncMethod am;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AsyncContext acontext = request.startAsync(request, response);
		PrintWriter pw = response.getWriter();
		
		response.setContentType("text/html");
		
		pw.println("<h2>Async Servlet using EJBs @Asynchronous method</h2>");
		pw.println("<br/>Thread: "+Thread.currentThread().getName());
		
		
		am.async(() -> {
			try {
				pw.println("<BR/>Before sleep");
				pw.println("<br/>Thread: "+Thread.currentThread().getName());
				pw.flush();
				Thread.sleep(2000);
				pw.println("<BR/>After sleep");
				pw.flush();
				acontext.complete();
			} catch (Exception e) {
				System.out.println("Exception "+e.getMessage());
			}
			System.out.println("-+= From runnable =+-");
		});
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
}
