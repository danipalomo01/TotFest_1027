package daos;

import models.Entrada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EntradaDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addEntrada(Entrada entrada) {
        Integer maxId = jdbcTemplate.queryForObject("SELECT MAX(numero) FROM entrada", Integer.class);

        if (maxId == null) {
            maxId = 0;
        }

        int nextId = maxId + 1;
        entrada.setNumero(nextId);

        jdbcTemplate.update("INSERT INTO entrada VALUES (?, ?, ?)", entrada.getNumero(), entrada.getIdEntradaTipus(), entrada.getPreuVendaEntradaIndividual());
    }

    public void deleteEntrada(int numero) {
        jdbcTemplate.update("DELETE FROM entrada WHERE numero = ?", numero);
    }

    public void updateEntrada(Entrada entrada) {
        jdbcTemplate.update("UPDATE entrada SET identradatipus=?, preuvendaentradaindividual=? WHERE numero=?",
                entrada.getIdEntradaTipus(), entrada.getPreuVendaEntradaIndividual(), entrada.getNumero());
    }

    public List<Entrada> getEntradas() {
        try {
            return jdbcTemplate.query("SELECT * FROM entrada", new EntradaRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public Entrada getEntrada(int numero) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM entrada WHERE numero=?", new EntradaRowMapper(), numero);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
