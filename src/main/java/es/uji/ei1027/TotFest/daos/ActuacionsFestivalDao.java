package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.ActuacionsFestival;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ActuacionsFestivalDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addActuacionsFestival(ActuacionsFestival actuacionsFestival) {
        jdbcTemplate.update("INSERT INTO actuacionesfestival (idFestival, idActuacion) VALUES(?, ?)",
                actuacionsFestival.getIdFestival(), actuacionsFestival.getIdActuacio());
    }

    public void deleteActuacionsFestival(int idFestival, int idActuacio) {
        jdbcTemplate.update("DELETE FROM actuacionesfestival WHERE idFestival = ? AND idActuacion = ?",
                idFestival, idActuacio);
    }

    public void updateActuacionsFestival(ActuacionsFestival actuacionsFestival) {
        jdbcTemplate.update("UPDATE actuacionesfestival SET idFestival = ?, idActuacion = ? WHERE idFestival = ? AND idActuacion = ?",
                actuacionsFestival.getIdFestival(), actuacionsFestival.getIdActuacio(),
                actuacionsFestival.getIdFestival(), actuacionsFestival.getIdActuacio());
    }

    public List<ActuacionsFestival> getActuacionsFestival() {
        try {
            return jdbcTemplate.query("SELECT * FROM actuacionesfestival", new ActuacionsFestivalRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<ActuacionsFestival>();
        }
    }

    public ActuacionsFestival getActuacionsFestival(int idFestival, int idActuacio) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM actuacionesfestival WHERE idFestival = ? AND idActuacion = ?",
                    new ActuacionsFestivalRowMapper(), idFestival, idActuacio);
        } catch (EmptyResultDataAccessException e) {
            return new ActuacionsFestival();
        }
    }
}
