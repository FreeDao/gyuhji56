package rays.web.salon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MetronicsTest {

	@RequestMapping("/admin/metronics")
	public ModelAndView sayHello() {

		String message = "Hello Metronics!";
		System.out.println(message);
		return new ModelAndView("metronics.base.test", "message", message);
	}
}
