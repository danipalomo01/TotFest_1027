package es.uji.ei1027.TotFest.controllers;

import es.uji.ei1027.TotFest.daos.ActuacioDao;
import es.uji.ei1027.TotFest.models.Actuacio;
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

@Controller
@RequestMapping("/actuacio")
public class ActuacioController {

    private ActuacioDao actuacioDao;

    @Autowired
    public void setActuacioDao(ActuacioDao actuacioDao) {
        this.actuacioDao = actuacioDao;
    }

    @RequestMapping("/list")
    public String listActuacions(Model model) {
        model.addAttribute("actuacions", actuacioDao.getActuacions());
        return "actuacio/list";
    }

    @RequestMapping("/add")
    public String addActuacio(Model model) {
        model.addAttribute("actuacio", new Actuacio());
        return "actuacio/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(
            @ModelAttribute("actuacio") Actuacio actuacio,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "actuacio/add";
        }

        try {
            actuacioDao.addActuacio(actuacio);
        } catch (DuplicateKeyException e) {
            // Manejar la excepción de clave duplicada si es necesario
        } catch (DataAccessException e) {
            // Manejar la excepción de acceso a datos si es necesario
        } catch (Exception e) {
            // Manejar otras excepciones no esperadas si es necesario
        }

        return "redirect:list";
    }

    @RequestMapping(value = "/update/{idActuacio}", method = RequestMethod.GET)
    public String editActuacio(Model model, @PathVariable int idActuacio) {
        model.addAttribute("actuacio", actuacioDao.getActuacio(idActuacio));
        return "actuacio/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("actuacio") Actuacio actuacio,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "actuacio/update";
        }
        actuacioDao.updateActuacio(actuacio);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idActuacio}")
    public String processDelete(@PathVariable int idActuacio) {
        actuacioDao.deleteActuacio(idActuacio);
        return "redirect:../list";
    }
}
