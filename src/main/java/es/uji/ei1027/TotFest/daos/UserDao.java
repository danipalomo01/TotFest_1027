package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.UserDetails;

import java.util.Collection;

public interface UserDao {
    UserDetails loadUserByUsername(String username, String password);
    Collection<UserDetails> listAllUsers();
}
