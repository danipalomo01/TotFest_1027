package es.uji.ei1027.TotFest.controllers;

import es.uji.ei1027.TotFest.daos.UsuarioDao;
import es.uji.ei1027.TotFest.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    public void addUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); // Hashea la contraseña
        // Resto del código para insertar el usuario
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String checkLogin(@ModelAttribute("usuario") Usuario usuario,
                             BindingResult bindingResult, HttpSession session) {

        if (session.getAttribute("size") == null) {
            session.setAttribute("size", 10);
        }

        // Verifica si hay errores en la validación del formulario
        if (bindingResult.hasErrors()) {
            return "login";
        }

        // Busca el usuario en la base de datos
        Usuario usuarioBD = usuarioDao.getUsuarioByEmail(usuario.getEmail());

        if (usuarioBD == null || !passwordEncoder.matches(usuario.getPassword(), usuarioBD.getPassword())) {
            bindingResult.rejectValue("password", "error.password", "Usuario o contraseña incorrectos");
            return "login";
        }

        // Si la autenticación es correcta, guarda el usuario en la sesión
        session.setAttribute("user", usuarioBD);
        session.setAttribute("userName", usuarioBD.getNombre());

        // Redirecciona según el rol del usuario
        switch (usuarioBD.getRol()) {
            case "USUARIO":
                session.setAttribute("rol", "USUARIO");
                session.setAttribute("idUsuario", usuarioBD.getId());
                session.setAttribute("cif", null);
                session.setAttribute("idComercial", null);
                return "redirect:/";
            case "GESTOR_FESTIVALES":
                session.setAttribute("rol", "GESTOR_FESTIVALES");
                session.setAttribute("idComercial", null);
                session.setAttribute("cif", "1234");
                return "redirect:/gestorFestival/indexGF";
            case "RESPONSABLE_CONTRATACION":
                session.setAttribute("rol", "RESPONSABLE_CONTRATACION");
                session.setAttribute("cif", null);
                session.setAttribute("idComercial", "1234");
                return "redirect:/responsableContratacion/indexRC";
            case "ADMIN":
                session.setAttribute("rol", null);
                session.setAttribute("cif", null);
                session.setAttribute("admin", "1234");
                return "redirect:/admin/usuarios/indexAdmin";
            default:
                return "redirect:/index.html";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
