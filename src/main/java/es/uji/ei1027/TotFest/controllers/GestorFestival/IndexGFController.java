package es.uji.ei1027.TotFest.controllers.GestorFestival;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("gestorFestival")
public class IndexGFController {

    @RequestMapping("/indexGF")
    public String goToIndex(HttpSession session) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "/gestorFestival/indexGF";
    }
}
