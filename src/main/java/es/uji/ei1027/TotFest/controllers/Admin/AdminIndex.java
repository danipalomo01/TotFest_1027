package es.uji.ei1027.TotFest.controllers.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class AdminIndex {

    @RequestMapping("/index/admin")
    public String goToIndex(HttpSession session) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }
        return "/admin/usuarios/indexAdmin";
    }
}
