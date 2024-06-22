package es.uji.ei1027.TotFest.controllers;

import es.uji.ei1027.TotFest.daos.*;
import es.uji.ei1027.TotFest.daos.ContracteArtistaDao;

import es.uji.ei1027.TotFest.models.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("gestorFestival")
public class GFFestivalController {

    private FestivalDao festivalDao;
    private ContracteArtistaDao contracteArtistaDao;
    private ArtistaGrupDao artistaGrupDao;
    private ActuacionsFestivalDao actuacionsFestivalDao;
    private ActuacioDao actuacioDao;
    private EntradaDao entradaDao;

    @Autowired
    public void setFestivalDao(FestivalDao festivalDao, ContracteArtistaDao contracteArtistaDao, ArtistaGrupDao artistaGrupDao, ActuacionsFestivalDao actuacionsFestivalDao, ActuacioDao actuacioDao, EntradaDao entradaDao) {
        this.festivalDao = festivalDao;
        this.contracteArtistaDao = contracteArtistaDao;
        this.artistaGrupDao = artistaGrupDao;
        this.actuacionsFestivalDao = actuacionsFestivalDao;
        this.actuacioDao = actuacioDao;
        this.entradaDao = entradaDao;
    }

    @RequestMapping("/list")
    public String listFestivals(HttpSession session, Model model) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        List<Festival> festivales = festivalDao.getFestivals();
        if (session.getAttribute("cif") != null) {
            model.addAttribute("cif", session.getAttribute("cif"));
        } else {
            model.addAttribute("cif", "noCif");
        }
        model.addAttribute("festivals", festivales);

        HashMap<Integer, List<String>> actuacionesFestivales = new HashMap<>();
        for (Festival festivale : festivales) {
            List<String> nombreArtistas = new ArrayList<>();
            List<Actuacio> acts = festivalDao.getActuacionesFestival(festivale.getIdFestival());
            for(Actuacio act: acts) {
                nombreArtistas.add(artistaGrupDao.getArtistaGrup(contracteArtistaDao.getContracteArtista(act.getIdContracte()).getIdArtista()).getNom());
            }
            actuacionesFestivales.put(festivale.getIdFestival(), nombreArtistas);
        }
        model.addAttribute("actuacionesFestivales", actuacionesFestivales);
        return "gestorFestival/list";
    }

    public List<String> getNombreArtistasDeFestival(int idFestival) {
        List<String> nombreArtistas = new ArrayList<>();
        List<Actuacio> acts = festivalDao.getActuacionesFestival(idFestival);
        for(Actuacio act: acts) {
            nombreArtistas.add(artistaGrupDao.getArtistaGrup(contracteArtistaDao.getContracteArtista(act.getIdContracte()).getIdArtista()).getNom());
        }
        return nombreArtistas;
    }

    @RequestMapping("/add")
    public String addFestival(HttpSession session, Model model) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        model.addAttribute("festival", new FestivalForm());
        return "gestorFestival/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(
            @ModelAttribute("festival") FestivalForm festivalForm,
            BindingResult bindingResult, HttpSession session) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        // Aquí puedes incluir la validación del Festival si es necesario

        Festival festival = new Festival(
                festivalForm.getIdFestival(),
                festivalForm.getCifPromotor(),
                festivalForm.getNom(),
                festivalForm.getAnyo(),
                festivalForm.getDataInici(),
                festivalForm.getDataFi(),
                festivalForm.getEstatFestival(),
                festivalForm.getDescripcio(),
                festivalForm.getCategoriaMusical(),
                festivalForm.getPressupostContractacio(),
                festivalForm.getAforamentMaxim(),
                festivalForm.getLocalitzacioDescriptiva(),
                festivalForm.getLocalitzacioGeografica(),
                festivalForm.getPublicEnfocat(),
                festivalForm.getRequisitMinimEdat(),
                festivalForm.getDataIniciPublicacio(),
                festivalForm.getDataIniciVenda(),
                festivalForm.getNumEntradasVendidas()
        );

        validarAddHtml(festival, bindingResult);

        if (bindingResult.hasErrors()) {
            return "gestorFestival/add";
        }


        try {

            festivalDao.addFestival(festival, session);

            EntradaTipus entradaTipusDia = new EntradaTipus(EntradaTipusEnum.dia, festival.getIdFestival(), BigDecimal.valueOf(festivalForm.getPrecioDia()), "", festivalForm.getAforamentMaxim()/10, 0, null, null );
            EntradaTipus entradaTipusCompleto = new EntradaTipus(EntradaTipusEnum.festivalComplet, festival.getIdFestival(), BigDecimal.valueOf(festivalForm.getPrecioFestivalCompleto()), "", festivalForm.getAforamentMaxim(), 0, null, null );

            entradaDao.addEntradaTipus(entradaTipusCompleto);
            entradaDao.addEntradaTipus(entradaTipusDia);
        } catch (DuplicateKeyException e) {
            // Manejar la excepción de clave duplicada si es necesario
        } catch (DataAccessException e) {
            // Manejar la excepción de acceso a datos si es necesario
        } catch (Exception e) {
            // Manejar otras excepciones no esperadas si es necesario
        }

        return "redirect:list";
    }

    public void validarAddHtml(Festival festival, BindingResult bindingResult) {
        validateFestival(festival, bindingResult);

        if(festival.getDataInici() != null && festival.getDataFi() != null) {
            LocalDate dataInici = festival.getDataInici().toLocalDate();
            LocalDate dataFi = festival.getDataFi().toLocalDate();

            if (dataFi.isBefore(dataInici)) {
                bindingResult.rejectValue("dataFi", "error.datafi", "La fecha de fin no puede ser anterior a la fecha de inicio.");
            }
        }

        if(festival.getDataIniciPublicacio() != null && festival.getDataIniciVenda() != null) {
            LocalDate dataIniciPublicacio = festival.getDataIniciPublicacio().toLocalDate();
            LocalDate dataIniciVenda = festival.getDataIniciVenda().toLocalDate();

            if (dataIniciVenda.isBefore(dataIniciPublicacio)) {
                bindingResult.rejectValue("dataIniciVenda", "error.dataIniciVenda", "La fecha de inicio de venta no puede ser anterior a la fecha de inicio de publicación del festival.");
            }
        }

        if(festival.getDataInici() != null && festival.getDataIniciPublicacio() != null) {
            LocalDate dataIniciPublicacio = festival.getDataIniciPublicacio().toLocalDate();
            LocalDate dataInici = festival.getDataInici().toLocalDate();

            if (dataInici.isBefore(dataIniciPublicacio)) {
                bindingResult.rejectValue("dataIniciPublicacio", "error.dataIniciPublicacio", "La fecha de inicio de publicación no puede ser posterior a la fecha de inicio del festival.");
            }
        }

        if(festival.getAforamentMaxim() <= 0) {
            bindingResult.rejectValue("aforamentMaxim", "error.aforamentMaxim","El aforo máximo no puede ser un número negativo.");
        }

        if (festival.getPressupostContractacio() != null && festival.getPressupostContractacio().compareTo(BigDecimal.ZERO) < 0) {
            bindingResult.rejectValue("pressupostContractacio", "error.pressupostContractacio","El presupuesto de contratación no puede ser un número negativo.");
        }

    }

    @RequestMapping(value = "/update/{idFestival}", method = RequestMethod.GET)
    public String editFestival(HttpSession session, Model model, @PathVariable int idFestival) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        model.addAttribute("festival", festivalDao.getFestival(idFestival));
        session.setAttribute("lastFestivalUpdate", idFestival);
        return "gestorFestival/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(HttpSession session,
            @ModelAttribute("festival") Festival festival,
            BindingResult bindingResult) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        // Puedes incluir la validación del Festival si es necesario
        if (bindingResult.hasErrors()) {
            return "gestorFestival/update";
        }
        festival.setIdFestival(Integer.parseInt(session.getAttribute("lastFestivalUpdate").toString()));
        festival.setCifPromotor(session.getAttribute("cif").toString());
        festivalDao.updateFestival(festival);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idFestival}")
    public String processDelete(HttpSession session, @PathVariable int idFestival) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        festivalDao.deleteFestival(idFestival);
        return "redirect:list";
    }

    public static void validateFestival(Festival festival, BindingResult bindingResult) {
        if (festival == null) {
            bindingResult.rejectValue("festival", "error.festival", "El festival no puede ser nulo.");
            return;
        }


        if (isEmptyOrNull(festival.getNom())) {
            bindingResult.rejectValue("nom", "error.nom", "El nombre del festival no puede estar vacío.");
        }

        if (festival.getAnyo() <= 0) {
            bindingResult.rejectValue("anyo", "error.anyo", "El año del festival debe ser mayor que cero.");
        }

        if (festival.getDataInici() == null) {
            bindingResult.rejectValue("dataInici", "error.dataInici", "La fecha de inicio del festival no puede estar vacía.");
        }

        if (festival.getDataFi() == null) {
            bindingResult.rejectValue("dataFi", "error.dataFi", "La fecha de fin del festival no puede estar vacía.");
        }

        if (isEmptyOrNull(festival.getDescripcio())) {
            bindingResult.rejectValue("descripcio", "error.descripcio", "La descripción del festival no puede estar vacía.");
        }

        if (isEmptyOrNull(festival.getCategoriaMusical())) {
            bindingResult.rejectValue("categoriaMusical", "error.categoriaMusical", "La categoría musical del festival no puede estar vacía.");
        }

        if (festival.getPressupostContractacio() == null || festival.getPressupostContractacio().compareTo(BigDecimal.ZERO) < 0) {
            bindingResult.rejectValue("pressupostContractacio", "error.pressupostContractacio", "El presupuesto de contratación del festival no puede ser nulo o negativo.");
        }

        if (festival.getAforamentMaxim() <= 0) {
            bindingResult.rejectValue("aforamentMaxim", "error.aforamentMaxim", "El aforo máximo del festival debe ser mayor que cero.");
        }

        if (isEmptyOrNull(festival.getLocalitzacioDescriptiva())) {
            bindingResult.rejectValue("localitzacioDescriptiva", "error.localitzacioDescriptiva", "La localización descriptiva del festival no puede estar vacía.");
        }

        if (isEmptyOrNull(festival.getLocalitzacioGeografica())) {
            bindingResult.rejectValue("localitzacioGeografica", "error.localitzacioGeografica", "La localización geográfica del festival no puede estar vacía.");
        }

        if (isEmptyOrNull(festival.getPublicEnfocat())) {
            bindingResult.rejectValue("publicEnfocat", "error.publicEnfocat", "El público objetivo del festival no puede estar vacío.");
        }

        if (festival.getRequisitMinimEdat() < 0) {
            bindingResult.rejectValue("requisitMinimEdat", "error.requisitMinimEdat", "El requisito mínimo de edad del festival no puede ser un número negativo.");
        }

        if (festival.getDataIniciPublicacio() == null) {
            bindingResult.rejectValue("dataIniciPublicacio", "error.dataIniciPublicacio", "La fecha de inicio de publicación del festival no puede estar vacía.");
        }

        if (festival.getDataIniciVenda() == null) {
            bindingResult.rejectValue("dataIniciVenda", "error.dataIniciVenda", "La fecha de inicio de venta del festival no puede estar vacía.");
        }
    }

    private static boolean isEmptyOrNull(String str) {
        return str == null || str.trim().isEmpty();
    }
}


