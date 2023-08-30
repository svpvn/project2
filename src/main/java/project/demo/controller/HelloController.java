package project.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
	@GetMapping("/hello")
	public String hi() {
		//map url vao 1 ham, tra ve ten file view
		return "hi.html";
	}
}
