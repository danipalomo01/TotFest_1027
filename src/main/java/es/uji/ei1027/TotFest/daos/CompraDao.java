package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.Compra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CompraDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int addCompra(Compra compra) {
        Integer maxId = jdbcTemplate.queryForObject("SELECT MAX(idCompra) FROM compra", Integer.class);

        if (maxId == null) {
            maxId = 0;
        }

        int nextId = maxId + 1;
        compra.setIdcompra(nextId);

        jdbcTemplate.update("INSERT INTO compra VALUES(?, ?, ?, ?, ?)", compra.getIdcompra(),
                compra.getData(), compra.getPreucompra(), compra.getEmail(), compra.getTelefon());

        return nextId;
    }

    public void deleteCompra(int idCompra) {
        jdbcTemplate.update("DELETE FROM compra WHERE idCompra = ?", idCompra);
    }

    public void updateCompraSenseRegistre(Compra compra) {
        jdbcTemplate.update("UPDATE compra SET data=?, preucompra=?, email=?, telefon=? WHERE idCompra=?",
                compra.getData(), compra.getPreucompra(), compra.getEmail(), compra.getTelefon(), compra.getIdcompra());
    }

    public List<Compra> getCompras() {
        try {
            return jdbcTemplate.query("SELECT * FROM compra", new CompraRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public Compra getCompra(int idCompra) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM compra WHERE idCompra = ?", new CompraRowMapper(), idCompra);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}

