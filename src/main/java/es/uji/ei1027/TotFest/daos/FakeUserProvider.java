package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.UserDetails;
import es.uji.ei1027.TotFest.models.UserType;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FakeUserProvider implements UserDao {
    final Map<String, UserDetails> knownUsers = new HashMap<>();

    final Map<String, UserType> userTypes = new HashMap<>();

    public FakeUserProvider() {
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();

        UserDetails userLaura = new UserDetails();
        userLaura.setUsername("laura");
        userLaura.setPassword(passwordEncryptor.encryptPassword("laura"));
        userLaura.setUserType(UserType.GESTOR_FESTIVALES);
        ArrayList<String> extraInfo = new ArrayList<>();
        extraInfo.add("A12345678");
        userLaura.setUserExtraInfo(extraInfo);
        knownUsers.put("laura", userLaura);
        userTypes.put("laura", UserType.GESTOR_FESTIVALES);

        UserDetails userBob = new UserDetails();
        userBob.setUsername("bob");
        userBob.setPassword(passwordEncryptor.encryptPassword("bob"));
        userBob.setUserType(UserType.RESPONSABLE_CONTRATACION);
        ArrayList<String> extraInfo2 = new ArrayList<>();
        extraInfo2.add("1");
        userBob.setUserExtraInfo(extraInfo2);
        knownUsers.put("bob", userBob);
        userTypes.put("bob", UserType.RESPONSABLE_CONTRATACION);
    }

    @Override
    public UserDetails loadUserByUsername(String username, String password) {
        UserDetails user = knownUsers.get(username.trim());
        if (user == null) {
            return null; // Usuario no encontrado
        }

        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        if (passwordEncryptor.checkPassword(password, user.getPassword())) {
            return user;
        } else {
            return null; // Credenciales incorrectas
        }
    }

    @Override
    public Collection<UserDetails> listAllUsers() {
        return knownUsers.values();
    }

    public UserType getUserTypeByUsername(String username) {
        return userTypes.get(username);
    }
}
