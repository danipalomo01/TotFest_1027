package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.Actuacio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ActuacioDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addActuacio(Actuacio actuacio) {
        Integer maxId = jdbcTemplate.queryForObject("SELECT MAX(idActuacio) FROM actuacio", Integer.class);

        if (maxId == null) {
            maxId = 0;
        }

        int nextId = maxId + 1;
        actuacio.setIdActuacio(nextId);

        jdbcTemplate.update("INSERT INTO actuacio VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                actuacio.getIdActuacio(),
                actuacio.getIdContracte(),
                actuacio.getData(),
                actuacio.getHoraInici(),
                actuacio.getHoraFiPrevista(),
                actuacio.getPreuContracteActuacio(),
                actuacio.getComentaris(),
                actuacio.getIdFestival()
        );
    }

    public void deleteActuacio(int idActuacio) {
        jdbcTemplate.update("DELETE FROM actuacio WHERE idActuacio = ?", idActuacio);
    }

    public void updateActuacio(Actuacio actuacio) {
        jdbcTemplate.update("UPDATE actuacio SET idContracte = ?, data = ?, horaInici = ?, horaFiPrevista = ?, preuContracteActuacio = ?, comentaris = ?, idFestival = ? WHERE idActuacio = ?",
                actuacio.getIdContracte(),
                actuacio.getData(),
                actuacio.getHoraInici(),
                actuacio.getHoraFiPrevista(),
                actuacio.getPreuContracteActuacio(),
                actuacio.getComentaris(),
                actuacio.getIdFestival(),
                actuacio.getIdActuacio()
        );
    }

    public List<Actuacio> getActuacions() {
        try {
            return jdbcTemplate.query("SELECT * FROM actuacio order by idactuacio", new ActuacioRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public Actuacio getActuacio(int idActuacio) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM actuacio WHERE idActuacio = ?", new ActuacioRowMapper(), idActuacio);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean horasActuacionesSeSolapan(Date fecha, Time horaInicio, Time horaFinPrevista, int idArtista) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM actuacio WHERE data = ? AND idartista = ? AND (" +
                "(horaInici = ? OR horaFiPrevista = ?) OR " +  // Caso donde las horas de inicio o fin son iguales
                "(? >= horaInici AND ? < horaFiPrevista) OR " +  // Caso donde horaInicio está dentro del intervalo
                "(? > horaInici AND ? <= horaFiPrevista) OR " +  // Caso donde horaFinPrevista está dentro del intervalo
                "(horaInici >= ? AND horaInici < ?) OR " +       // Caso donde el inicio de la otra actuación está dentro del intervalo
                "(horaFiPrevista > ? AND horaFiPrevista <= ?)" + // Caso donde el fin de la otra actuación está dentro del intervalo
                ")";

        return jdbcTemplate.queryForObject(sql, new Object[]{
                fecha, idArtista, horaInicio, horaFinPrevista, // Para el caso donde las horas de inicio o fin son iguales
                horaInicio, horaInicio,                       // Para el caso donde horaInicio está dentro del intervalo
                horaFinPrevista, horaFinPrevista,             // Para el caso donde horaFinPrevista está dentro del intervalo
                horaInicio, horaFinPrevista,                  // Para el caso donde el inicio de la otra actuación está dentro del intervalo
                horaInicio, horaFinPrevista                   // Para el caso donde el fin de la otra actuación está dentro del intervalo
        }, Integer.class) > 0;
    }

    public List<Actuacio> getActuacionsDia(int idFestival, Date date) {
        try {
            String sql = "SELECT * FROM actuacio WHERE idfestival=? AND data=? order by idactuacio";
            return jdbcTemplate.query(sql, new Object[]{idFestival, date}, new ActuacioRowMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<Actuacio> getActuacionsFestival(int idFestival) {
        try {
            String sql = "SELECT * FROM actuacio WHERE idfestival=? order by idactuacio";
            return jdbcTemplate.query(sql, new Object[]{idFestival}, new ActuacioRowMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
