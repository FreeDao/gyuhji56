package rays.web.salon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {

	@RequestMapping("/hello")
	public ModelAndView sayHello() {

		String message = "Hello World, Spring 4.0!";
		System.out.println(message);
		return new ModelAndView("hello", "message", message);
	}
}
