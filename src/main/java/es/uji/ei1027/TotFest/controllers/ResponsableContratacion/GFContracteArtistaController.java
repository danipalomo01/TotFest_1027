package es.uji.ei1027.TotFest.controllers.ResponsableContratacion;

import es.uji.ei1027.TotFest.daos.ActuacionsFestivalDao;
import es.uji.ei1027.TotFest.daos.ContracteArtistaDao;
import es.uji.ei1027.TotFest.daos.FestivalDao;
import es.uji.ei1027.TotFest.daos.ActuacioDao;
import es.uji.ei1027.TotFest.models.*;
import es.uji.ei1027.TotFest.daos.ArtistaGrupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("responsablecontratacion/contracteartista")
public class GFContracteArtistaController {

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

    @RequestMapping("/list")
    public String listContractesArtistes(HttpSession session,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "1") int size,
                                         Model model) {


        //if (session == null || session.getAttribute("user") == null) {
        //    return "redirect:/login";
        //}

        session.setAttribute("solapamiento", "false");
        if (session.getAttribute("size") != null && size == 1) {
            size = (int) session.getAttribute("size");
        } else {
            session.setAttribute("size", size);
        }

        List<ContracteArtista> contractesArtistes = contracteArtistaDao.getContractesArtistes(page, size);
        int totalContracts = contracteArtistaDao.getContractesArtistes().size();
        int totalPages = (int) Math.ceil((double) totalContracts / size);

        model.addAttribute("contratos", contractesArtistes);
        model.addAttribute("artistas", artistaDao.getArtistaGrups());
        model.addAttribute("festivales", festivalDao.getFestivals());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);

        return "responsablecontratacion/contracteartista/list";
    }

    @RequestMapping("/add")
    public String addContracteArtista(HttpSession session, Model model) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        model.addAttribute("contratoNuevo", new ContracteArtista());
        model.addAttribute("artistas", artistaDao.getArtistaGrups());

        return "responsablecontratacion/contracteartista/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(
            @ModelAttribute("contratoNuevo") ContracteArtista contracteArtista,
            BindingResult bindingResult, HttpSession session, Model model) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        validarAddContratoArtista(contracteArtista, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("artistas", artistaDao.getArtistaGrups());
            return "responsablecontratacion/contracteartista/add";
        }
        try {
            contracteArtista.setIdComercial(Integer.parseInt(session.getAttribute("idComercial").toString()));
            contracteArtistaDao.addContracteArtista(contracteArtista);

        } catch (DuplicateKeyException e) {
            bindingResult.rejectValue("idContracte", "duplicate", "Este ID de contrato ya existe");
            return "responsablecontratacion/contracteartista/add";
        } catch (DataAccessException e) {
            bindingResult.reject("database", "Error de acceso a datos");
            return "responsablecontratacion/contracteartista/add";
        }
        return "redirect:list";
    }

    @RequestMapping(value = "/update/{idContracte}", method = RequestMethod.GET)
    public String editContracteArtista(HttpSession session, Model model, @PathVariable int idContracte) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        model.addAttribute("artistas", artistaDao.getArtistaGrups());
        model.addAttribute("contracteArtista", contracteArtistaDao.getContracteArtista(idContracte));
        session.setAttribute("lastContratoUpdate", idContracte);
        return "responsablecontratacion/contracteartista/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(HttpSession session,
                                      @ModelAttribute("contracteArtista") ContracteArtista contracteArtista,
                                      BindingResult bindingResult, Model model) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        validarAddContratoArtista(contracteArtista, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("artistas", artistaDao.getArtistaGrups());
            return "responsablecontratacion/contracteartista/update";
        }
        try {
            contracteArtista.setIdContracte(Integer.parseInt(session.getAttribute("lastContratoUpdate").toString()));
            contracteArtista.setIdComercial(Integer.parseInt(session.getAttribute("idComercial").toString()));
            contracteArtistaDao.updateContracteArtista(contracteArtista);
        } catch (Exception e) {
            return "responsablecontratacion/contracteartista/update";
        }
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idContracte}")
    public String processDelete(HttpSession session, @PathVariable int idContracte) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        contracteArtistaDao.deleteContracteArtista(idContracte);
        return "redirect:/list";
    }

    private void validarAddContratoArtista(ContracteArtista contrato, BindingResult bindingResult) {
        if (contrato.getIdArtista() == 0) {
            bindingResult.rejectValue("idArtista", "error.idArtista", "Debes seleccionar un artista.");
        }

        if(contrato.getDataInici() == null || contrato.getDataFi() == null) {
            if(contrato.getDataInici() == null) {
                bindingResult.rejectValue("dataInici", "error.dataInici", "La fecha de inicio es requerida.");
            }
            if(contrato.getDataFi() == null) {
                bindingResult.rejectValue("dataFi", "error.dataFi", "La fecha de fin es requerida.");
            }
        } else {
            LocalDate dataInici = contrato.getDataInici().toLocalDate();
            LocalDate dataFi = contrato.getDataFi().toLocalDate();
            if (dataFi.isBefore(dataInici)) {
                bindingResult.rejectValue("dataFi", "error.dataFi", "La fecha de fin no puede ser anterior a la fecha de inicio.");
            }
        }

        if (contrato.getCondicionsDescriptiu() == null || contrato.getCondicionsDescriptiu().trim().isEmpty()) {
            bindingResult.rejectValue("condicionsDescriptiu", "error.condicionsDescriptiu", "Las condiciones descriptivas son requeridas.");
        } else if (contrato.getCondicionsDescriptiu().length() > 500) {
            bindingResult.rejectValue("condicionsDescriptiu", "error.condicionsDescriptiu", "Las condiciones descriptivas no pueden exceder los 500 caracteres.");
        }

        if (contrato.getNumActuacionsAny() <= 0) {
            bindingResult.rejectValue("numActuacionsAny", "error.numActuacionsAny", "El número de actuaciones por año debe ser mayor que 0.");
        }

        if (contrato.getImportContracte() == null || contrato.getImportContracte().compareTo(BigDecimal.ZERO) <= 0) {
            bindingResult.rejectValue("importContracte", "error.importContracte", "El importe del contrato debe ser mayor que 0.");
        }
    }



}
