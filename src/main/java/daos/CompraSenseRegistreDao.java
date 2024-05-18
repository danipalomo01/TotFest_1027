package daos;

import models.CompraSenseRegistre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CompraSenseRegistreDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addCompraSenseRegistre(CompraSenseRegistre compra) {
        Integer maxId = jdbcTemplate.queryForObject("SELECT MAX(idCompra) FROM compra_sense_registre", Integer.class);

        if (maxId == null) {
            maxId = 0;
        }

        int nextId = maxId + 1;
        compra.setIdCompra(nextId);

        jdbcTemplate.update("INSERT INTO compra_sense_registre VALUES(?, ?, ?, ?, ?, ?, ?, ?)", compra.getIdCompra(), compra.getIdFestival(),
                compra.getData(), compra.getPreuCompra(), compra.getMail(), compra.getTelefon(), compra.getImportTotal(), compra.getDataCompra());
    }

    public void deleteCompraSenseRegistre(int idCompra) {
        jdbcTemplate.update("DELETE FROM compra_sense_registre WHERE idCompra = ?", idCompra);
    }

    public void updateCompraSenseRegistre(CompraSenseRegistre compra) {
        jdbcTemplate.update("UPDATE compra_sense_registre SET idFestival=?, data=?, preucompra=?, mail=?, telefon=?, importtotal=?, dataCompra=? WHERE idCompra=?",
                compra.getIdFestival(), compra.getData(), compra.getPreuCompra(), compra.getMail(), compra.getTelefon(), compra.getImportTotal(), compra.getDataCompra(), compra.getIdCompra());
    }

    public List<CompraSenseRegistre> getComprasSenseRegistre() {
        try {
            return jdbcTemplate.query("SELECT * FROM compra_sense_registre", new CompraSenseRegistreRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public CompraSenseRegistre getCompraSenseRegistre(int idCompra) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM compra_sense_registre WHERE idCompra = ?", new CompraSenseRegistreRowMapper(), idCompra);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}

