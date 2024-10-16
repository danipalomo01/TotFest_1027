package es.uji.ei1027.TotFest.controllers;

import es.uji.ei1027.TotFest.daos.ActuacioDao;
import es.uji.ei1027.TotFest.daos.EntradaDao;
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
import java.sql.Time;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/festival")
public class FestivalController {

    private FestivalDao festivalDao;
    private ActuacioDao actuacioDao;
    private EntradaDao entradaDao;

    @Autowired
    public void setFestivalDao(FestivalDao festivalDao) {
        this.festivalDao = festivalDao;
    }

    @Autowired
    public void setActuacioDao(ActuacioDao actuacioDao) {
        this.actuacioDao = actuacioDao;
    }

    @Autowired
    public void setEntradaDao(EntradaDao entradaDao) {
        this.entradaDao = entradaDao;
    }

    @RequestMapping("/list")
    public String listFestivals(HttpSession session,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "5") int size,
                                @RequestParam(value = "localizacion", required = false) String localizacion,
                                @RequestParam(value = "anyo", required = false) Integer anyo,
                                @RequestParam(value = "categoria", required = false) String categoria,
                                Model model) {

        try {
            int offset = page * size;

            List<Festival> festivalesTodos = festivalDao.getFestivals();
            List<Festival> festivalesOffset = festivalDao.getFestivals(size, offset);
            List<Festival> festivales = new ArrayList<>();

            LocalDate fechaActual = LocalDate.now();

            //for (Festival festival : festivalesOffset) {
            //    if (festival.getDataIniciPublicacio().toLocalDate().isEqual(fechaActual) ||
            //            festival.getDataIniciPublicacio().toLocalDate().isBefore(fechaActual)) {
            //        festivales.add(festival);
            //    }
            //}

            for (Festival festival : festivalesOffset) {
                boolean matchesFilters = (localizacion == null || festival.getLocalitzacioDescriptiva().toLowerCase().contains(localizacion.toLowerCase())) &&
                        (anyo == null || festival.getAnyo() == anyo) &&
                        (categoria == null || festival.getCategoriaMusical().toLowerCase().contains(categoria.toLowerCase())) &&
                        (festival.getDataIniciPublicacio().toLocalDate().isEqual(fechaActual) ||
                                festival.getDataIniciPublicacio().toLocalDate().isBefore(fechaActual));

                if (matchesFilters) {
                    festivales.add(festival);
                }
            }

            festivales.sort(Comparator.comparingInt(Festival::getIdFestival));

            model.addAttribute("fechaActual", fechaActual);
            model.addAttribute("localizacion", localizacion);
            model.addAttribute("anyo", anyo);
            model.addAttribute("categoria", categoria);
            model.addAttribute("currentPage", page);

            festivales.sort(Comparator.comparingInt(Festival::getIdFestival));

            model.addAttribute("fechaActual", fechaActual);

            model.addAttribute("currentPage", page);
            model.addAttribute("totalElems", festivalDao.getFestivals().size());

            int totalFestivales = festivalesTodos.size();
            int totalPages = (int) Math.ceil((double) totalFestivales / size);

            HashMap<Integer, List<Integer>> diccionarioPreciosEntrada = new HashMap<Integer, List<Integer>>();

            for(int i = 0; i < festivales.size(); i++) {
                int precioDia = festivalDao.getPrecioEntradaDia(festivales.get(i).getIdFestival());
                int precioCompleto = festivalDao.getPrecioEntradaCompleto(festivales.get(i).getIdFestival());
                int numEntradasDia = Math.min((festivalDao.getFestival(festivales.get(i).getIdFestival()).getAforamentMaxim()/10 - entradaDao.getEntradesVenudesPerDiaDeDia(festivales.get(i).getIdFestival(), java.sql.Date.valueOf(LocalDate.now()))), 10);
                int numEntradasCompleto = Math.min(festivalDao.getFestival(festivales.get(i).getIdFestival()).getAforamentMaxim() - festivalDao.getNumEntradasVendidas(festivales.get(i).getIdFestival()), 10);

                List<Integer> listaPrecios = new ArrayList<Integer>();
                listaPrecios.add(precioDia);
                listaPrecios.add(precioCompleto);
                listaPrecios.add(numEntradasDia);
                listaPrecios.add(numEntradasCompleto);

                diccionarioPreciosEntrada.put(festivales.get(i).getIdFestival(), listaPrecios);
            }

            model.addAttribute("festivalesPrecios", diccionarioPreciosEntrada);
            model.addAttribute("festivales", festivales);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);


            return "festival/list";
        }  catch (Exception e){
            model.addAttribute("mensajeError", "No se ha podidon obtener los festivales, inténtalo de nuevo más tarde.");
            return "error.html";
        }
    }



    @RequestMapping(value = "/info/{idFestival}", method = RequestMethod.GET)
    public String info(Model model, @PathVariable int idFestival) {

        try {
            Festival festival = festivalDao.getFestival(idFestival);
            model.addAttribute("festival", festival);

            List<Actuacio> todasActuaciones = actuacioDao.getActuacionsFestival(festival.getIdFestival());
            Map<Date, List<Actuacio>> listaFechasActuaciones = todasActuaciones.stream()
                    .collect(Collectors.groupingBy(Actuacio::getData));

            Map<Date, List<Actuacio>> listaFechasActuacionesOrdenadas = listaFechasActuaciones.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey()) // Orden descendente
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue,
                            LinkedHashMap::new // Para mantener el orden de inserción
                    ));


            // Ordenar las actuaciones por hora de inicio para cada día
            listaFechasActuacionesOrdenadas.values().forEach(la -> la.sort(Comparator.comparing(Actuacio::getHoraInici)));

            model.addAttribute("actuaciones", listaFechasActuacionesOrdenadas);
            return "festival/info";
        }  catch (Exception e){
            model.addAttribute("mensajeError", "No se ha podido obtener las actuaciones del festival, inténtalo de nuevo más tarde.");
            return "error.html";
        }
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
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idFestival}")
    public String processDelete(@PathVariable int idFestival) {
        return "redirect:/list";
    }


}

