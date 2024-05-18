package daos;

import models.Festival;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FestivalDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addFestival(Festival festival) {
        Integer maxId = jdbcTemplate.queryForObject("SELECT MAX(idfestival) FROM festival", Integer.class);

        if (maxId == null) {
            maxId = 0;
        }

        int nextId = maxId + 1;
        festival.setIdFestival(nextId);
        jdbcTemplate.update("INSERT INTO festival VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                festival.getIdFestival(), festival.getCifPromotor(), festival.getNom(), festival.getAnyo(),
                festival.getDataInici(), festival.getDataFi(), festival.getEstatFestival().name(),
                festival.getDescripcio(), festival.getCategoriaMusical(), festival.getPressupostContractacio(),
                festival.getAforamentMaxim(), festival.getLocalitzacioDescriptiva(), festival.getLocalitzacioGeografica(),
                festival.getPublicEnfocat(), festival.getRequisitMinimEdat(), festival.getDataIniciPublicacio(),
                festival.getDataIniciVenda());
    }

    public void deleteFestival(int idFestival) {
        jdbcTemplate.update("DELETE FROM festival WHERE idfestival = ?", idFestival);
    }

    public void updateFestival(Festival festival) {
        jdbcTemplate.update("UPDATE festival SET cif_promotor=?, nom=?, anyo=?, datainici=?, datafi=?, estatfestival=?, descripcio=?, " +
                        "categoriamusical=?, pressupostcontractacio=?, aforamentmaxim=?, localitzaciodescriptiva=?, localitzaciogeografica=?, " +
                        "publicenfocat=?, requisitminimedat=?, datainicipublicacio=?, datainicivenda=? WHERE idfestival=?",
                festival.getCifPromotor(), festival.getNom(), festival.getAnyo(), festival.getDataInici(), festival.getDataFi(),
                festival.getEstatFestival().name(), festival.getDescripcio(), festival.getCategoriaMusical(), festival.getPressupostContractacio(),
                festival.getAforamentMaxim(), festival.getLocalitzacioDescriptiva(), festival.getLocalitzacioGeografica(),
                festival.getPublicEnfocat(), festival.getRequisitMinimEdat(), festival.getDataIniciPublicacio(), festival.getDataIniciVenda(),
                festival.getIdFestival());
    }

    public List<Festival> getFestivals() {
        try {
            return jdbcTemplate.query("SELECT * FROM festival", new FestivalRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public Festival getFestival(int idFestival) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM festival WHERE idfestival=?", new FestivalRowMapper(), idFestival);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
