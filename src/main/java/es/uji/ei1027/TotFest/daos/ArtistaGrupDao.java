package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.Actuacio;
import es.uji.ei1027.TotFest.models.ArtistaGrup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ArtistaGrupDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addArtistaGrup(ArtistaGrup artistaGrup) {
        Integer maxId = jdbcTemplate.queryForObject("SELECT MAX(idartista) FROM artistagrup", Integer.class);

        if (maxId == null) {
            maxId = 0;
        }

        int nextId = maxId + 1;
        artistaGrup.setIdArtista(nextId);

        jdbcTemplate.update("INSERT into artistagrup VALUES(?, ?, ?, ?, ?)", artistaGrup.getIdArtista(), artistaGrup.getNom(),
                artistaGrup.getTipusMusica(), artistaGrup.getDescripcio(), artistaGrup.getCachetActual());
    }


    public void deleteArtistaGrup(int idArtistaGrup) {
        jdbcTemplate.update("DELETE FROM artistagrup WHERE idartista =?", idArtistaGrup);
    }

    public void updateArtistaGrup(ArtistaGrup artistaGrup) {
        jdbcTemplate.update("UPDATE artistagrup SET nom=?, tipusmusica=?, descripcio=?, cachetactual=? WHERE idartista=?", artistaGrup.getNom(),
                artistaGrup.getTipusMusica(), artistaGrup.getDescripcio(), artistaGrup.getCachetActual(), artistaGrup.getIdArtista());
    }

    public List<ArtistaGrup> getArtistaGrups() {
        try {
            return jdbcTemplate.query("SELECT * FROM artistagrup", new ArtistaGrupRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<ArtistaGrup>();
        }
    }

    public ArtistaGrup getArtistaGrup(int idArtista) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM artistagrup WHERE idartista=?", new ArtistaGrupRowMapper(), idArtista);
        } catch (EmptyResultDataAccessException e) {
            return new ArtistaGrup();
        }
    }
}
