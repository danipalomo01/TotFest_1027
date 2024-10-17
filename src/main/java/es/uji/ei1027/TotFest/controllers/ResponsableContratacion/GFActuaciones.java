package es.uji.ei1027.TotFest.controllers.ResponsableContratacion;

import es.uji.ei1027.TotFest.daos.*;
import es.uji.ei1027.TotFest.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    @RequestMapping(value = "/list/{idcontrato}")
    public String listActuacionesContrato(HttpSession session, Model model,
                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "5") int size,
                                          @PathVariable("idcontrato") int idContracte) {

        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }
        try {

            if (session.getAttribute("size") != null && size == 1) {
                size = (int) session.getAttribute("size");
            } else {
                session.setAttribute("size", size);
            }

            List<Actuacio> actuacionesPrevias = contracteArtistaDao.getActuacionesContrato(idContracte);

            ContracteArtista contracteArtista = contracteArtistaDao.getContracteArtista(idContracte);

            LocalDate contractStartDate = contracteArtista.getDataInici().toLocalDate();
            LocalDate contractEndDate = contracteArtista.getDataFi().toLocalDate();

            List<Festival> filteredFestivals = festivalDao.getFestivals().stream()
                    .filter(festival ->
                            (((festival.getDataFi().toLocalDate().isAfter(contractEndDate) || festival.getDataFi().toLocalDate().isEqual(contractEndDate)) && (festival.getDataInici().toLocalDate().isBefore(contractEndDate) || festival.getDataInici().toLocalDate().isEqual(contractEndDate)))) ||
                                    ((festival.getDataInici().toLocalDate().isBefore(contractEndDate) || festival.getDataInici().toLocalDate().isEqual(contractEndDate)) && (festival.getDataFi().toLocalDate().isAfter(contractStartDate) || festival.getDataFi().toLocalDate().isEqual(contractStartDate)))

                    ).collect(Collectors.toList());


            int offset = page * size;
            List<Festival> festivales;
            int toIndex = Math.min(offset + size, filteredFestivals.size());

            if (offset <= filteredFestivals.size()) {
                festivales = filteredFestivals.subList(offset, toIndex);
            } else {
                festivales = Collections.emptyList();
            }

            actuacionesPrevias.sort(Comparator.comparingInt(Actuacio::getIdContracte));

            int totalfestivales = festivales.size();
            int totalPages = (int) Math.ceil((double) totalfestivales / size);

            int numActuacionesMaximasContrato = contracteArtista.getNumActuacionsAny();

            List<String> nombresFestivales = new ArrayList<>();

            for (Actuacio actuacio: actuacionesPrevias) {
                Festival festival = festivalDao.getFestival(actuacio.getIdFestival());
                nombresFestivales.add(festival.getNom());
            }

            ArtistaGrup artistaGrup = artistaDao.getArtistaGrup(contracteArtista.getIdArtista());
            model.addAttribute("nombresFestivales", nombresFestivales);
            model.addAttribute("numActuacionesMaximasContrato", numActuacionesMaximasContrato);
            model.addAttribute("festivales", festivales);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);

            model.addAttribute("actuaciones", actuacionesPrevias);
            model.addAttribute("idContrato", idContracte);
            model.addAttribute("nomartista", artistaGrup.getNom());
            model.addAttribute("totalElems", contracteArtistaDao.getActuacionesContrato(contracteArtista.getIdContracte()).size());

            return "responsablecontratacion/actuaciones/listActuacionesContrato";
        }
        catch (Exception e) {
            return "error.html";
        }
    }

    public void addModelValuesAddActuacion(Model model, ActuacioForm actuacioForm, int idContracte) {
        ContracteArtista contracteArtista = contracteArtistaDao.getContracteArtista(idContracte);
        actuacioForm.setIdContracte(idContracte);
        actuacioForm.setIdArtista(contracteArtista.getIdArtista());
        actuacioForm.setData(Date.valueOf(LocalDate.now()));
        actuacioForm.setHoraInici(java.sql.Time.valueOf(LocalTime.of(0, 0)));
        actuacioForm.setHoraFiPrevista(java.sql.Time.valueOf(LocalTime.of(0, 0)));
        actuacioForm.setPreuContracteActuacio(BigDecimal.valueOf(0));

        LocalDate contractStartDate = contracteArtista.getDataInici().toLocalDate();
        LocalDate contractEndDate = contracteArtista.getDataFi().toLocalDate();

        List<Festival> filteredFestivals = festivalDao.getFestivals().stream()
                .filter(festival ->
                        (((festival.getDataFi().toLocalDate().isAfter(contractEndDate) || festival.getDataFi().toLocalDate().isEqual(contractEndDate)) && (festival.getDataInici().toLocalDate().isBefore(contractEndDate) || festival.getDataInici().toLocalDate().isEqual(contractEndDate)))) ||
                                ((festival.getDataInici().toLocalDate().isBefore(contractEndDate) || festival.getDataInici().toLocalDate().isEqual(contractEndDate)) && (festival.getDataFi().toLocalDate().isAfter(contractStartDate) || festival.getDataFi().toLocalDate().isEqual(contractStartDate)))

                ).collect(Collectors.toList());

        model.addAttribute("actuacioForm", actuacioForm);
        model.addAttribute("festivales", filteredFestivals);
        model.addAttribute("inicioContrato", contracteArtista.getDataInici());
        model.addAttribute("finContrato", contracteArtista.getDataFi());
    }

    @RequestMapping(value = "/add/{idcontrato}")
    public String addActuacionContrato(HttpSession session, Model model, @PathVariable("idcontrato") int idContracte) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }
        try {
            ActuacioForm actuacioForm = new ActuacioForm();
            addModelValuesAddActuacion(model, actuacioForm, idContracte);

            ContracteArtista contracteArtista = contracteArtistaDao.getContracteArtista(idContracte);
            ArtistaGrup artistaGrup = artistaDao.getArtistaGrup(contracteArtista.getIdArtista());
            model.addAttribute("nombreArtista", artistaGrup.getNom());
            return "responsablecontratacion/actuaciones/addActuacionContrato";
        }
        catch (Exception e) {
            return "error.html";
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addActuacionContrato(@ModelAttribute("actuacioForm") ActuacioForm actuacioForm,
                                       HttpSession session, BindingResult bindingResult, Model model) {
        // Verifica la sesión
        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }
        try {
            validateActuacioForm(actuacioForm, bindingResult);

            if (bindingResult.hasErrors()) {
                addModelValuesAddActuacion(model, actuacioForm, actuacioForm.getIdContracte());
                return "responsablecontratacion/actuaciones/addActuacionContrato";
            }

            ArtistaGrup artistaGrup = artistaDao.getArtistaGrup(actuacioForm.getIdArtista());

            Actuacio actuacio = new Actuacio();
            actuacio.setNomartista(artistaGrup.getNom());
            actuacio.setIdFestival(actuacioForm.getIdFestival());
            actuacio.setIdartista(actuacioForm.getIdArtista());
            actuacio.setHoraFiPrevista(actuacioForm.getHoraFiPrevista());
            actuacio.setHoraInici(actuacioForm.getHoraInici());
            actuacio.setIdContracte(actuacioForm.getIdContracte());
            actuacio.setData(actuacioForm.getData());
            actuacio.setComentaris(actuacioForm.getComentaris());
            actuacio.setPreuContracteActuacio(actuacioForm.getPreuContracteActuacio());

            actuacioDao.addActuacio(actuacio);
            return "redirect:/responsablecontratacion/actuaciones/list/" + actuacio.getIdContracte();
        }
        catch (Exception e) {
            return "error.html";
        }
    }

    @RequestMapping(value = "/update/{id}")
    public String updateContrato(HttpSession session, Model model, @PathVariable("id") int idActuacion) {

        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }

        try {

            Actuacio actuacio = actuacioDao.getActuacio(idActuacion);

            ActuacioForm actuacioForm = new ActuacioForm();
            actuacioForm.setIdContracte(actuacio.getIdContracte());
            addModelValuesAddActuacion(model, actuacioForm, actuacioForm.getIdContracte());
            actuacioForm.setIdActuacio(actuacio.getIdActuacio());
            actuacioForm.setComentaris(actuacio.getComentaris());
            actuacioForm.setData(actuacio.getData());
            actuacioForm.setHoraInici(actuacio.getHoraInici());
            actuacioForm.setHoraFiPrevista(actuacio.getHoraFiPrevista());
            actuacioForm.setPreuContracteActuacio(actuacio.getPreuContracteActuacio());
            actuacioForm.setIdContracte(actuacio.getIdContracte());
            actuacioForm.setIdArtista(actuacio.getIdartista());
            actuacioForm.setIdFestival(actuacio.getIdFestival());

            model.addAttribute("nombreArtista", actuacio.getNomartista());
            return "responsablecontratacion/actuaciones/updateActuacionContrato";
        }
        catch (Exception e) {
            return "error.html";
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateActuacionContrato(@ModelAttribute("actuacioForm") ActuacioForm actuacioForm,
                                       HttpSession session, BindingResult bindingResult, Model model) {

        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }

        try {

            validateActuacioForm(actuacioForm, bindingResult);
            if (bindingResult.hasErrors()) {
                addModelValuesAddActuacion(model, actuacioForm, actuacioForm.getIdContracte());
                ContracteArtista contracteArtista = contracteArtistaDao.getContracteArtista(actuacioForm.getIdContracte());
                ArtistaGrup artistaGrup = artistaDao.getArtistaGrup(contracteArtista.getIdArtista());
                model.addAttribute("nombreArtista", artistaGrup.getNom());
                return "responsablecontratacion/actuaciones/updateActuacionContrato";
            }
            ArtistaGrup artistaGrup = artistaDao.getArtistaGrup(actuacioForm.getIdArtista());

            Actuacio actuacio = new Actuacio();
            actuacio.setIdActuacio(actuacioForm.getIdActuacio());
            actuacio.setNomartista(artistaGrup.getNom());
            actuacio.setIdFestival(actuacioForm.getIdFestival());
            actuacio.setIdartista(actuacioForm.getIdArtista());
            actuacio.setHoraFiPrevista(actuacioForm.getHoraFiPrevista());
            actuacio.setHoraInici(actuacioForm.getHoraInici());
            actuacio.setIdContracte(actuacioForm.getIdContracte());
            actuacio.setData(actuacioForm.getData());
            actuacio.setComentaris(actuacioForm.getComentaris());
            actuacio.setPreuContracteActuacio(actuacioForm.getPreuContracteActuacio());

            actuacioDao.updateActuacio(actuacio);
            return "redirect:/responsablecontratacion/actuaciones/list/" + actuacio.getIdContracte();
        }
        catch (Exception e) {
            return "error.html";
        }
    }

    @RequestMapping(value = "/delete-selected")
    public String processDeleteSelected(HttpSession session, @RequestParam(value = "selectedActuaciones") List<Integer> selectedActuaciones, @RequestParam(value = "idContrato") Integer idContrato, Model model) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        try {
            if (selectedActuaciones == null || selectedActuaciones.isEmpty()) {
                return "redirect:/responsablecontratacion/actuaciones/list/" + idContrato;
            }

            for (Integer selectedActuacion : selectedActuaciones) {
                actuacioDao.deleteActuacio(selectedActuacion);
            }
            session.setAttribute("mensajeConfirmacionFestival", " eliminado ");

            model.addAttribute("mensaje", "Se han eliminado las actuaciones correctamente");
            model.addAttribute("redireccion", "/responsablecontratacion/actuaciones/list/" + idContrato);
            return "/responsableContratacion/actuaciones/exito";
        } catch (Exception e){
            model.addAttribute("mensajeError", "No se han podido eliminar los contratos, inténtalo de nuevo más tarde o contacta con el soporte informático.");
            return "error.html";
        }
    }


    public boolean validateActuacioForm(ActuacioForm actuacioForm, BindingResult bindingResult) {

        if (actuacioForm.getHoraFiPrevista() != null && actuacioForm.getHoraInici() != null) {
            if (actuacioForm.getHoraFiPrevista().before(actuacioForm.getHoraInici())) {
                bindingResult.rejectValue("horaFiPrevista", "error.horaFiPrevista", "La hora de fin no puede ser anterior a la hora de inicio.");
            }
        }

        if (actuacioForm.getPreuContracteActuacio() != null) {
            if (actuacioForm.getPreuContracteActuacio().compareTo(BigDecimal.ZERO) < 0) {
                bindingResult.rejectValue("preuContracteActuacio", "error.preuContracteActuacio", "El precio no puede ser inferior a 0.");
            }
        }

        ContracteArtista contracteArtista = contracteArtistaDao.getContracteArtista(actuacioForm.getIdContracte());

        LocalDate dataActuacio = actuacioForm.getData().toLocalDate();
        LocalDate dataIniciContracte = contracteArtista.getDataInici().toLocalDate();
        LocalDate dataFiContracte = contracteArtista.getDataFi().toLocalDate();

        if (dataActuacio.isBefore(dataIniciContracte)) {
            bindingResult.rejectValue("data", "error.data", "La fecha de la actuación no puede ser anterior a la de inicio del contrato");
        }

        if (dataActuacio.isAfter(dataFiContracte)) {
            bindingResult.rejectValue("data", "error.data", "La fecha de la actuación no puede ser posterior a la de fin del contrato");
        }

        return bindingResult.hasErrors();
    }

    @RequestMapping(value = "/{idcontrato}/{idartista}")
    public String addActuacionAContrato(HttpSession session, Model model, @PathVariable("id") int idContracte, @PathVariable("idartista") int idArtista) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }
        try {
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
            LocalDate contractEndDate = contracteArtista.getDataFi().toLocalDate();

            // Filter festivals by date range intersection with the contract duration
            List<Festival> filteredFestivals = festivalDao.getFestivals().stream()
                    .filter(festival ->
                            (((festival.getDataFi().toLocalDate().isBefore(contractEndDate) || festival.getDataFi().toLocalDate().isEqual(contractEndDate)) && (festival.getDataFi().toLocalDate().isAfter(contractStartDate) || festival.getDataFi().toLocalDate().isEqual(contractStartDate)))) ||
                                    ((festival.getDataInici().toLocalDate().isBefore(contractEndDate) || festival.getDataInici().toLocalDate().isEqual(contractEndDate)) && (festival.getDataInici().toLocalDate().isAfter(contractStartDate) || festival.getDataInici().toLocalDate().isEqual(contractStartDate)))

                            ).collect(Collectors.toList());


            List<Actuacio> actuacionesNuevas = new ArrayList<>();
            for (int i=0; i< contracteArtista.getNumActuacionsAny(); i++) {
                Actuacio actuacio = new Actuacio();
                actuacio.setData(Date.valueOf(LocalDate.now()));
                actuacio.setHoraInici(java.sql.Time.valueOf(LocalTime.of(0, 0)));
                actuacio.setHoraFiPrevista(java.sql.Time.valueOf(LocalTime.of(0, 0)));
                actuacionesNuevas.add(actuacio);
            }
            ContratoActuaciones contratoActuaciones = new ContratoActuaciones();
            contratoActuaciones.setContrato(contracteArtista);
            contratoActuaciones.setActuaciones(actuacionesNuevas);
            model.addAttribute("actuacionesNuevas", actuacionesNuevas);
            model.addAttribute("festivales", filteredFestivals);
            model.addAttribute("contratoActuaciones", contratoActuaciones);
            model.addAttribute("actuaciones", contratoActuaciones.getActuaciones());
            session.setAttribute("ultimoContrato", contratoActuaciones.getContrato().getIdContracte());
            session.setAttribute("ultimoContratoObject", contratoActuaciones.getContrato());
            session.setAttribute("nombreArtista", artistaDao.getArtistaGrup(contracteArtista.getIdArtista()).getNom());
            session.setAttribute("ultimoIdArtistaAModificar", contracteArtista.getIdArtista());

            if(session.getAttribute("solapamiento") != null){
                model.addAttribute("solapamiento", session.getAttribute("solapamiento"));
            }

            return "responsablecontratacion/actuaciones/copiaAdd";
        } catch (Exception e){
            model.addAttribute("mensajeError", "No se ha podido obtener el contrato con sus actuaciones, inténtalo de nuevo más tarde o contacta con el soporte informático.");
            return "error.html";
        }
    }


    @RequestMapping(value = "/delete/{id}")
    public String addActuacionAContrato(HttpSession session, Model model, @PathVariable("id") int idActuacion) {

        if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
            return "redirect:/login";
        }
        try {
            Actuacio actuacio = actuacioDao.getActuacio(idActuacion);
            actuacioDao.deleteActuacio(idActuacion);

            model.addAttribute("mensaje", "Se ha eliminado la actuación correctamente");
            model.addAttribute("redireccion", "/responsablecontratacion/actuaciones/list/" + actuacio.getIdContracte());
            return "/responsablecontratacion/actuaciones/exito";

        }
        catch (Exception e) {
            return "error.html";
        }
    }

    //@RequestMapping(value = "/add", method = RequestMethod.POST)
    //public String addActuacionAContrato(@ModelAttribute("contratoActuaciones") ContratoActuaciones contratoActuaciones, HttpSession session, BindingResult bindingResult, Model model) {
    //    if (session == null || session.getAttribute("user") == null || session.getAttribute("idComercial") == null) {
    //        return "redirect:/login";
    //    }
//
    //    try {
    //        for (Actuacio actuacion : contratoActuaciones.getActuaciones()) {
    //            if (seSolapanActuaciones(actuacion)) {
    //                String idContrato = (String) session.getAttribute("ultimoContrato").toString();
    //                String idArtista = session.getAttribute("ultimoIdArtistaAModificar").toString();
//
    //                session.setAttribute("solapamiento", "No es posible añadir las actuaciones, ya que la actuación con fecha de inicio: " + actuacion.getHoraInici().toString() + " y hora de fin prevista: " + actuacion.getHoraFiPrevista().toString() + " se solapa con otra actuación para el artista ese mismo día.");
    //                return "redirect:/responsablecontratacion/actuaciones/" + idContrato + "/" + idArtista;
    //            }
    //        }
//
    //        for (int i = 0; i <  contratoActuaciones.getActuaciones().size(); i++) {
    //            for (int j = i + 1; j <  contratoActuaciones.getActuaciones().size(); j++) {
    //                if (actuacionesSeSolapan(contratoActuaciones.getActuaciones().get(i), contratoActuaciones.getActuaciones().get(j))) {
    //                    String idContrato = (String) session.getAttribute("ultimoContrato").toString();
    //                    String idArtista = session.getAttribute("ultimoIdArtistaAModificar").toString();
//
    //                    session.setAttribute("solapamiento", "No es posible añadir las actuaciones, ya que algunas actuaciones se solapan entre sí");
    //                    return "redirect:/responsablecontratacion/actuaciones/" + idContrato + "/" + idArtista;
    //                }
    //            }
    //        }
    //        for (Actuacio actuacio : contratoActuaciones.getActuaciones()) {
//
    //            if (actuacio.getData() == null || actuacio.getHoraInici() == null || actuacio.getHoraFiPrevista() == null) {
    //                bindingResult.rejectValue("actuaciones", "error.contratoActuaciones", "Las fechas y horas no pueden estar vacías");
    //                return "responsablecontratacion/actuaciones/copiaAdd";
    //            }
    //            actuacio.setNomartista(session.getAttribute("nombreArtista").toString());
    //            actuacio.setIdContracte((int) session.getAttribute("ultimoContrato"));
    //            actuacio.setIdartista((int) session.getAttribute("ultimoIdArtistaAModificar"));
    //            actuacioDao.addActuacio(actuacio);
    //        }
    //        //session.setAttribute("mensajeConfirmacionFestival", "n añadido las actuaciones ");
    //        return "redirect:/responsablecontratacion/contracteartista/list";
    //    } catch (Exception e){
    //        model.addAttribute("mensajeError", "No se han podido añadir las actuaciones al contrato, inténtalo de nuevo más tarde o contacta con el soporte informático.");
    //        return "error.html";
    //    }
    //}

    public Boolean seSolapanActuaciones(Actuacio actuacion) {

        try {
            boolean seSolapan = actuacioDao.horasActuacionesSeSolapan(actuacion.getData(), actuacion.getHoraInici(), actuacion.getHoraFiPrevista(), actuacion.getIdFestival());
            return seSolapan;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean actuacionesSeSolapan(Actuacio actuacion1, Actuacio actuacion2) {
        if (actuacion1.getData().equals(actuacion2.getData())) {
            return true;
        }

        return (actuacion1.getHoraInici().before(actuacion2.getHoraFiPrevista()) && actuacion1.getHoraFiPrevista().after(actuacion2.getHoraInici())) ||
                (actuacion2.getHoraInici().before(actuacion1.getHoraFiPrevista()) && actuacion2.getHoraFiPrevista().after(actuacion1.getHoraInici()));
    }


}
