package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.Actuacio;
import es.uji.ei1027.TotFest.models.ContracteArtista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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

        jdbcTemplate.update("INSERT INTO contracteartista VALUES (?, ?, ?, ?, ?, ?, ?, ?)", contracte.getIdContracte(), contracte.getIdArtista(),
                contracte.getDataInici(), contracte.getDataFi(), contracte.getCondicionsDescriptiu(), contracte.getNumActuacionsAny(), contracte.getImportContracte(), contracte.getIdComercial());
    }

    public void deleteContracteArtista(int idContracte) {
        jdbcTemplate.update("DELETE FROM contracteartista WHERE idcontracte = ?", idContracte);
    }

    public void updateContracteArtista(ContracteArtista contracte) {
        jdbcTemplate.update("UPDATE contracteartista SET idartista=?, datainici=?, datafi=?, condicionsdescriptiu=?, numactuacionsany=?, importcontracte=?, idgestorcomercial=? WHERE idcontracte=?",
                contracte.getIdArtista(), contracte.getDataInici(), contracte.getDataFi(), contracte.getCondicionsDescriptiu(), contracte.getNumActuacionsAny(), contracte.getImportContracte(), contracte.getIdComercial(), contracte.getIdContracte());
    }

    public List<ContracteArtista> getContractesArtistes() {
        try {
            return jdbcTemplate.query("SELECT * FROM contracteartista order by idcontracte", new ContracteArtistaRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<ContracteArtista> getContractesArtistes(int page, int size) {
        int offset = page * size;

        return jdbcTemplate.query("SELECT * FROM contracteartista order by idcontracte LIMIT ? OFFSET ?", new ContracteArtistaRowMapper(), size, offset);

    }

    public ContracteArtista getContracteArtista(int idContracte) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM contracteartista WHERE idcontracte = ?", new ContracteArtistaRowMapper(), idContracte);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int getContractesArtista(int idArtista) {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM contracteartista WHERE idartista = ?", Integer.class, idArtista);
        } catch (EmptyResultDataAccessException e) {
            return -1;
        }
    }

    public List<Actuacio> getActuacionesContrato(int idContracte) {
        try {
            String sql = "SELECT * FROM actuacio WHERE idcontracte=? order by idcontracte";
            return jdbcTemplate.query(sql, new ActuacioRowMapper(), idContracte);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public int getNumActuacionesContrato(int idContracte) {
        try {
            String sql = "SELECT numactuacionsany FROM contracteartista WHERE idcontracte=?";
            return jdbcTemplate.queryForObject(sql, Integer.class, idContracte);
        } catch (Exception e) {
            return -1;
        }
    }

    public Boolean contratoCerrado(int idcontrato, int numActuacionsAny) {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) as count FROM actuacio WHERE idcontracte = ?", Integer.class, idcontrato) == numActuacionsAny;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<ContracteArtista> getContractesArtista(int page, int size, int idArtista) {

        int offset = page * size;
        return jdbcTemplate.query("SELECT * FROM contracteartista WHERE idArtista = ? order by idcontracte LIMIT ? OFFSET ?", new ContracteArtistaRowMapper(), idArtista, size, offset);
    }
}
