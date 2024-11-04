package es.uji.ei1027.TotFest.controllers;

import es.uji.ei1027.TotFest.daos.PromotorDao;
import es.uji.ei1027.TotFest.models.Promotor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/promotor")
public class PromotorController {

    private final PromotorDao promotorDao;
    private static final String CIF_REGEX = "^[A-Z0-9]{1,20}$";

    @Autowired
    public PromotorController(PromotorDao promotorDao) {
        this.promotorDao = promotorDao;
    }

    @RequestMapping("/list")
    public String listPromotores(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        List<Promotor> promotores = promotorDao.getPromotores(size, page);
        int totalElems = promotores.size();
        int totalPages = (int) Math.ceil((double) totalElems / size);

        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElems);

        model.addAttribute("promotores", promotores.subList(startIndex, endIndex));
        model.addAttribute("size", size);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElems", totalElems);

        return "gestorFestival/promotor/list";
    }

    @GetMapping("/add")
    public String addPromotorForm(Model model) {
        Promotor promotor = new Promotor();
        if (promotor.getDataBaixaRelacioComercial() == null) {
            promotor.setDataBaixaRelacioComercial(Date.valueOf(LocalDate.now())); // java.sql.Date
        }

        // Asigna la fecha de inicio del gestor del festival si es nula
        if (promotor.getDatainiciGestorFest() == null) {
            promotor.setDatainiciGestorFest(Date.valueOf(LocalDate.now())); // java.sql.Date
        }
        model.addAttribute("promotor", promotor);
        return "gestorFestival/promotor/add";
    }

    @PostMapping("/add")
    public String addPromotor(@ModelAttribute Promotor promotor, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "promotor/add";
        }

        promotor.setDataAlta(Date.valueOf(LocalDate.now()));
        promotorDao.addPromotor(promotor);
        model.addAttribute("mensaje", "Se ha añadido correctamente el promotor");
        return "gestorFestival/promotor/exito";    }

    @GetMapping("/update/{id}")
    public String updatePromotorForm(@PathVariable int id, Model model) {
        Promotor promotor = promotorDao.getPromotor(id);
        if (promotor.getDataBaixaRelacioComercial() == null) {
            promotor.setDataBaixaRelacioComercial(Date.valueOf(LocalDate.now())); // java.sql.Date
        }

        // Asigna la fecha de inicio del gestor del festival si es nula
        if (promotor.getDatainiciGestorFest() == null) {
            promotor.setDatainiciGestorFest(Date.valueOf(LocalDate.now())); // java.sql.Date
        }
        model.addAttribute("promotor", promotor);
        return "gestorFestival/promotor/update";
    }

    @PostMapping("/update")
    public String updatePromotor(@ModelAttribute Promotor promotor, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "promotor/update";
        }

        promotorDao.updatePromotor(promotor);
        model.addAttribute("mensaje", "Se ha modificado correctamente el promotor");
        return "gestorFestival/promotor/exito";
    }

    @RequestMapping("/delete-selected")
    public String deletePromotor(@RequestParam(value = "selectedPromotores", required = false) List<Integer> selectedPromotores, Model model ) {

        for (Integer id: selectedPromotores) {
            promotorDao.deletePromotor(id);
        }

        model.addAttribute("mensaje", "Se ha eliminado correctamente el promotor");
        return "gestorFestival/promotor/exito";
    }


    public void validatePromotor(Promotor promotor, BindingResult bindingResult) {
        if (promotor.getCif() == null || promotor.getCif().isEmpty()) {
            bindingResult.rejectValue("cif", "required", "El CIF es obligatorio");
        } else if (!Pattern.matches(CIF_REGEX, promotor.getCif())) {
            bindingResult.rejectValue("cif", "format", "El formato del CIF es inválido");
        }

        // Validar campo obligatorio: Nombre del Organismo
        if (promotor.getNomOrganisme() == null || promotor.getNomOrganisme().isEmpty()) {
            bindingResult.rejectValue("nomOrganisme", "required", "El nombre del organismo es obligatorio");
        }

        // Validar campo obligatorio: Domicilio Fiscal
        if (promotor.getDomiciliFiscal() == null || promotor.getDomiciliFiscal().isEmpty()) {
            bindingResult.rejectValue("domiciliFiscal", "required", "El domicilio fiscal es obligatorio");
        }

        // Validar campo obligatorio: Sector
        if (promotor.getSector() == null || promotor.getSector().isEmpty()) {
            bindingResult.rejectValue("sector", "required", "El sector es obligatorio");
        }

        // Validar campo obligatorio: Fecha de Alta
        if (promotor.getDataAlta() == null) {
            bindingResult.rejectValue("dataAlta", "required", "La fecha de alta es obligatoria");
        }

    }
}
