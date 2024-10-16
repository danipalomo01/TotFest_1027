package es.uji.ei1027.TotFest.controllers.Registro;

import es.uji.ei1027.TotFest.models.Usuario;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class RegistroValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Usuario.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Usuario usuario = (Usuario) target;

        // Validar campos vacíos
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "error.email", "El email es obligatorio.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "telefono", "error.telefono", "El teléfono es obligatorio.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.password", "La contraseña es obligatoria.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "error.confirmPassword", "Confirme su contraseña.");

        // Validar que las contraseñas coincidan
        if (!usuario.getPassword().equals(usuario.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "error.confirmPassword", "Las contraseñas no coinciden.");
        }

        // Validación del formato del email
        if (!usuario.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.rejectValue("email", "error.email", "El formato del email es incorrecto.");
        }

        // Validación del teléfono (solo números)
        if (!usuario.getTelefono().matches("^\\+?[0-9]{9}$")) {
            errors.rejectValue("telefono", "error.telefono", "El formato del teléfono es incorrecto.");
        }

        // Validación de la longitud de la contraseña
        if (usuario.getPassword().length() < 8 || usuario.getPassword().length() > 20) {
            errors.rejectValue("password", "error.password", "La contraseña debe tener entre 8 y 20 caracteres.");
        }
    }
}

