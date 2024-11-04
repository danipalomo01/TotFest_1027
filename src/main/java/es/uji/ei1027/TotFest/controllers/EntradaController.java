package es.uji.ei1027.TotFest.controllers;

import es.uji.ei1027.TotFest.daos.CompraDao;
import es.uji.ei1027.TotFest.daos.FestivalDao;
import es.uji.ei1027.TotFest.daos.EntradaDao;
import es.uji.ei1027.TotFest.daos.UsuarioDao;
import es.uji.ei1027.TotFest.models.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/compra-entradas")
public class EntradaController {

    private EntradaDao entradaDao;
    private FestivalDao festivalDao;
    private UsuarioDao usuarioDao;
    private CompraDao compraDao;

    @Autowired
    public void setEntradaDao(EntradaDao entradaDao) {
        this.entradaDao = entradaDao;
    }

    @Autowired
    public void setCompraDao(CompraDao compraDao) {
        this.compraDao = compraDao;
    }

    @Autowired
    public void setUsuarioDao(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    @Autowired
    public void setFestivalDao(FestivalDao festivalDao) {
        this.festivalDao = festivalDao;
    }

    @RequestMapping("/compraDia/{idFestival}")
    public String showCompraDiaForm(Model model, @PathVariable int idFestival, HttpSession session) {
        Festival festival = festivalDao.getFestival(idFestival);
        if (new Date().before(festival.getDataIniciVenda())) {
            model.addAttribute("noDisponible", "true");
        } else {
            model.addAttribute("noDisponible", "false");
        }

        EntradaTipus entradaTipus = entradaDao.getEntradaTipus(idFestival, EntradaTipusEnum.DIA.getValue());
        int numEntradasDia = Math.min((BigDecimal.valueOf(festivalDao.getFestival(idFestival).getAforamentMaxim()).multiply(entradaTipus.getPercentatgeMaximAforament()).divide(BigDecimal.valueOf(100), RoundingMode.FLOOR).subtract(BigDecimal.valueOf(entradaDao.getEntradesVenudesPerDiaDeDia(idFestival, java.sql.Date.valueOf(LocalDate.now()))))).intValue(), 10);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String dataInici = festival.getDataInici().toLocalDate().format(formatter);
        String dataFi = festival.getDataFi().toLocalDate().format(formatter);

        model.addAttribute("numEntradasDia", numEntradasDia);
        model.addAttribute("festival", festival);
        model.addAttribute("dataInicio", dataInici.toString());
        model.addAttribute("dataFin", dataFi);

        session.setAttribute("ultimoFestivalCompra", festival);

        CompraForm compraForm = new CompraForm();
        compraForm.setFecha(festival.getDataInici());
        compraForm.setEntradatipus(EntradaTipusEnum.DIA.getValue());
        compraForm.setIdFestival(idFestival);
        model.addAttribute("compraForm", compraForm);

        BigDecimal precioEntradaDia = entradaDao.getEntradaTipus(festival.getIdFestival(), EntradaTipusEnum.DIA.getValue()).getPreu();
        model.addAttribute("precio", precioEntradaDia);

        return "entrades/compraDia";
    }

    @GetMapping("/view/{numero}")
    public String viewEntrada(HttpSession session, @PathVariable("numero") int numero, Model model) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        // Obtener la entrada por su número
        Entrada entrada = entradaDao.getEntrada(numero);
        if (entrada == null) {
            model.addAttribute("mensajeError", "No se ha encontrado la entrada con ese número.");
            return "error.html";
        }

        EntradaTipus entradaTipus = entradaDao.getEntradaTipus(entrada.getEntradaTipus());
        Compra compra = compraDao.getCompra(entrada.getIdcompra());
        Festival festival = festivalDao.getFestival(entradaTipus.getIdFestival());
        // Añadir la entrada al modelo para mostrarla en la vista
        model.addAttribute("entrada", entrada);
        model.addAttribute("telefono", compra.getTelefon());
        model.addAttribute("preu", entradaTipus.getPreu());
        model.addAttribute("email", compra.getEmail());
        model.addAttribute("entradaTipus", entradaTipus.getEntradaTipus().getValue() == 1 ? "Entrada de Día" : "Entrada del Festival Completo");

        model.addAttribute("festival", festival.getNom());

        // Dirigir a la vista correspondiente según el tipo de entrada
        return "entrades/ver";
    }

    @RequestMapping("/compraCompleto/{idFestival}")
    public String showCompraCompletoForm(Model model, @PathVariable int idFestival, HttpSession session) {
        Festival festival = festivalDao.getFestival(idFestival);
        if (new Date().before(festival.getDataIniciVenda())) {
            model.addAttribute("noDisponible", "true");
        } else {
            model.addAttribute("noDisponible", "false");
        }
        int numEntradasCompleto = Math.min(
                BigDecimal.valueOf(festivalDao.getFestival(idFestival).getAforamentMaxim())
                        .subtract(BigDecimal.valueOf(festivalDao.getNumEntradasVendidas(festival.getIdFestival())))
                        .intValue(),
                10
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String dataInici = festival.getDataInici().toLocalDate().format(formatter);
        String dataFi = festival.getDataFi().toLocalDate().format(formatter);

        model.addAttribute("numEntradasCompleto", numEntradasCompleto);
        model.addAttribute("festival", festival);
        model.addAttribute("dataInicio", dataInici.toString());
        model.addAttribute("dataFin", dataFi);

        session.setAttribute("ultimoFestivalCompra", festival);

        CompraForm compraForm = new CompraForm();
        compraForm.setFecha(festival.getDataInici());
        compraForm.setEntradatipus(EntradaTipusEnum.FESTIVAL_COMPLETO.getValue());
        compraForm.setIdFestival(idFestival);

        BigDecimal precioEntradaCompleto = entradaDao.getEntradaTipus(festival.getIdFestival(), EntradaTipusEnum.FESTIVAL_COMPLETO.getValue()).getPreu();

        model.addAttribute("precio", precioEntradaCompleto);
        model.addAttribute("compraForm", compraForm);
        return "entrades/compraCompleto";
    }

    @RequestMapping(value = "/compraParcial", method = RequestMethod.POST)
    public String processCompraParcialForm(@ModelAttribute("compraForm") CompraForm compraForm,
                                    BindingResult bindingResult, HttpSession session, Model model) {

        try {
            if (session.getAttribute("ultimoFestivalCompra") == null){
                return "redirect:/festival/list";
            }
            int idfestival = ((Festival) session.getAttribute("ultimoFestivalCompra")).getIdFestival();

            if (compraForm.getEmail() == null && compraForm.getTelefon() == null) {
                int idUsuario = (int) session.getAttribute("idUsuario");
                Usuario usuario = usuarioDao.getUsuarioById(idUsuario);
                compraForm.setEmail(usuario.getEmail());
                compraForm.setTelefon(usuario.getTelefono());
            }
            validarCompraForm(compraForm, bindingResult, model);

            EntradaTipus entradaTipus = entradaDao.getEntradaTipus(compraForm.getIdFestival(), compraForm.getEntradatipus());

            if (bindingResult.hasErrors()) {
                Festival festival = (Festival) session.getAttribute("ultimoFestivalCompra");

                int numEntradasDia = Math.min((BigDecimal.valueOf(festival.getAforamentMaxim()).multiply(entradaTipus.getPercentatgeMaximAforament()).divide(BigDecimal.valueOf(100), RoundingMode.FLOOR).subtract(BigDecimal.valueOf(entradaDao.getEntradesVenudesPerDiaDeDia(entradaTipus.getIdFestival(), java.sql.Date.valueOf(LocalDate.now()))))).intValue(), 10);
                int numEntradasCompleto = Math.min(
                        BigDecimal.valueOf(festival.getAforamentMaxim())
                                .subtract(BigDecimal.valueOf(festivalDao.getNumEntradasVendidas(festival.getIdFestival())))
                                .intValue(),
                        10
                );

                BigDecimal precioEntradaDia = entradaDao.getEntradaTipus(festival.getIdFestival(), EntradaTipusEnum.DIA.getValue()).getPreu();
                BigDecimal precioEntradaCompleto = entradaDao.getEntradaTipus(festival.getIdFestival(), EntradaTipusEnum.FESTIVAL_COMPLETO.getValue()).getPreu();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String dataInici = festival.getDataInici().toLocalDate().format(formatter);
                String dataFi = festival.getDataFi().toLocalDate().format(formatter);

                model.addAttribute("numEntradasDia", numEntradasDia);
                model.addAttribute("numEntradasCompleto", numEntradasCompleto);
                model.addAttribute("festival", festival);
                model.addAttribute("dataInicio", dataInici.toString());
                model.addAttribute("dataFin", dataFi);
                model.addAttribute("precioDia", precioEntradaDia);
                model.addAttribute("precioCompleto", precioEntradaCompleto);

                session.setAttribute("ultimoFestivalCompra", festival);
                session.setAttribute("ultimoPrecioDia", precioEntradaDia);
                session.setAttribute("ultimoPrecioCompleto", precioEntradaCompleto);


                compraForm.setFecha(festival.getDataInici());
                compraForm.setIdFestival(festival.getIdFestival());
                model.addAttribute("compraForm", compraForm);

                if (compraForm.getEntradatipus() == 1) {
                    return "entrades/compraDia";
                }
                else {
                    return "entrades/compraCompleto";
                }
            }
        } catch (Exception e) {
            return "redirect:/festival/list";
        }

        session.setAttribute("ultimaFecha", compraForm.getFecha());

        model.addAttribute("emailUltimaCompra", compraForm.getEmail());

        model.addAttribute("compraForm", compraForm);

        String redireccion = "/compra-entradas/compra";
        model.addAttribute("redireccion", redireccion);
        model.addAttribute("numEntrades", compraForm.getNumEntrades());
        EntradaTipus entradaTipus = entradaDao.getEntradaTipus(compraForm.getIdFestival(), compraForm.getEntradatipus());
        model.addAttribute("totalImporte", BigDecimal.valueOf(compraForm.getNumEntrades()).multiply(entradaTipus.getPreu()));
        return "entrades/confirmacionCompra";
    }


    @RequestMapping(value = "/compra", method = RequestMethod.POST)
    public String processCompraForm(@ModelAttribute("compraForm") CompraForm compraForm,
                                    BindingResult bindingResult, HttpSession session, Model model) {

        try {
            if (session.getAttribute("ultimoFestivalCompra") == null){
                return "redirect:/festival/list";
            }
            int idfestival = ((Festival) session.getAttribute("ultimoFestivalCompra")).getIdFestival();

            if (compraForm.getEmail() == null && compraForm.getTelefon() == null) {
                int idUsuario = (int) session.getAttribute("idUsuario");
                Usuario usuario = usuarioDao.getUsuarioById(idUsuario);
                compraForm.setEmail(usuario.getEmail());
                compraForm.setTelefon(usuario.getTelefono());
            }
            compraForm.setFecha((Date) session.getAttribute("ultimaFecha"));

            validarCompraForm(compraForm, bindingResult, model);

            EntradaTipus entradaTipus = entradaDao.getEntradaTipus(compraForm.getIdFestival(), compraForm.getEntradatipus());

            if (bindingResult.hasErrors()) {
                Festival festival = (Festival) session.getAttribute("ultimoFestivalCompra");

                int numEntradasDia = Math.min((BigDecimal.valueOf(festival.getAforamentMaxim()).multiply(entradaTipus.getPercentatgeMaximAforament()).divide(BigDecimal.valueOf(100), RoundingMode.FLOOR).subtract(BigDecimal.valueOf(entradaDao.getEntradesVenudesPerDiaDeDia(entradaTipus.getIdFestival(), java.sql.Date.valueOf(LocalDate.now()))))).intValue(), 10);
                int numEntradasCompleto = Math.min(
                        BigDecimal.valueOf(festival.getAforamentMaxim())
                                .subtract(BigDecimal.valueOf(festivalDao.getNumEntradasVendidas(festival.getIdFestival())))
                                .intValue(),
                        10
                );

                BigDecimal precioEntradaDia = entradaDao.getEntradaTipus(festival.getIdFestival(), EntradaTipusEnum.DIA.getValue()).getPreu();
                BigDecimal precioEntradaCompleto = entradaDao.getEntradaTipus(festival.getIdFestival(), EntradaTipusEnum.FESTIVAL_COMPLETO.getValue()).getPreu();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String dataInici = festival.getDataInici().toLocalDate().format(formatter);
                String dataFi = festival.getDataFi().toLocalDate().format(formatter);

                model.addAttribute("numEntradasDia", numEntradasDia);
                model.addAttribute("numEntradasCompleto", numEntradasCompleto);
                model.addAttribute("festival", festival);
                model.addAttribute("dataInicio", dataInici.toString());
                model.addAttribute("dataFin", dataFi);
                model.addAttribute("precioDia", precioEntradaDia);
                model.addAttribute("precioCompleto", precioEntradaCompleto);

                session.setAttribute("ultimoFestivalCompra", festival);
                session.setAttribute("ultimoPrecioDia", precioEntradaDia);
                session.setAttribute("ultimoPrecioCompleto", precioEntradaCompleto);


                compraForm.setFecha(festival.getDataInici());
                compraForm.setIdFestival(festival.getIdFestival());
                model.addAttribute("compraForm", compraForm);

                if (compraForm.getEntradatipus() == 1) {
                    return "entrades/compraDia";
                }
                else {
                    return "entrades/compraCompleto";
                }
            }

            Compra compra = new Compra(java.sql.Date.valueOf(LocalDate.now()), entradaTipus.getPreu().multiply(BigDecimal.valueOf(compraForm.getNumEntrades())).doubleValue(), compraForm.getEmail(), compraForm.getTelefon());
            int idCompra = compraDao.addCompra(compra);

            if (compraForm.getEntradatipus() == 1) {

                for(int i=0; i< compraForm.getNumEntrades(); i++) {
                    Entrada entrada = new Entrada();
                    entrada.setIdcompra(idCompra);
                    entrada.setData(new java.sql.Date(compraForm.getFecha().getTime()));
                    entrada.setDatacompra(java.sql.Date.valueOf(LocalDate.now()));
                    entrada.setEntradaTipus(entradaTipus.getId());
                    entradaDao.addEntrada(entrada);
                }

            } else {
                for(int i=0; i< compraForm.getNumEntrades(); i++) {
                    Entrada entrada = new Entrada();
                    entrada.setIdcompra(idCompra);
                    entrada.setEntradaTipus(entradaTipus.getId());
                    entrada.setDatacompra(java.sql.Date.valueOf(LocalDate.now()));
                    entradaDao.addEntrada(entrada);
                }
            }


        } catch (Exception e) {
            return "redirect:/festival/list";
        }
        model.addAttribute("emailUltimaCompra", compraForm.getEmail());
        return "entrades/compraExitosa";

    }

    public void validarCompraForm(CompraForm compraForm, BindingResult bindingResult, Model model) {

        if (compraForm.getTelefon() == null || compraForm.getEmail() == null || compraForm.getTelefon().length() == 0  || compraForm.getEmail().length() == 0) {
            if (compraForm.getTelefon().length() != 9) {
                bindingResult.rejectValue("telefon", "error.telefon", "El teléfono debe tener 9 numeros");
            }

            String EMAIL_REGEX =
                    "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            Pattern pattern = Pattern.compile(EMAIL_REGEX);

            if (!pattern.matcher(compraForm.getEmail()).matches()) {
                bindingResult.rejectValue("email", "error.email", "El mail no es válido");
            }
        }

        if (compraForm.getFecha() != null) {
            if (compraForm.getFecha().compareTo(new java.util.Date(festivalDao.getFestival(compraForm.getIdFestival()).getDataInici().getTime())) < 0) {
                bindingResult.rejectValue("fecha", "error.fecha", "La fecha seleccionada no puede ser anterior al inicio del festival");
            }

            if (compraForm.getFecha().compareTo(new java.util.Date(festivalDao.getFestival(compraForm.getIdFestival()).getDataFi().getTime())) > 0) {
                bindingResult.rejectValue("fecha", "error.fecha", "La fecha seleccionada no puede ser posterior al fin del festival");
            }
        }

        if (compraForm.getNumEntrades() <= 0) {
            bindingResult.rejectValue("numEntrades", "error.numEntrades", "El número de entradas debe ser mayor que 0");
        }
    }

    @RequestMapping("/list/{idFestival}")
    public String listEntradas(@PathVariable("idFestival") int idFestival,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "5") int size,
                               @RequestParam(value = "tipoEntrada", required = false) Integer tipoEntrada,
                               Model model, HttpSession session) {
        // Verificar si el usuario está en sesión
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        if (tipoEntrada != null) {
            session.setAttribute("ultimoTipoEntrada", tipoEntrada);
        }
        else {
            if (session.getAttribute("ultimoTipoEntrada") != null) {
                tipoEntrada = (int) session.getAttribute("ultimoTipoEntrada");
            }
        }

        List<Entrada> entradas;
        int totalPages = 0;
        int totalElems = 0;

        // Filtrar entradas por idFestival y tipo de entrada
        if (tipoEntrada != null && tipoEntrada != 0) {
            totalElems = entradaDao.getNumTotalEntradasFestivalPorTipo(idFestival, tipoEntrada);
            entradas = entradaDao.getEntradasByFestivalAndTipo(idFestival, tipoEntrada, page, size);
            totalPages = entradaDao.getTotalPagesByFestivalAndTipo(idFestival, tipoEntrada, size);
        } else {
            totalElems = entradaDao.getNumTotalEntradasFestival(idFestival);
            entradas = entradaDao.getEntradasByFestival(idFestival, page, size);
            totalPages = entradaDao.getTotalPagesByFestival(idFestival, size);
        }

        Festival festival = festivalDao.getFestival(idFestival);

        List<String> nombresFestivales = new ArrayList<>();
        List<Integer> tiposEntrada = new ArrayList<>();
        List<java.sql.Date> fechasEntrada = new ArrayList<>();
        List<Double> preciosEntrada = new ArrayList<>();

        for (Entrada entrada: entradas) {

            EntradaTipus entradaTipus = entradaDao.getEntradaTipus(entrada.getEntradaTipus());
            festival = festivalDao.getFestival(entradaTipus.getIdFestival());
            nombresFestivales.add(festival.getNom());

            tiposEntrada.add(entradaTipus.getEntradaTipus().getValue());
            preciosEntrada.add(entradaTipus.getPreu().doubleValue());

            Compra compra = compraDao.getCompra(entrada.getIdcompra());
            fechasEntrada.add(compra.getData());
        }

        entradas.sort(Comparator.comparingInt(Entrada::getNumero));

        model.addAttribute("nombresFestivales", nombresFestivales);
        model.addAttribute("fechasEntrada", fechasEntrada);
        model.addAttribute("tiposEntrada", tiposEntrada);
        model.addAttribute("preciosEntrada", preciosEntrada);

        model.addAttribute("nomfestival", festival.getNom());
        model.addAttribute("entradas", entradas);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("tipoEntrada", tipoEntrada);
        model.addAttribute("idFestival", idFestival);
        model.addAttribute("totalElems", totalElems);
        return "entrades/list";
    }

    @RequestMapping("/listUsuario/{idUsuario}")
    public String listEntradasUsuario(@PathVariable("idUsuario") int idUsuario,
                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "size", defaultValue = "5") int size,
                                      @RequestParam(value = "tipoEntrada", required = false) Integer tipoEntrada,
                                      @RequestParam(value = "buscarFestival", required = false) String buscarFestival,
                                      Model model, HttpSession session) {
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        // Recuperar la información del usuario
        Usuario usuario = usuarioDao.getUsuarioById(idUsuario);
        String email = usuario.getEmail();

        List<Entrada> entradas;

        List<Boolean> devolvibles = new ArrayList<>();

        int totalElems;
        int totalPages;

        // Filtrar por festival si el campo de búsqueda no está vacío
        if (buscarFestival != null && !buscarFestival.isEmpty()) {
            model.addAttribute("filtrado", "Si");
            totalElems = entradaDao.getNumTotalEntradasByFestival(email, buscarFestival);
            entradas = entradaDao.getEntradasByUsuarioAndFestival(email, buscarFestival, page, size);
            totalPages = (int) Math.ceil((double) totalElems / size);
        } else if (tipoEntrada != null && tipoEntrada != 0) {
            model.addAttribute("filtradotipo", tipoEntrada);
            totalElems = entradaDao.getNumTotalEntradasUsuarioPorTipo(email, tipoEntrada);
            entradas = entradaDao.getEntradasByUsuarioAndTipo(email, tipoEntrada, page, size);
            totalPages = entradaDao.getTotalPagesByUsuarioAndTipo(email, tipoEntrada, size);
        } else {
            totalElems = entradaDao.getNumTotalEntradasUsuario(email);
            entradas = entradaDao.getEntradasByUsuario(email, page, size);
            totalPages = entradaDao.getTotalPagesByUsuario(email, size);
        }

        List<String> nombresFestivales = new ArrayList<>();
        List<Integer> tiposEntrada = new ArrayList<>();
        List<java.sql.Date> fechasEntrada = new ArrayList<>();
        List<Double> preciosEntrada = new ArrayList<>();

        for (Entrada entrada: entradas) {


            EntradaTipus entradaTipus = entradaDao.getEntradaTipus(entrada.getEntradaTipus());
            Festival festival = festivalDao.getFestival(entradaTipus.getIdFestival());
            nombresFestivales.add(festival.getNom());
            LocalDate today = LocalDate.now();
            devolvibles.add(festival.getDataInici().toLocalDate().isAfter(today));

            tiposEntrada.add(entradaTipus.getEntradaTipus().getValue());
            preciosEntrada.add(entradaTipus.getPreu().doubleValue());

            Compra compra = compraDao.getCompra(entrada.getIdcompra());
            fechasEntrada.add(compra.getData());
        }

        entradas.sort(Comparator.comparingInt(Entrada::getNumero));

        model.addAttribute("nombresFestivales", nombresFestivales);
        model.addAttribute("fechasEntrada", fechasEntrada);
        model.addAttribute("tiposEntrada", tiposEntrada);
        model.addAttribute("preciosEntrada", preciosEntrada);

        model.addAttribute("devolvibles", devolvibles);
        model.addAttribute("entradaTipo", tipoEntrada);
        model.addAttribute("nomusuario", usuario.getNombre());
        model.addAttribute("entradas", entradas);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("tipoEntrada", tipoEntrada);
        model.addAttribute("idUsuario", idUsuario);
        model.addAttribute("totalElems", totalElems);
        model.addAttribute("buscarFestival", buscarFestival); // Para que se mantenga el valor en el input
        return "entrades/listUsuario";
    }

    @RequestMapping("/devolver")
    public String devolverEntrada(@RequestParam("entradaNumero") int numeroEntrada, @RequestParam("idUsuario") int idUsuario, Model model, HttpSession session) {

        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        entradaDao.deleteEntrada(numeroEntrada);
        entradaDao.addDevolucio(numeroEntrada);
        model.addAttribute("idUsuario", idUsuario);
        return "/entrades/exitoDevolucion";
    }


}//


