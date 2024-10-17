package es.uji.ei1027.TotFest.controllers;

import es.uji.ei1027.TotFest.daos.ArtistaGrupDao;
import es.uji.ei1027.TotFest.models.ArtistaGrup;
import es.uji.ei1027.TotFest.models.ContracteArtista;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/artista")
public class ArtistaGrupController {

    private ArtistaGrupDao artistaGrupDao;

    @Autowired
    public void setArtistaGrupDao(ArtistaGrupDao artistaGrupDao) {
        this.artistaGrupDao = artistaGrupDao;
    }

    @RequestMapping("/list")
    public String listArtistas(Model model) {
        List<ArtistaGrup> artistas = artistaGrupDao.getArtistaGrups();
        artistas.sort(Comparator.comparingInt(ArtistaGrup::getIdArtista));

        model.addAttribute("artistas", artistas);
        return "artista/list";
    }

    @RequestMapping("/add")
    public String addArtista(Model model) {
        model.addAttribute("artista", new ArtistaGrup());
        return "artista/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(
            @ModelAttribute("artista") ArtistaGrup artista,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "artista/add";
        }

        try {
            artistaGrupDao.addArtistaGrup(artista);
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
    public String editArtista(Model model, @PathVariable int idArtista) {
        model.addAttribute("artista", artistaGrupDao.getArtistaGrup(idArtista));
        return "artista/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("artista") ArtistaGrup artista,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "artista/update";
        }
        artistaGrupDao.updateArtistaGrup(artista);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idArtista}")
    public String processDelete(@PathVariable int idArtista) {
        artistaGrupDao.deleteArtistaGrup(idArtista);
        return "redirect:../list";
    }
}
