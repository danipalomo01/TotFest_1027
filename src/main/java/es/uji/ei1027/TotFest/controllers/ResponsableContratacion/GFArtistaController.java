package es.uji.ei1027.TotFest.controllers.ResponsableContratacion;

import es.uji.ei1027.TotFest.daos.ArtistaGrupDao;
import es.uji.ei1027.TotFest.daos.ContracteArtistaDao;
import es.uji.ei1027.TotFest.daos.FestivalDao;
import es.uji.ei1027.TotFest.models.ArtistaGrup;
import es.uji.ei1027.TotFest.models.ContracteArtista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;



@Controller
@RequestMapping("responsablecontratacion/artista")
public class GFArtistaController {

    private ArtistaGrupDao artistaDao;
    private FestivalDao festivalDao;
    private ContracteArtistaDao contracteArtistaDao;

    @Autowired
    public void setArtistaDao(ArtistaGrupDao artistaDao) {
        this.artistaDao = artistaDao;
    }

    @Autowired
    public void setFestivalDao(FestivalDao festivalDao) {
        this.festivalDao = festivalDao;
    }

    @Autowired
    public void setContracteArtistaDao(ContracteArtistaDao contracteArtistaDao) {
        this.contracteArtistaDao = contracteArtistaDao;
    }


    @RequestMapping("/list")
    public String listArtistas(HttpSession session,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "5") int size,
                                         Model model) {


        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }

        try {
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

            List<ArtistaGrup> artistasTotales = artistaDao.getArtistaGrups();
            List<ArtistaGrup> artistas = artistaDao.getArtistaGrups(page, size);

            artistas.sort(Comparator.comparingInt(ArtistaGrup::getIdArtista));

            int totalArtistas = artistasTotales.size();
            int totalPages = (int) Math.ceil((double) totalArtistas / size);

            model.addAttribute("artistas", artistas);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("totalElems", artistaDao.getArtistaGrups().size());

            return "responsableContratacion/gestionArtistas/list";
        }  catch (Exception e){
            model.addAttribute("mensajeError", "No se han podido listar los artistas, inténtalo de nuevo más tarde o contacta con el soporte informático.");
            return "error.html";
        }
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
            BindingResult bindingResult, HttpSession session, Model model) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }

        try {
            validarAddArtista(artista, bindingResult);
            if (bindingResult.hasErrors()) {
                return "responsablecontratacion/gestionArtistas/add";
            }


            artistaDao.addArtistaGrup(artista);
            model.addAttribute("mensajeConfirmacionArtista", "Se ha añadido correctamente el artista");


            return "responsableContratacion/gestionArtistas/exito";
        } catch (Exception e){
            model.addAttribute("mensajeError", "No se ha podido añadir el artista, inténtalo de nuevo más tarde o contacta con el soporte informático.");
            return "error.html";
        }
    }

    @RequestMapping("/list/{idArtista}")
    public String listArtistas(HttpSession session,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "5") int size,
                               @PathVariable int idArtista,
                               Model model) {

        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }

        List<ContracteArtista> contractesArtista = contracteArtistaDao.getContractesArtista(page, size, idArtista);

        int totalContracts = contracteArtistaDao.getContractesArtistes().size();
        int totalPages = (int) Math.ceil((double) totalContracts / size);

        contractesArtista.sort(Comparator.comparingInt(ContracteArtista::getIdContracte));

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("contratos", contractesArtista);
        model.addAttribute("artistas", artistaDao.getArtistaGrups());
        model.addAttribute("festivales", festivalDao.getFestivals());
        model.addAttribute("nomArtista", artistaDao.getArtistaGrup(idArtista).getNom());
        model.addAttribute("totalElems", contractesArtista.size());
        model.addAttribute("idArtista", idArtista);

        return "responsablecontratacion/gestionArtistas/listContratosArtista";
    }

    @RequestMapping(value = "/update/{idArtista}", method = RequestMethod.GET)
    public String editArtista(HttpSession session, Model model, @PathVariable int idArtista) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }
        try {
            model.addAttribute("artista", artistaDao.getArtistaGrup(idArtista));
            session.setAttribute("lastArtistaUpdate", idArtista);
            return "responsablecontratacion/gestionArtistas/update";
        }  catch (Exception e){
            model.addAttribute("mensajeError", "No se ha podido obtener el artista, inténtalo de nuevo más tarde o contacta con el soporte informático.");
            return "error.html";
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(HttpSession session,
                                      @ModelAttribute("artista") ArtistaGrup artista,
                                      BindingResult bindingResult, Model model) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }
        try {
            validarAddArtista(artista, bindingResult);

            if (bindingResult.hasErrors()) {
                return "responsablecontratacion/gestionArtistas/update";
            }
            artista.setIdArtista(Integer.parseInt(session.getAttribute("lastArtistaUpdate").toString()));
            artistaDao.updateArtistaGrup(artista);
            model.addAttribute("mensajeConfirmacionArtista", "Se ha editado correctamente el artista");
            return "responsableContratacion/gestionArtistas/exito";
        } catch (Exception e){
            model.addAttribute("mensajeError", "No se ha podido editar el artista, inténtalo de nuevo más tarde o contacta con el soporte informático.");
            return "error.html";
        }
    }

    @RequestMapping(value = "/delete/{idArtista}")
    public String processDelete(HttpSession session, @PathVariable int idArtista, Model model) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }
        try {
            artistaDao.deleteArtistaGrup(idArtista);
            model.addAttribute("mensajeConfirmacionArtista", "Se ha eliminado correctamente el artista");
            return "responsableContratacion/gestionArtistas/exito";
        } catch (Exception e){
            model.addAttribute("mensajeError", "No se ha podido eliminar el artista, inténtalo de nuevo más tarde o contacta con el soporte informático.");
            return "error.html";
        }
    }

    @RequestMapping(value = "/delete-selected")
    public String processDeleteSelected(HttpSession session, @RequestParam(value = "selectedArtistas", required = false) List<Integer> selectedArtistas, Model model) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        try {
            if (selectedArtistas == null || selectedArtistas.isEmpty()) {
                return "redirect:/responsablecontratacion/artista/list";
            }

            for (Integer selectedArtista : selectedArtistas) {
                artistaDao.deleteArtistaGrup(selectedArtista);
            }

            model.addAttribute("mensajeConfirmacionArtista", "Se han eliminado correctamente los artistas");
            return "responsableContratacion/gestionArtistas/exito";
        } catch (Exception e){
            model.addAttribute("mensajeError", "No se han podido eliminar los contratos, inténtalo de nuevo más tarde o contacta con el soporte informático.");
            return "error.html";
        }
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

