package daos;

import models.Actuacio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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

        jdbcTemplate.update("INSERT into actuacio VALUES(?, ?, ?, ?, ?, ?, ?)", actuacio.getIdActuacio(), actuacio.getIdContracte(),
                actuacio.getData(), actuacio.getHoraInici(), actuacio.getHoraFiPrevista(), actuacio.getPreuContracteActuacio(), actuacio.getComentaris());
    }


    public void deleteActuacio(int idActuacio) {
        jdbcTemplate.update("DELETE FROM actuacio WHERE idactuacio =?", idActuacio);
    }

    public void updateActuacio(Actuacio actuacio){
        jdbcTemplate.update("UPDATE actuacio SET idcontracte=?, data=?, horainici=?, horafiprevista=?, preucontracteactuacio=?, comentaris=? WHERE idactuacio=?",actuacio.getIdContracte(),
                actuacio.getData(), actuacio.getHoraInici(), actuacio.getHoraFiPrevista(), actuacio.getPreuContracteActuacio(), actuacio.getComentaris(), actuacio.getIdActuacio());
    }

    public List<Actuacio> getActuacions(){
        try {
            return jdbcTemplate.query("SELECT * FROM actuacio", new ActuacioRowMapper());
        }
        catch (EmptyResultDataAccessException e){
            return new ArrayList<Actuacio>();
        }
    }
    public Actuacio getActuacio(int idActuacio) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM actuacio WHERE idactuacio=?", new ActuacioRowMapper(), idActuacio);
        }
        catch(EmptyResultDataAccessException e) {
            return new Actuacio();
        }
    }

}

