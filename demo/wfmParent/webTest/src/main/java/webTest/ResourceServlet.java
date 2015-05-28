package webTest;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ResourceServlet extends HttpServlet {
    private static final String HEADER_IFMODSINCE = "If-Modified-Since";
    private static final String HEADER_LASTMOD = "Last-Modified";
    
	private GregorianCalendar calendar = new GregorianCalendar(2015, 1, 1);

	@Override
	protected long getLastModified(HttpServletRequest req) {
		return this.calendar.getTimeInMillis();
	}
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
		ServletOutputStream out = resp.getOutputStream();
		out.println("test");
		out.close();
    }	
	
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long ifModifiedSince = req.getDateHeader(HEADER_IFMODSINCE);
		long lastModified = getLastModified(req);	
		if (ifModifiedSince < (lastModified / 1000 * 1000)) {
			System.out.println(" call doGet ----");
		} else {
			System.out.println(" xxxxxxxxxxxxxxxxxxx  ");			
		}		
		super.service(req, resp);
	}	
}
