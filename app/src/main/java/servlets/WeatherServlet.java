package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import api.WeatherAPI;

/**
 * Servlet implementation class WeatherServlet
 */
@WebServlet("/WeatherServlet")
@MultipartConfig
public class WeatherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeatherServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String locationString = request.getParameter("location");
		String metricString = request.getParameter("metricsystem");
		String startdateString = request.getParameter("startdate");
		String enddateString = request.getParameter("enddate");
		
		WeatherAPI locationWeatherAPI = new WeatherAPI(locationString, metricString, startdateString, enddateString);
		String jsonString = "";
		
		try {
			JSONObject weather = locationWeatherAPI.request();
			jsonString = weather.toString();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Location Not Found");
		}
		
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter outPrintWriter = response.getWriter();
		
		outPrintWriter.write(jsonString);
		outPrintWriter.flush();
	}

}

