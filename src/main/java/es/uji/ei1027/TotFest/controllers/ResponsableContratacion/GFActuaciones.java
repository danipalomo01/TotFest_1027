package es.uji.ei1027.TotFest.controllers.ResponsableContratacion;

import es.uji.ei1027.TotFest.daos.*;
import es.uji.ei1027.TotFest.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @RequestMapping(value = "/{id}/{idartista}")
    public String addActuacionAContrato(HttpSession session, Model model, @PathVariable("id") int idContracte, @PathVariable("idartista") int idArtista) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }

        if (contracteArtistaDao.contratoCerrado(idContracte, contracteArtistaDao.getContracteArtista(idContracte).getNumActuacionsAny())) {
            List<Actuacio> actuacionesPrevias = contracteArtistaDao.getActuacionesContrato(idContracte);
            model.addAttribute("actuacionesPrevias", actuacionesPrevias);
            model.addAttribute("contratoCerrado", true);
        } else {
            model.addAttribute("contratoCerrado", false);
            model.addAttribute("actuacionesPrevias", new ArrayList<>());
        }


        ContracteArtista contracteArtista = contracteArtistaDao.getContracteArtista(idContracte);
        model.addAttribute("contrato", contracteArtista);

        LocalDate contractStartDate = contracteArtista.getDataInici().toLocalDate();
        LocalDate contractEndDate = contractStartDate.plusYears(1);

        // Filter festivals by date range intersection with the contract duration
        List<Festival> filteredFestivals = festivalDao.getFestivals().stream()
                .filter(festival ->
                        (((festival.getDataFi().toLocalDate().isBefore(contractEndDate) || festival.getDataFi().toLocalDate().isEqual(contractEndDate)) && (festival.getDataFi().toLocalDate().isAfter(contractStartDate) || festival.getDataFi().toLocalDate().isEqual(contractStartDate)))) ||
                                ((festival.getDataInici().toLocalDate().isBefore(contractEndDate) || festival.getDataInici().toLocalDate().isEqual(contractEndDate)) && (festival.getDataInici().toLocalDate().isAfter(contractStartDate) || festival.getDataInici().toLocalDate().isEqual(contractStartDate)))

                        ).collect(Collectors.toList());

        ContratoActuaciones contratoActuaciones = new ContratoActuaciones();
        contratoActuaciones.setContrato(contracteArtista);
        model.addAttribute("festivales", filteredFestivals);
        model.addAttribute("contratoActuaciones", contratoActuaciones);
        model.addAttribute("actuaciones", contratoActuaciones.getActuaciones());
        session.setAttribute("ultimoContrato", contratoActuaciones.getContrato().getIdContracte());
        session.setAttribute("ultimoContratoObject", contratoActuaciones.getContrato());
        session.setAttribute("nombreArtista", artistaDao.getArtistaGrup(contracteArtista.getIdArtista()).getNom());
        session.setAttribute("ultimoIdArtistaAModificar", contracteArtista.getIdArtista());

        if (session.getAttribute("solapamiento") == null) {
            session.setAttribute("solapamiento", "false");
        }
        return "responsablecontratacion/actuaciones/copiaAdd";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addActuacionAContrato(@ModelAttribute("contratoActuaciones") ContratoActuaciones contratoActuaciones, HttpSession session, BindingResult bindingResult, Model model) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }

        for (Actuacio actuacion: contratoActuaciones.getActuaciones()) {
            if (seSolapanActuaciones(actuacion)) {
                String idContrato = (String) session.getAttribute("ultimoContrato").toString();
                String idArtista = session.getAttribute("ultimoIdArtistaAModificar").toString();

                session.setAttribute("solapamiento", "No es posible añadir las actuaciones, ya que la actuación con fecha de inicio: " + actuacion.getHoraInici().toString() + " y hora de fin prevista: " + actuacion.getHoraFiPrevista().toString() + " se solapa con otra actuación para el artista ese mismo día.");
                return "redirect:/responsablecontratacion/actuaciones/" + idContrato + "/" + idArtista;
            }
            session.setAttribute("solapamiento", "false");
        }
        for (Actuacio actuacio : contratoActuaciones.getActuaciones()) {

            if (actuacio.getData() == null || actuacio.getHoraInici() == null || actuacio.getHoraFiPrevista() == null) {
                bindingResult.rejectValue("actuaciones", "error.contratoActuaciones", "Las fechas y horas no pueden estar vacías");
                return "responsablecontratacion/actuaciones/copiaAdd";
            }
            actuacio.setNomartista(session.getAttribute("nombreArtista").toString());
            actuacio.setIdContracte((int) session.getAttribute("ultimoContrato"));
            actuacio.setIdartista((int)session.getAttribute("ultimoIdArtistaAModificar"));
            actuacioDao.addActuacio(actuacio);
        }
        return "redirect:/responsablecontratacion/contracteartista/list";
    }

    public Boolean seSolapanActuaciones(Actuacio actuacion) {

        try {
            boolean seSolapan = actuacioDao.horasActuacionesSeSolapan(actuacion.getData(), actuacion.getHoraInici(), actuacion.getHoraFiPrevista(), actuacion.getIdFestival());
            return seSolapan;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }


}
