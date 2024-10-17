package es.uji.ei1027.TotFest.controllers.Admin;

import es.uji.ei1027.TotFest.daos.UsuarioDao;
import es.uji.ei1027.TotFest.models.ContracteArtista;
import es.uji.ei1027.TotFest.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioDao usuarioDao;

    @RequestMapping("/indexAdmin")
    public String listUsuarios(HttpSession session) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "admin/usuarios/indexAdmin";
    }

    @RequestMapping("/list")
    public String listUsuarios(HttpSession session,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "5") int size,
                               Model model) {

        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        if (session.getAttribute("size") != null && size == 1) {
            size = (int) session.getAttribute("size");
        } else {
            session.setAttribute("size", size);
        }

        try {
            List<Usuario> usuariosTotales = usuarioDao.getUsuarios();
            int offset = page * size;
            List<Usuario> usuarios = usuarioDao.getUsuarios(size, offset);
            int totalUsuarios = usuariosTotales.size();
            int totalPages = (int) Math.ceil((double) totalUsuarios / size);

            usuariosTotales.sort(Comparator.comparingInt(Usuario::getId));

            model.addAttribute("usuarios", usuarios);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("size", size);
            model.addAttribute("totalElems", totalUsuarios);

            return "admin/usuarios/list";
        } catch (Exception e) {
            model.addAttribute("mensajeError", "No se han podido listar los usuarios, inténtalo de nuevo más tarde o contacta con el soporte informático.");
            return "error.html";
        }
    }

    @RequestMapping("/add")
    public String addUsuario(HttpSession session, Model model) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);
        return "admin/usuarios/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddUsuario(
            @ModelAttribute("usuario") Usuario usuario,
            BindingResult bindingResult, HttpSession session, Model model) {

        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        // Validar que las contraseñas coincidan
        if (!usuario.getPassword().equals(usuario.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.usuario", "Las contraseñas no coinciden.");
            return "admin/usuarios/add";
        }

        // Validar si el email ya existe
        if (usuarioDao.emailExists(usuario.getEmail())) {
            bindingResult.rejectValue("email", "error.usuario", "Este email ya está registrado.");
            return "admin/usuarios/add";
        }

        // Hash de la contraseña
        String hashedPassword = new BCryptPasswordEncoder().encode(usuario.getPassword());
        usuario.setPassword(hashedPassword);

        usuarioDao.addUsuario(usuario);

        model.addAttribute("mensaje", "Usuario añadido correctamente.");
        return "/admin/usuarios/exito";
    }

    @RequestMapping(value = "/update/{idUsuario}", method = RequestMethod.GET)
    public String editUsuario(HttpSession session, Model model, @PathVariable int idUsuario) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioDao.getUsuarioById(idUsuario);

        // Si el rol del usuario no es editable por alguna condición
        if (usuario.getRol().equals("ADMIN")) {
            model.addAttribute("editable", false);
        } else {
            model.addAttribute("editable", true);
        }

        // Limpia la contraseña para que no se muestre en el formulario
        usuario.setPassword("");  // Esto evita mostrar el hash al usuario
        model.addAttribute("usuario", usuario);
        session.setAttribute("lastUsuarioUpdate", idUsuario);
        return "admin/usuarios/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(HttpSession session,
                                      @ModelAttribute("usuario") Usuario usuario,
                                      BindingResult bindingResult, Model model) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        // Validación si es necesaria
        if (bindingResult.hasErrors()) {
            return "admin/usuarios/update";
        }

        try {
            // Obtener el ID del usuario de la sesión
            usuario.setId(Integer.parseInt(session.getAttribute("lastUsuarioUpdate").toString()));

            // Si el campo de contraseña no está vacío, se hashea la nueva contraseña
            if (!usuario.getPassword().isEmpty()) {
                String hashedPassword = new BCryptPasswordEncoder().encode(usuario.getPassword());
                usuario.setPassword(hashedPassword);
            } else {
                // Si el campo está vacío, mantén la contraseña anterior (sin modificarla)
                String currentPassword = usuarioDao.getUsuarioById(usuario.getId()).getPassword();
                usuario.setPassword(currentPassword);
            }

            // Actualizar el usuario en la base de datos
            usuarioDao.updateUsuario(usuario);

            model.addAttribute("mensaje", "Usuario editado correctamente.");
            return "/admin/usuarios/exito";
        } catch (Exception e) {
            model.addAttribute("mensajeError", "No se ha podido editar el usuario, inténtalo de nuevo más tarde o contacta con el soporte informático.");
            return "error.html";
        }
    }



    @PostMapping("/edit")
    public String updateUsuario(@ModelAttribute("usuario") Usuario usuario, BindingResult result, HttpSession session) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            return "admin/usuarios/edit";
        }
        usuarioDao.updateUsuario(usuario);
        return "redirect:/admin/usuarios/list";
    }

    @RequestMapping("/delete/{id}")
    public String deleteUsuario(@PathVariable("id") int id, HttpSession session) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        usuarioDao.deleteUsuario(id);
        return "redirect:/admin/usuarios/list";
    }

    @RequestMapping(value = "/delete-selected", method = RequestMethod.POST)
    public String processDeleteSelected(HttpSession session, @RequestParam("selectedUsuarios") List<Integer> selectedUsuarios, Model model) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        try {
            // Verificar si no hay usuarios seleccionados
            if (selectedUsuarios == null || selectedUsuarios.isEmpty()) {
                model.addAttribute("mensajeError", "No se ha seleccionado ningún usuario para eliminar.");
                return "admin/usuarios/list"; // Redirigir al listado de usuarios
            }

            // Eliminar todos los usuarios seleccionados
            for (Integer idUsuario : selectedUsuarios) {
                usuarioDao.deleteUsuario(idUsuario); // Método para eliminar usuarios por ID
            }

            model.addAttribute("mensaje", "Usuario eliminado correctamente.");
            return "/admin/usuarios/exito";
        } catch (Exception e) {
            model.addAttribute("mensajeError", "No se han podido eliminar los usuarios, inténtalo de nuevo más tarde o contacta con el soporte informático.");
            return "error.html";
        }
    }


}
