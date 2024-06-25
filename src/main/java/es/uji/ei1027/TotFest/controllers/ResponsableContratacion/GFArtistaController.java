package es.uji.ei1027.TotFest.controllers.ResponsableContratacion;

import es.uji.ei1027.TotFest.daos.ArtistaGrupDao;
import es.uji.ei1027.TotFest.models.ArtistaGrup;
import es.uji.ei1027.TotFest.models.ContracteArtista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;



@Controller
@RequestMapping("responsablecontratacion/artista")
public class GFArtistaController {

    private ArtistaGrupDao artistaGrupDao;

    @Autowired
    public void setArtistaGrupDao(ArtistaGrupDao artistaGrupDao) {
        this.artistaGrupDao = artistaGrupDao;
    }


    @RequestMapping("/list")
    public String listArtistas(HttpSession session,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "1") int size,
                                         Model model) {


        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }

        if (session.getAttribute("size") != null && size == 1) {
            size = (int) session.getAttribute("size");
        } else {
            session.setAttribute("size", size);
        }

        if (session.getAttribute("mensajeConfirmacionArtista") != null && session.getAttribute("mensajeConfirmacionArtista") != "nada") {
            model.addAttribute("mensajeConfirmacionArtista", session.getAttribute("mensajeConfirmacionArtista"));
            session.setAttribute("mensajeConfirmacionArtista", "nada");
        } else {
            model.addAttribute("mensajeConfirmacionArtista", "nada");
        }

        List<ArtistaGrup> artistasTotales = artistaGrupDao.getArtistaGrups();
        List<ArtistaGrup> artistas = artistaGrupDao.getArtistaGrups(page, size);

        artistas.sort(Comparator.comparingInt(ArtistaGrup::getIdArtista));

        int totalArtistas = artistasTotales.size();
        int totalPages = (int) Math.ceil((double) totalArtistas / size);

        model.addAttribute("artistas", artistas);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);

        return "responsableContratacion/gestionArtistas/list";
    }

    @RequestMapping("/add")
    public String addArtista(HttpSession session, Model model) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }
        model.addAttribute("artista", new ArtistaGrup());
        return "responsableContratacion/gestionArtistas/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(
            @ModelAttribute("artista") ArtistaGrup artista,
            BindingResult bindingResult, HttpSession session) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }
        validarAddArtista(artista, bindingResult);
        if (bindingResult.hasErrors()) {
            return "responsablecontratacion/gestionArtistas/add";
        }

        try {
            artistaGrupDao.addArtistaGrup(artista);
            session.setAttribute("mensajeConfirmacionArtista", " añadido ");
        } catch (DuplicateKeyException e) {
            // Manejar la excepción de clave duplicada si es necesario
        } catch (DataAccessException e) {
            // Manejar la excepción de acceso a datos si es necesario
        } catch (Exception e) {
            // Manejar otras excepciones no esperadas si es necesario
        }

        return "redirect:list";
    }

    @RequestMapping(value = "/update/{idArtista}", method = RequestMethod.GET)
    public String editArtista(HttpSession session, Model model, @PathVariable int idArtista) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }
        model.addAttribute("artista", artistaGrupDao.getArtistaGrup(idArtista));
        session.setAttribute("lastArtistaUpdate", idArtista);
        return "responsablecontratacion/gestionArtistas/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(HttpSession session,
                                      @ModelAttribute("artista") ArtistaGrup artista,
                                      BindingResult bindingResult) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }
        validarAddArtista(artista, bindingResult);

        if (bindingResult.hasErrors()) {
            return "responsablecontratacion/gestionArtistas/update";
        }
        artista.setIdArtista(Integer.parseInt(session.getAttribute("lastArtistaUpdate").toString()));
        artistaGrupDao.updateArtistaGrup(artista);
        session.setAttribute("mensajeConfirmacionArtista", " editado ");
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idArtista}")
    public String processDelete(HttpSession session, @PathVariable int idArtista) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }
        artistaGrupDao.deleteArtistaGrup(idArtista);
        session.setAttribute("mensajeConfirmacionArtista", " eliminado ");
        return "redirect:/responsablecontratacion/artista/list";
    }

    private void validarAddArtista(ArtistaGrup artista, BindingResult bindingResult) {
        if (artista.getNom() == null || artista.getNom().trim().isEmpty()) {
            bindingResult.rejectValue("nom", "error.nom", "El nombre no puede estar vacío.");
        } else if (artista.getNom().length() > 250) {
            bindingResult.rejectValue("nom", "error.nom", "El nombre no puede exceder 250 caracteres.");
        }

        if (artista.getTipusMusica() == null || artista.getTipusMusica().trim().isEmpty()) {
            bindingResult.rejectValue("tipusMusica", "error.tipusMusica", "El tipo de música no puede estar vacío.");
        } else if (artista.getTipusMusica().length() > 100) {
            bindingResult.rejectValue("tipusMusica", "error.tipusMusica", "El tipo de música no puede exceder 100 caracteres.");
        }

        if (artista.getDescripcio() == null || artista.getDescripcio().trim().isEmpty()) {
            bindingResult.rejectValue("descripcio", "error.descripcio", "La descripción no puede estar vacía.");
        } else if (artista.getDescripcio().length() > 250) {
            bindingResult.rejectValue("descripcio", "error.descripcio", "La descripción no puede exceder 250 caracteres.");
        }

        if (artista.getCachetActual() == null || artista.getCachetActual().compareTo(BigDecimal.ZERO) <= 0) {
            bindingResult.rejectValue("cachetActual", "error.cachetActual", "El cachet actual debe ser mayor que 0.");
        }
    }
}

