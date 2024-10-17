package es.uji.ei1027.TotFest.controllers.Registro;

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

@Controller
public class RegistroController {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private RegistroValidator registroValidator;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro"; // Nombre del archivo HTML de registro
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario, BindingResult bindingResult, Model model) {
        // Validar el formulario
        registroValidator.validate(usuario, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registro"; // Si hay errores, devuelve al formulario de registro
        }

        // Verificar si el email ya existe
        if (usuarioDao.emailExists(usuario.getEmail())) {
            bindingResult.rejectValue("email", "error.usuario", "Este email ya está registrado.");
            return "registro";
        }

        // Verificar si el teléfono ya existe
        if (usuarioDao.telefonoExists(usuario.getTelefono())) {
            bindingResult.rejectValue("telefono", "error.usuario", "Este teléfono ya está registrado.");
            return "registro";
        }

        // Hashear la contraseña
        String hashedPassword = new BCryptPasswordEncoder().encode(usuario.getPassword());
        usuario.setPassword(hashedPassword);

        usuario.setRol("USUARIO");
        // Guardar el usuario en la base de datos
        usuarioDao.addUsuario(usuario);

        model.addAttribute("email", usuario.getEmail());
        return "registroExito"; // Redirigir a la página de login
    }
}

