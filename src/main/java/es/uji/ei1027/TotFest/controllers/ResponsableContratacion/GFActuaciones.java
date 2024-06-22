package es.uji.ei1027.TotFest.controllers.ResponsableContratacion;

import es.uji.ei1027.TotFest.daos.*;
import es.uji.ei1027.TotFest.models.ContracteArtista;
import es.uji.ei1027.TotFest.models.ContratoActuaciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/responsablecontratacion/actuaciones")
public class GFActuaciones {

    private ContracteArtistaDao contracteArtistaDao;
    private ArtistaGrupDao artistaDao;
    private FestivalDao festivalDao;
    private ActuacioDao actuacioDao;
    private ActuacionsFestivalDao actuacionsFestivalDao;

    @Autowired
    public void setContracteArtistaDao(ContracteArtistaDao contracteArtistaDao, ArtistaGrupDao artistaDao, FestivalDao festivalDao, ActuacioDao actuacioDao, ActuacionsFestivalDao actuacionsFestivalDao) {
        this.contracteArtistaDao = contracteArtistaDao;
        this.artistaDao = artistaDao;
        this.festivalDao = festivalDao;
        this.actuacioDao = actuacioDao;
        this.actuacionsFestivalDao = actuacionsFestivalDao;
    }

    @RequestMapping(value = "/{id}/{idartista}", method = RequestMethod.GET)
    public String addActuacionAContrato(HttpSession session, Model model, @PathVariable("id") int idContracte, @PathVariable("idartista") int idArtista) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        ContracteArtista contracteArtista = contracteArtistaDao.getContracteArtista(idContracte);
        model.addAttribute("contrato", contracteArtista);

        ContratoActuaciones contratoActuaciones = new ContratoActuaciones();
        contratoActuaciones.setContrato(contracteArtista);
        model.addAttribute("festivales", festivalDao.getFestivals());
        model.addAttribute("contratoActuaciones", contratoActuaciones);
        model.addAttribute("actuaciones", contratoActuaciones.getActuaciones());
        model.addAttribute("nombreArtista", artistaDao.getArtistaGrup(contracteArtista.getIdArtista()).getNom());
        return "responsablecontratacion/actuaciones/copiaAdd";
    }

    @RequestMapping(value = "/actuaciones/add", method = RequestMethod.POST)
    public String addActuacionAContrato(HttpSession session,
                                        @ModelAttribute("contracteArtista") ContracteArtista contracteArtista,
                                        BindingResult bindingResult, Model model){
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        return "";
    }
}
