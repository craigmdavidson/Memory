package application.requests;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestHandler extends HttpServlet {
	private static final long serialVersionUID = -579016072065838066L;
	Runtime runtime = Runtime.getRuntime();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter w = resp.getWriter();
		if (req.getPathInfo().endsWith(".xml")) w.write(gadgetXml(req));
		else w.write(showMemory(req, resp));
		w.close();
	}

	public String gadgetXml(HttpServletRequest req){
		return 
	"<Module>" +  
	  "<ModulePrefs title=\"Memory\" height=\"200\" />"+   
	  "<Content type=\"url\" href=\"" + showMemoryUrl(req) + "\">" +  
	  "</Content>" +  
	"</Module>"; 
	}

	private String showMemoryUrl(HttpServletRequest req) {
		return "http://" +
				req.getServerName() + ":" + req.getServerPort() + 
				req.getContextPath() + "/"+ "show";
	}
	
	private String showMemory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		long maxMemory = runtime.maxMemory() / 1024;
		long totalMemory = runtime.totalMemory() / 1024;
		long freeMemory = runtime.freeMemory() / 1024;
		long usedMemory = totalMemory - freeMemory;	
		return
		  page(
			  style(),
			  title(req),
			  table(
			    tr("max", "total", "used", "free"),
			    tr(maxMemory, totalMemory, usedMemory, freeMemory)
			  )
		  );
	}

	private String title(HttpServletRequest req) {
		return "<h1>" + req.getServerName() + ":" + req.getServerPort() + "</h1>";
	}

	private String style() {
		return 
		"<style>" +
		"body {background:#becede;font-family:arial;font:14px;} " +
		"table{background:black;} " +
		"td{background:white;text-align:center;font-weight:bold;padding:4px;}" +
		"</style>";
	}

	private String page(String ...bits) {
		return join("<html>", "</html>", "", "", bits);
	}

	private String table(String ...rows) {
		return join("<table>", "</table>", "", "", rows);
	}

	private String tr(Object ...columns) {
		return join("<tr>", "</tr>", "<td>", "</td>", columns);
	}
	
	private String join(String wrapperStart, String wrapperEnd, String separatorStart, String separatorEnd, Object[] bits) {
		String result = "";
		for (Object r : bits) result += separatorStart + r + separatorEnd;
		return wrapperStart + result + wrapperEnd;
	}
}