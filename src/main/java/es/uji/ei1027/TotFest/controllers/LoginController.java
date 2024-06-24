package es.uji.ei1027.TotFest.controllers;

import es.uji.ei1027.TotFest.daos.UserDao;
import es.uji.ei1027.TotFest.models.UserDetails;
import es.uji.ei1027.TotFest.models.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private UserDao userDao;

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new UserDetails());
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String checkLogin(@ModelAttribute("user") UserDetails user,
                             BindingResult bindingResult, HttpSession session) {

        if (session.getAttribute("size") == null) {
            session.setAttribute("size", 10);
        }
        UserValidator userValidator = new UserValidator();
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "login";
        }

        // Comprova que el login siga correcte
        // intentant carregar les dades de l'usuari
        user = userDao.loadUserByUsername(user.getUsername(), user.getPassword());
        if (user == null) {
            bindingResult.rejectValue("password", "badpw", "Contrasenya incorrecta");
            return "login";
        }

        // Autenticats correctament.
        // Guardem les dades de l'usuari autenticat a la sessió

        session.setAttribute("user", user);
        session.setAttribute("userName", user.getUsername());
        if(user.getUserType() == UserType.GESTOR_FESTIVALES) {
            List<String> extraInfo = user.getUserExtraInfo();
            if (extraInfo.size() > 0) {
                String cif = user.getUserExtraInfo().get(0);
                cif.replaceAll("[\\[\\]]", "");
                session.setAttribute("cif", cif);
            }

            return "gestorFestival/indexGF";
        }
        if(user.getUserType() == UserType.RESPONSABLE_CONTRATACION) {
            List<String> extraInfo = user.getUserExtraInfo();
            if (extraInfo.size() > 0) {
                String idComercial = user.getUserExtraInfo().get(0);
                idComercial.replaceAll("[\\[\\]]", "");
                session.setAttribute("idComercial", idComercial);
            }
            return "responsableContratacion/indexRC";
        }else{
            return "redirect:/index.html";
        }
        // Torna a la pàgina principal

    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}