package es.uji.ei1027.TotFest.controllers;

import es.uji.ei1027.TotFest.daos.FestivalDao;
import es.uji.ei1027.TotFest.daos.EntradaDao;
import es.uji.ei1027.TotFest.models.CompraForm;
import es.uji.ei1027.TotFest.models.Entrada;

import es.uji.ei1027.TotFest.models.EntradaTipus;
import es.uji.ei1027.TotFest.models.EntradaTipusEnum;
import es.uji.ei1027.TotFest.models.Festival;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/compra-entradas")
public class EntradaController {

    private EntradaDao entradaDao;
    private FestivalDao festivalDao;

    @Autowired
    public void setEntradaDao(EntradaDao entradaDao) {
        this.entradaDao = entradaDao;
    }


    @Autowired
    public void setFestivalDao(FestivalDao festivalDao) {
        this.festivalDao = festivalDao;
    }

    @RequestMapping("/compra/{idFestival}")
    public String showCompraForm(Model model, @PathVariable int idFestival, HttpSession session) {
        Festival festival = festivalDao.getFestival(idFestival);
        if (new Date().before(festival.getDataIniciVenda())) {
            model.addAttribute("noDisponible", "true");
        } else {
            model.addAttribute("noDisponible", "false");
        }
        int numEntradasDia = Math.min((festivalDao.getFestival(idFestival).getAforamentMaxim() - entradaDao.getEntradesVenudesPerDiaDeDia(idFestival, java.sql.Date.valueOf(LocalDate.now())))/10, 10);
        int numEntradasCompleto = Math.min(festivalDao.getFestival(idFestival).getAforamentMaxim() - festivalDao.getFestival(idFestival).getNumEntradasVendidas(), 10);

        model.addAttribute("numEntradasDia", numEntradasDia);
        model.addAttribute("numEntradasCompleto", numEntradasCompleto);

        BigDecimal precioEntradaDia = entradaDao.getEntradaTipus(idFestival, EntradaTipusEnum.dia.name()).getPreu();
        BigDecimal precioEntradaCompleto = entradaDao.getEntradaTipus(idFestival, EntradaTipusEnum.festivalComplet.name()).getPreu();

        model.addAttribute("festival", festival);
        model.addAttribute("precioDia", precioEntradaDia);
        model.addAttribute("precioCompleto", precioEntradaCompleto);

        session.setAttribute("ultimoFestivalCompra", festival);
        session.setAttribute("ultimoPrecioDia", precioEntradaDia);
        session.setAttribute("ultimoPrecioCompleto", precioEntradaCompleto);


        CompraForm compraForm = new CompraForm();
        compraForm.setIdFestival(idFestival);
        model.addAttribute("compraForm", compraForm);
        return "entrades/compra";
    }


    @RequestMapping(value = "/compra", method = RequestMethod.POST)
    public String processCompraForm(@ModelAttribute("compraForm") CompraForm compraForm,
                                    BindingResult bindingResult, HttpSession session, Model model) {

        try {
            int idfestival = ((Festival) session.getAttribute("ultimoFestivalCompra")).getIdFestival();

            if (bindingResult.hasErrors()) {
                return ("redirect://compra-entradas/compra/"+idfestival);
            }

            if (compraForm.getListaFechas().size() > 0) {
                compraForm.setTipusEntrada(EntradaTipusEnum.dia);
                for (int i = 0; i < compraForm.getListaFechas().size(); i++) {
                    Entrada entrada = new Entrada();
                    entrada.setData(new java.sql.Date(compraForm.getListaFechas().get(i).getTime()));
                    entrada.setIdFestival(idfestival);
                    entrada.setDatacompra(java.sql.Date.valueOf(LocalDate.now()));
                    entrada.setEntradaTipus(1);
                    entrada.setPreuVendaEntradaIndividual((BigDecimal) session.getAttribute("ultimoPrecioDia"));
                    entradaDao.addEntrada(entrada);
                }
                entradaDao.addNumEntradasVendidasEntradaTipus(idfestival, EntradaTipusEnum.dia.name(), compraForm.getListaFechas().size());
            } else {
                Entrada entrada = new Entrada();
                entrada.setPreuVendaEntradaIndividual((BigDecimal) session.getAttribute("ultimoPrecioCompleto"));
                entrada.setIdFestival(idfestival);
                entrada.setEntradaTipus(2);
                entrada.setDatacompra(java.sql.Date.valueOf(LocalDate.now()));
                entradaDao.addEntrada(entrada);
                entradaDao.addNumEntradasVendidasEntradaTipus(idfestival, EntradaTipusEnum.festivalComplet.name(), compraForm.getListaFechas().size());

            }

        } catch (Exception e) {
            return "redirect:/festival/list";
        }
        model.addAttribute("emailUltimaCompra", compraForm.getEmail());
        return "entrades/compraExitosa";

    }


    //@RequestMapping(value = "/compra/{idFestival}", method = RequestMethod.POST)
    //public String processCompraForm(@ModelAttribute("compraForm") EntradaTipus entradaTipus, @PathVariable int idFestival,
    //                                BindingResult bindingResult, HttpSession session, Model model) {
    //   // // Validaciones adicionales
    //   // if (compraForm.getNumEntrades() > 10) {
    //   //     bindingResult.rejectValue("numEntrades", "error.numEntrades", "No pots comprar més de 10 entrades a la vegada.");
    //   // }
//
    //    Festival festival = festivalDao.getFestival(entradaTipus.getIdFestival());
//
    //    // Validar si la venta está permitida en la fecha actual
    //    if (new Date().before(festival.getDataIniciVenda())) {
    //        bindingResult.rejectValue("idFestival", "error.dataIniciVenda", "La venda no està disponible encara.");
    //    }
//
    //    // Validar aforo
    //    int totalEntradesVenudes = festivalDao.getNumEntradasVendidas(festival.getIdFestival());
    //    int aforamentMaxim = festival.getAforamentMaxim();
    //    int entradesVenudesDia = entradaDao.getEntradesVenudesPerDia(festival.getIdFestival(), compraForm.getDataPerTipusDia());
//
    //    if (compraForm.getIdEntradaTipus().equals(EntradaTipusEnum.festivalComplet.toString())) {
    //        if (totalEntradesVenudes + compraForm.getNumEntrades() > aforamentMaxim) {
    //            bindingResult.rejectValue("numEntrades", "error.aforament", "No hi ha suficient aforament per a tantes entrades.");
    //        }
    //    } else {
    //        if (entradesVenudesDia + compraForm.getNumEntrades() > aforamentMaxim * 0.10) {
    //            bindingResult.rejectValue("numEntrades", "error.aforamentDia", "No hi ha suficient aforament per a tantes entrades en aquest dia.");
    //        }
    //    }
//
    //    if (bindingResult.hasErrors()) {
    //        model.addAttribute("festivals", festivalDao.getFestivals());
    //        return "entrades/compra";
    //    }
//
    //    // Procesar el pago (aquí iría la integración con la pasarela de pago)
//
    //    // Simular una respuesta exitosa de la pasarela de pagos
    //    boolean pagamentExit = true;
//
    //    if (pagamentExit) {
    //        // Registrar la venta y crear las entradas
    //        for (int i = 0; i < compraForm.getNumEntrades(); i++) {
    //            Entrada entrada = new Entrada();
    //            entrada.setNumero(totalEntradesVenudes + i + 1);
    //            entrada.setIdEntradaTipus(compraForm.getIdEntradaTipus());
    //            entrada.setPreuVendaEntradaIndividual(entradaTipus.getPreu());
    //            entradaDao.addEntrada(entrada);
    //        }
//
    //        // Enviar correo electrónico con las entradas (simulado)
    //        // String emailContent = generarContenidoEmail(compraForm);
    //        // emailService.sendEmail(compraForm.getEmail(), "Les teves entrades", emailContent);
//
    //        return "redirect:/entrades/confirmacio";
    //    } else {
    //        bindingResult.rejectValue("numEntrades", "error.pagament", "Hi ha hagut un problema amb el pagament.");
    //        model.addAttribute("festivals", festivalDao.getFestivals());
    //        return "entrades/compra";
    //    }
    //}
}//


