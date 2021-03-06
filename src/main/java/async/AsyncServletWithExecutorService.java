package async;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/asyncServ3", asyncSupported=true)
public class AsyncServletWithExecutorService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource
	ManagedExecutorService mes;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AsyncContext acontext = request.startAsync(request, response);
		PrintWriter pw = response.getWriter();
		
		response.setContentType("text/html");
		
		pw.println("<h2>Async Servlet using @Resource ManagedExecutorService</h2>");
		pw.println("<br/>Thread: "+Thread.currentThread().getName());
		
		mes.execute(() -> {
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
