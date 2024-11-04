package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsuarioDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsuarioDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Método para obtener todos los usuari con paginación
    public List<Usuario> getUsuarios(int size, int offset) {
        String sql = "SELECT * FROM usuari order by id LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{size, offset}, new UsuarioRowMapper());
    }

    // Método para obtener todos los usuari sin paginación
    public List<Usuario> getUsuarios() {
        String sql = "SELECT * FROM usuari order by id";
        return jdbcTemplate.query(sql, new UsuarioRowMapper());
    }

    // Método para obtener un usuario por su id
    public Usuario getUsuarioById(int id) {
        String sql = "SELECT * FROM usuari WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new UsuarioRowMapper());
    }

    // Método para agregar un nuevo usuario
    public void addUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuari (email, password, rol, telefono, nombre) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, usuario.getEmail(), usuario.getPassword(), usuario.getRol(), usuario.getTelefono(), usuario.getNombre());
    }

    // Método para actualizar un usuario existente
    public void updateUsuario(Usuario usuario) {
        String sql = "UPDATE usuari SET email = ?, password = ?, rol = ?, telefono = ?, nombre = ? WHERE id = ?";
        jdbcTemplate.update(sql, usuario.getEmail(), usuario.getPassword(), usuario.getRol(), usuario.getTelefono(), usuario.getNombre(), usuario.getId());
    }

    public boolean telefonoExists(String telefono) {
        String sql = "SELECT COUNT(*) FROM usuari WHERE telefono = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{telefono}, Integer.class);
        return count > 0;
    }

    // Método para eliminar un usuario por su id
    public void deleteUsuario(int id) {
        String sql = "DELETE FROM usuari WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Método para verificar si un email ya está registrado
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM usuari WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);
        return count != null && count > 0;
    }

    // Método para obtener un usuario por su email
    public Usuario getUsuarioByEmail(String email) {
        String sql = "SELECT * FROM usuari WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, new UsuarioRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;  // Si no se encuentra un usuario con ese email
        }
    }
}
