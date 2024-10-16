package es.uji.ei1027.TotFest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @RequestMapping("/")
    public String home() {
        return "index"; // Thymeleaf buscará en el directorio templates/index.html
    }
}
