package es.uji.ei1027.TotFest.controllers;

import es.uji.ei1027.TotFest.daos.FestivalDao;
import es.uji.ei1027.TotFest.models.Actuacio;
import es.uji.ei1027.TotFest.models.Festival;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/festival")
public class FestivalController {

    private FestivalDao festivalDao;

    @Autowired
    public void setFestivalDao(FestivalDao festivalDao) {
        this.festivalDao = festivalDao;
    }

    @RequestMapping("/list")
    public String listFestivals(HttpSession session,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "5") int size,
                                Model model) {
        int offset = page * size;

        List<Festival> festivalesTodos = festivalDao.getFestivals();
        List<Festival> festivalesOffset = festivalDao.getFestivals(size, offset);
        List<Festival> festivales = new ArrayList<>();

        LocalDate fechaActual = LocalDate.now();

        for (Festival festival : festivalesOffset) {
            if (festival.getDataIniciPublicacio().toLocalDate().isEqual(fechaActual) ||
                    festival.getDataIniciPublicacio().toLocalDate().isBefore(fechaActual)) {
                festivales.add(festival);
            }
        }
        model.addAttribute("fechaActual", fechaActual);

        model.addAttribute("currentPage", page);
        int totalFestivales = festivalesTodos.size();
        int totalPages = (int) Math.ceil((double) totalFestivales / size);
        model.addAttribute("festivales", festivales);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        return "festival/list";
    }



    @RequestMapping(value = "/info/{idFestival}", method = RequestMethod.GET)
    public String info(Model model, @PathVariable int idFestival) {
        model.addAttribute("festival", festivalDao.getFestival(idFestival));
        List<Actuacio> actuaciones = festivalDao.getActuacionesFestival(idFestival);
        actuaciones.sort(Comparator.comparing(Actuacio::getHoraInici));
        model.addAttribute("actuaciones", actuaciones);
        return "festival/info";
    }

    @RequestMapping("/add")
    public String addFestival(Model model) {
        model.addAttribute("festival", new Festival());
        return "festival/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(
            @ModelAttribute("festival") Festival festival,
            BindingResult bindingResult, HttpServletRequest request) {
        // Aquí puedes incluir la validación del Festival si es necesario
        if (bindingResult.hasErrors()) {
            return "festival/add";
        }

        try {
            festivalDao.addFestival(festival, request.getSession());
        } catch (DuplicateKeyException e) {
            // Manejar la excepción de clave duplicada si es necesario
        } catch (DataAccessException e) {
            // Manejar la excepción de acceso a datos si es necesario
        } catch (Exception e) {
            // Manejar otras excepciones no esperadas si es necesario
        }

        return "redirect:list";
    }

    @RequestMapping(value = "/update/{idFestival}", method = RequestMethod.GET)
    public String editFestival(Model model, @PathVariable int idFestival) {
        model.addAttribute("festival", festivalDao.getFestival(idFestival));
        return "festival/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("festival") Festival festival,
            BindingResult bindingResult) {
        // Puedes incluir la validación del Festival si es necesario
        if (bindingResult.hasErrors()) {
            return "festival/update";
        }
        festivalDao.updateFestival(festival);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idFestival}")
    public String processDelete(@PathVariable int idFestival) {
        festivalDao.deleteFestival(idFestival);
        return "redirect:/list";
    }
}

