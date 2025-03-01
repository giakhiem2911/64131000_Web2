package khiemnhg.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {
	//URL cho action này, tính từ root
	//hhtp://ten-mien:cong/HelloSpringMVC/say-hi
	@RequestMapping("say-hi")
	public String SayHello() {
		//code xử lý
		return "chao"; // view sẽ là chao.jsp
	}
}
