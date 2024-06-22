package es.uji.ei1027.TotFest.controllers;

import es.uji.ei1027.TotFest.models.UserDetails;
import org.springframework.validation.Errors;

public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return UserDetails.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        UserDetails user = (UserDetails) obj;
        if(user.getUsername().trim().equals(""))
            errors.rejectValue("username", "obligatorio",
                    "Hay que introducir un valor");
        if(user.getPassword().trim().equals(""))
            errors.rejectValue("password", "obligatorio",
                                "Hay que introducir una contraseña");
    }
}
