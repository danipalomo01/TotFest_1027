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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
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
    private PromotorDao promotorDao;

    @Autowired
    public void setFestivalDao(FestivalDao festivalDao, ContracteArtistaDao contracteArtistaDao, ArtistaGrupDao artistaGrupDao, ActuacionsFestivalDao actuacionsFestivalDao, ActuacioDao actuacioDao, EntradaDao entradaDao, PromotorDao promotorDao) {
        this.festivalDao = festivalDao;
        this.contracteArtistaDao = contracteArtistaDao;
        this.artistaGrupDao = artistaGrupDao;
        this.actuacionsFestivalDao = actuacionsFestivalDao;
        this.actuacioDao = actuacioDao;
        this.entradaDao = entradaDao;
        this.promotorDao = promotorDao;
    }

    @RequestMapping("/list")
    public String listFestivals(HttpSession session,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "5") int size,
                               Model model) {



        if (session == null || session.getAttribute("user") == null || session.getAttribute("cif") == null) {
            return "redirect:/login";
        }

        if (session.getAttribute("mensajeConfirmacionFestival") != null && session.getAttribute("mensajeConfirmacionFestival") != "nada") {
            model.addAttribute("mensajeConfirmacionFestival", session.getAttribute("mensajeConfirmacionFestival"));
            session.setAttribute("mensajeConfirmacionFestival", "nada");
        } else {
            model.addAttribute("mensajeConfirmacionFestival", "nada");
        }

        try {
            List<Festival> festivalesTotales = festivalDao.getFestivals();
            int offset = page * size;
            List<Festival> festivales = festivalDao.getFestivals(size, offset);
            int totalfestivales = festivalesTotales.size();
            int totalPages = (int) Math.ceil((double) totalfestivales / size);

            festivales.sort(Comparator.comparingInt(Festival::getIdFestival));

            List<Integer> numEntradasVendidas = new ArrayList<>();

            for(Festival festival: festivales) {
                int numEntradas = entradaDao.getNumTotalEntradasFestival(festival.getIdFestival());
                numEntradasVendidas.add(numEntradas);
            }
            model.addAttribute("numEntradasVendidas", numEntradasVendidas);
            model.addAttribute("festivales", festivales);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("totalElems", festivalDao.getFestivals().size());

            return "gestorFestival/list";
        } catch (Exception e){
            model.addAttribute("mensajeError", "No se han podido listar los festivales, inténtalo de nuevo más tarde o contacta con el soporte informático.");
            return "error.html";
        }

    }

    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
    public String listFestivalsPromotor(HttpSession session,
                                        @PathVariable int id,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "5") int size,
                                Model model) {

        if (session == null || session.getAttribute("user") == null || session.getAttribute("cif") == null) {
            return "redirect:/login";
        }

        if (session.getAttribute("mensajeConfirmacionFestival") != null && session.getAttribute("mensajeConfirmacionFestival") != "nada") {
            model.addAttribute("mensajeConfirmacionFestival", session.getAttribute("mensajeConfirmacionFestival"));
            session.setAttribute("mensajeConfirmacionFestival", "nada");
        } else {
            model.addAttribute("mensajeConfirmacionFestival", "nada");
        }

        try {
            List<Festival> festivalesTotales = festivalDao.getFestivalsPromotor(id);
            int offset = page * size;
            List<Festival> festivales = festivalDao.getFestivalsPromotor(id, size, offset);
            int totalfestivales = festivalesTotales.size();
            int totalPages = (int) Math.ceil((double) totalfestivales / size);

            festivales.sort(Comparator.comparingInt(Festival::getIdFestival));

            List<Integer> numEntradasVendidas = new ArrayList<>();

            for(Festival festival: festivales) {
                int numEntradas = entradaDao.getNumTotalEntradasFestival(festival.getIdFestival());
                numEntradasVendidas.add(numEntradas);
            }
            model.addAttribute("numEntradasVendidas", numEntradasVendidas);
            model.addAttribute("festivales", festivales);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("totalElems", festivalDao.getFestivals().size());
            model.addAttribute("nombrePromotor", promotorDao.getPromotor(id).getNomOrganisme());
            model.addAttribute("idPromotor", promotorDao.getPromotor(id).getId());
            return "gestorFestival/promotor/festivalespromotor";
        } catch (Exception e){
            model.addAttribute("mensajeError", "No se han podido listar los festivales, inténtalo de nuevo más tarde o contacta con el soporte informático.");
            return "error.html";
        }

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
        if (session == null || session.getAttribute("user") == null || session.getAttribute("cif") == null) {
            return "redirect:/login";
        }
        FestivalForm festivalForm = new FestivalForm();
        festivalForm.setDataInici(Date.valueOf(LocalDate.now()));
        festivalForm.setDataFi(Date.valueOf(LocalDate.now()));
        festivalForm.setDataIniciPublicacio(Date.valueOf(LocalDate.now()));
        festivalForm.setDataIniciVenda(Date.valueOf(LocalDate.now()));
        festivalForm.setAnyo(2024);
        model.addAttribute("festival", festivalForm);

        List<Promotor> promotores = promotorDao.getPromotores();
        model.addAttribute("promotores", promotores);
        model.addAttribute("idPromotor", null);

        return "gestorFestival/add";
    }

    @RequestMapping("/add/{idPromotor}")
    public String addFestivalPromotor(@PathVariable int idPromotor, HttpSession session, Model model) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("cif") == null) {
            return "redirect:/login";
        }
        FestivalForm festivalForm = new FestivalForm();
        festivalForm.setDataInici(Date.valueOf(LocalDate.now()));
        festivalForm.setDataFi(Date.valueOf(LocalDate.now()));
        festivalForm.setDataIniciPublicacio(Date.valueOf(LocalDate.now()));
        festivalForm.setDataIniciVenda(Date.valueOf(LocalDate.now()));
        festivalForm.setAnyo(2024);
        festivalForm.setIdPromotor(idPromotor);
        model.addAttribute("festival", festivalForm);

        model.addAttribute("idPromotor", idPromotor);

        return "gestorFestival/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(
            @ModelAttribute("festival") FestivalForm festivalForm,
            BindingResult bindingResult, HttpSession session ,Model model) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("cif") == null) {
            return "redirect:/login";
        }
        // Aquí puedes incluir la validación del Festival si es necesario

        validarAddHtml(festivalForm, bindingResult);

        if (bindingResult.hasErrors()) {
            if (festivalForm.getIdPromotor() != 0) {
                model.addAttribute("festivalForm", festivalForm);
                model.addAttribute("idPromotor", festivalForm.getIdPromotor());

            }
            return "gestorFestival/add";
        }

        Festival festival = new Festival(
                festivalForm.getIdFestival(),
                festivalForm.getIdPromotor(),
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
                festivalForm.getDataIniciVenda()
        );

        try {
            festivalDao.addFestival(festival, festivalForm.getIdPromotor(), session);

            EntradaTipus entradaTipusDia = new EntradaTipus(EntradaTipusEnum.DIA, festival.getIdFestival(), BigDecimal.valueOf(festivalForm.getPrecioDia()), festivalForm.getDescripcionDia(), BigDecimal.valueOf(festivalForm.getPorcentajeDia()));
            EntradaTipus entradaTipusCompleto = new EntradaTipus(EntradaTipusEnum.FESTIVAL_COMPLETO, festival.getIdFestival(), BigDecimal.valueOf(festivalForm.getPrecioFestivalCompleto()), festivalForm.getDescripcionCompleto(), BigDecimal.valueOf(festivalForm.getPorcentajeCompleto()));

            entradaDao.addEntradaTipus(entradaTipusCompleto);
            entradaDao.addEntradaTipus(entradaTipusDia);
            session.setAttribute("mensajeConfirmacionFestival", " añadido ");

            model.addAttribute("mensajeConfirmacionArtista", "Se ha añadido correctamente el festival");
            return "gestorFestival/exito";
        } catch (Exception e) {
            model.addAttribute("mensajeError", "No se ha podido añadir el festival a la base de datos");
            return "error.html";
        }
    }

    public void validarAddHtml(FestivalForm festival, BindingResult bindingResult) {
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

        if(festival.getDataFi() != null && festival.getDataIniciPublicacio() != null) {
            LocalDate dataIniciPublicacio = festival.getDataIniciPublicacio().toLocalDate();
            LocalDate dataFi = festival.getDataFi().toLocalDate();

            if (dataFi.isBefore(dataIniciPublicacio)) {
                bindingResult.rejectValue("dataIniciVenda", "error.dataIniciPublicacio", "La fecha de inicio de venta no puede ser posterior a la fecha de fin del festival.");
            }
        }

        if (festival.getPorcentajeCompleto() < 0) {
            bindingResult.rejectValue("porcentajeCompleto", "error.porcentajeCompleto","El porcentaje no puede ser un número negativo.");
        }

        if (festival.getRequisitMinimEdat() < 0) {
            bindingResult.rejectValue("requisitMinimEdat", "error.requisitMinimEdat","La edad no puede ser un número negativo.");
        }

        if (festival.getPorcentajeDia() < 0) {
            bindingResult.rejectValue("porcentajeDia", "error.porcentajeDia","El porcentaje no puede ser un número negativo.");
        }

        if (festival.getPorcentajeCompleto() > 100) {
            bindingResult.rejectValue("porcentajeCompleto", "error.porcentajeCompleto","El porcentaje no puede ser mayor que 100.");
        }

        if (festival.getPorcentajeDia() > 100) {
            bindingResult.rejectValue("porcentajeDia", "error.porcentajeDia","El porcentaje no puede ser mayor que 100.");
        }

        if (festival.getPressupostContractacio() != null && festival.getPressupostContractacio().compareTo(BigDecimal.ZERO) < 0) {
            bindingResult.rejectValue("pressupostContractacio", "error.pressupostContractacio","El presupuesto de contratación no puede ser un número negativo.");
        }

    }

    @RequestMapping(value = "/update/{idFestival}", method = RequestMethod.GET)
    public String editFestival(HttpSession session, Model model, @PathVariable int idFestival) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("cif") == null) {
            return "redirect:/login";
        }
        Festival festival = festivalDao.getFestival(idFestival);

        if (festival.getDataIniciVenda().toLocalDate().isEqual(LocalDate.now()) || festival.getDataIniciVenda().toLocalDate().isBefore(LocalDate.now())) {
            model.addAttribute("editable", false);
        } else {
            model.addAttribute("editable", true);
        }
        FestivalForm festivalForm = new FestivalForm();

        festivalForm.setAnyo(festival.getAnyo());
        festivalForm.setIdFestival(festival.getIdFestival());
        festivalForm.setNom(festival.getNom());
        festivalForm.setEstatFestival(festival.getEstatFestival());
        festivalForm.setIdPromotor(festival.getIdPromotor());
        festivalForm.setDataInici(festival.getDataInici());
        festivalForm.setDataFi(festival.getDataFi());
        festivalForm.setAforamentMaxim(festival.getAforamentMaxim());
        festivalForm.setCategoriaMusical(festival.getCategoriaMusical());
        festivalForm.setRequisitMinimEdat(festival.getRequisitMinimEdat());
        festivalForm.setPublicEnfocat(festival.getPublicEnfocat());
        festivalForm.setPressupostContractacio(festival.getPressupostContractacio());
        festivalForm.setLocalitzacioGeografica(festival.getLocalitzacioGeografica());
        festivalForm.setLocalitzacioDescriptiva(festival.getLocalitzacioDescriptiva());
        festivalForm.setDescripcio(festival.getDescripcio());
        festivalForm.setDataIniciVenda(festival.getDataIniciVenda());
        festivalForm.setDataIniciPublicacio(festival.getDataIniciPublicacio());

        EntradaTipus entradaTipusDia = entradaDao.getEntradaTipus(idFestival, 1);
        EntradaTipus entradaTipusCompleto = entradaDao.getEntradaTipus(idFestival, 2);

        festivalForm.setPorcentajeDia(entradaTipusDia.getPercentatgeMaximAforament().floatValue());
        festivalForm.setPorcentajeCompleto(entradaTipusCompleto.getPercentatgeMaximAforament().floatValue());
        festivalForm.setDescripcionDia(entradaTipusDia.getDescripcio());
        festivalForm.setDescripcionCompleto(entradaTipusCompleto.getDescripcio());
        festivalForm.setPrecioDia(entradaTipusDia.getPreu().floatValue());
        festivalForm.setPrecioFestivalCompleto(entradaTipusCompleto.getPreu().floatValue());

        model.addAttribute("festival", festivalForm);

        session.setAttribute("lastFestivalUpdate", idFestival);

        List<Promotor> promotores = promotorDao.getPromotores();
        model.addAttribute("promotores", promotores);
        return "gestorFestival/update";

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(HttpSession session,
            @ModelAttribute("festival") FestivalForm festival,
            BindingResult bindingResult, Model model) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("cif") == null) {
            return "redirect:/login";
        }
        // Puedes incluir la validación del Festival si es necesario
        if (bindingResult.hasErrors()) {
            return "gestorFestival/update";
        }
        try {

            Festival festivalNuevo = new Festival();

            festivalNuevo.setAnyo(festival.getAnyo());
            festivalNuevo.setIdFestival(festival.getIdFestival());
            festivalNuevo.setNom(festival.getNom());
            festivalNuevo.setEstatFestival(festival.getEstatFestival());
            festivalNuevo.setIdPromotor(festival.getIdPromotor());
            festivalNuevo.setDataInici(festival.getDataInici());
            festivalNuevo.setDataFi(festival.getDataFi());
            festivalNuevo.setAforamentMaxim(festival.getAforamentMaxim());
            festivalNuevo.setCategoriaMusical(festival.getCategoriaMusical());
            festivalNuevo.setRequisitMinimEdat(festival.getRequisitMinimEdat());
            festivalNuevo.setPublicEnfocat(festival.getPublicEnfocat());
            festivalNuevo.setPressupostContractacio(festival.getPressupostContractacio());
            festivalNuevo.setLocalitzacioGeografica(festival.getLocalitzacioGeografica());
            festivalNuevo.setLocalitzacioDescriptiva(festival.getLocalitzacioDescriptiva());
            festivalNuevo.setDescripcio(festival.getDescripcio());
            festivalNuevo.setDataIniciVenda(festival.getDataIniciVenda());
            festivalNuevo.setDataIniciPublicacio(festival.getDataIniciPublicacio());

            EntradaTipus entradaTipusDia = new EntradaTipus();
            EntradaTipus entradaTipusCompleto = new EntradaTipus();

            entradaTipusDia.setPercentatgeMaximAforament(BigDecimal.valueOf(festival.getPorcentajeDia()));
            entradaTipusCompleto.setPercentatgeMaximAforament(BigDecimal.valueOf(festival.getPorcentajeCompleto()));

            entradaTipusDia.setDescripcio(festival.getDescripcionDia());
            entradaTipusCompleto.setDescripcio(festival.getDescripcionCompleto());

            entradaTipusDia.setPreu(BigDecimal.valueOf(festival.getPrecioDia()));
            entradaTipusCompleto.setPreu(BigDecimal.valueOf(festival.getPrecioFestivalCompleto()));

            entradaTipusDia.setEntradaTipus(EntradaTipusEnum.DIA);
            entradaTipusCompleto.setEntradaTipus(EntradaTipusEnum.FESTIVAL_COMPLETO);

            festivalDao.updateFestival(festivalNuevo);

            entradaDao.updateEntradaTipus(entradaTipusDia);
            entradaDao.updateEntradaTipus(entradaTipusCompleto);

            session.setAttribute("mensajeConfirmacionFestival", " editado ");

            model.addAttribute("mensajeConfirmacionArtista", "Se ha editado correctamente el festival");
            return "gestorFestival/exito";
        } catch (Exception e){
            model.addAttribute("mensajeError", "No se ha podido editar el festival, inténtalo de nuevo más tarde o contacta con el soporte informático.");
            return "error.html";
        }
    }

    @RequestMapping(value = "/delete/{idFestival}")
    public String processDelete(HttpSession session, @PathVariable int idFestival, Model model) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("cif") == null) {
            return "redirect:/login";
        }
        try {
            festivalDao.deleteFestival(idFestival);
            session.setAttribute("mensajeConfirmacionFestival", " eliminado ");

            model.addAttribute("mensajeConfirmacionArtista", "Se ha eliminado correctamente el festival");
            return "gestorFestival/exito";
        } catch (Exception e){
            model.addAttribute("mensajeError", "No se ha podido eliminar el festival, inténtalo de nuevo más tarde o contacta con el soporte informático.");
            return "error.html";
        }
    }

    @RequestMapping(value = "/delete-selected")
    public String processDeleteSelected(HttpSession session, @RequestParam(value = "selectedFestivals", required = false) List<Integer> selectedFestivals , Model model) {
        if (session == null || session.getAttribute("user") == null || session.getAttribute("cif") == null) {
            return "redirect:/login";
        }

        try {

            if (selectedFestivals == null || selectedFestivals.isEmpty()) {
                return "redirect:/gestorFestival/list"; // Redirige a la misma página con el mensaje
            }

            for (Integer selectedFestival : selectedFestivals) {
                festivalDao.deleteFestival(selectedFestival);
            }
            session.setAttribute("mensajeConfirmacionFestival", " eliminado ");

            model.addAttribute("mensajeConfirmacionArtista", "Se han eliminado correctamente los festivales seleccionados");
            return "gestorFestival/exito";
        } catch (Exception e){
            model.addAttribute("mensajeError", "No se han podido eliminar los festivales, inténtalo de nuevo más tarde o contacta con el soporte informático.");
            return "error.html";
        }
    }

    public static void validateFestival(FestivalForm festival, BindingResult bindingResult) {
        if (festival == null) {
            bindingResult.rejectValue("festival", "error.festival", "El festival no puede ser nulo.");
            return;
        }


        if (isEmptyOrNull(festival.getNom())) {
            bindingResult.rejectValue("nom", "error.nom", "El nombre del festival no puede estar vacío.");
        }

        if (festival.getAnyo() < 2024) {
            bindingResult.rejectValue("anyo", "error.anyo", "El año del festival no puede ser inferior al año actual.");
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

    public void clearBindingResult(BindingResult bindingResult) {
        List<FieldError> fieldErrors = new ArrayList<>(bindingResult.getFieldErrors());
        for (FieldError fieldError : fieldErrors) {
            bindingResult.rejectValue(fieldError.getField(), null, null);
        }
    }
}


