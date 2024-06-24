package es.uji.ei1027.TotFest.converter;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConversionFailedException(ConversionFailedException ex, RedirectAttributes redirectAttributes, Model model) {
        // Puedes agregar un mensaje personalizado aquí
        redirectAttributes.addFlashAttribute("errorMessage", "La fecha de inicio de publicación no es válida.");

        // Redirige a la página del formulario o donde quieras manejar el error
        return "redirect:/gestorFestival/list"; // Ajusta el endpoint de redirección según tu aplicación
    }
}

