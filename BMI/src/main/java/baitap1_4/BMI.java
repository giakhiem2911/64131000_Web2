package baitap1_4;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class BMI
 */
@WebServlet("/BMI")
public class BMI extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BMI() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter traVe = response.getWriter();
		traVe.append("Bạn vừa gửi yêu cầu dạng Get! xin hãy nhập vào form sức khỏe!");
		String noiDungHTML = "<div class=\"container\">\r\n"
				+ "        <h2>Nhập Thông Tin</h2>\r\n"
				+ "        <form action=\"/BMI/BMI\" method=\"Post\">\r\n"
				+ "            <label for=\"height\">Chiều cao (cm):</label>\r\n"
				+ "            <input type=\"number\" name=\"height\" required>\r\n"
				+ "            \r\n"
				+ "            <label for=\"weight\">Cân nặng (kg):</label>\r\n"
				+ "            <input type=\"number\" name=\"weight\" required>\r\n"
				+ "            \r\n"
				+ "            <button type=\"submit\">Gửi</button>\r\n"
				+ "        </form>\r\n"
				+ "    </div>";
		traVe.append(noiDungHTML);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		String heightstr = request.getParameter("height");
		String weightstr = request.getParameter("weight");
		double height = Double.parseDouble(heightstr) / 100;
		double weight = Double.parseDouble(weightstr);
		double bmi = Math.round((weight/(height*height))*100/100.0);
		PrintWriter traVe = response.getWriter();
		traVe.append("Bạn vừa gửi yêu cầu Post! ");
		traVe.append(" Chiều cao: " + heightstr);
		traVe.append(" Cân nặng: " + weightstr);
		traVe.append(" BMI của bạn: " + bmi);
		if(bmi < 18.5) {
			traVe.append(" Gầy");
		} else if(bmi < 25) {
			traVe.append(" Bình thường");
		} else if(bmi < 30) {
			traVe.append(" Thừa cân");
		} else {
			traVe.append(" Béo phì");
		}
	}

}