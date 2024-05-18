package daos;

import models.ContracteArtista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContracteArtistaDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addContracteArtista(ContracteArtista contracte) {
        Integer maxId = jdbcTemplate.queryForObject("SELECT MAX(idcontracte) FROM contracteartista", Integer.class);

        if (maxId == null) {
            maxId = 0;
        }

        int nextId = maxId + 1;
        contracte.setIdContracte(nextId);

        jdbcTemplate.update("INSERT INTO contracteartista VALUES (?, ?, ?, ?, ?, ?, ?)", contracte.getIdContracte(), contracte.getIdArtista(),
                contracte.getDataInici(), contracte.getDataFi(), contracte.getCondicionsDescriptiu(), contracte.getNumActuacionsAny(), contracte.getImportContracte());
    }

    public void deleteContracteArtista(int idContracte) {
        jdbcTemplate.update("DELETE FROM contracteartista WHERE idcontracte = ?", idContracte);
    }

    public void updateContracteArtista(ContracteArtista contracte) {
        jdbcTemplate.update("UPDATE contracteartista SET idartista=?, datainici=?, datafi=?, condicionsdescriptiu=?, numactuacionsany=?, importcontracte=? WHERE idcontracte=?",
                contracte.getIdArtista(), contracte.getDataInici(), contracte.getDataFi(), contracte.getCondicionsDescriptiu(), contracte.getNumActuacionsAny(), contracte.getImportContracte(), contracte.getIdContracte());
    }

    public List<ContracteArtista> getContractesArtistes() {
        try {
            return jdbcTemplate.query("SELECT * FROM contracteartista", new ContracteArtistaRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public ContracteArtista getContracteArtista(int idContracte) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM contracteartista WHERE idcontracte = ?", new ContracteArtistaRowMapper(), idContracte);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
