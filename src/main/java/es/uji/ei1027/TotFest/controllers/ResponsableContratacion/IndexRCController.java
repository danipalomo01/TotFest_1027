package es.uji.ei1027.TotFest.controllers.ResponsableContratacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("responsableContratacion")
public class IndexRCController {

    @RequestMapping("/indexRC")
    public String goToIndex(HttpSession session) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "/responsableContratacion/indexRC";
    }
}
