package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.Entrada;
import es.uji.ei1027.TotFest.models.EntradaTipus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EntradaDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addEntradaTipus(EntradaTipus entradaTipus) {
        jdbcTemplate.update("INSERT INTO entradatipus (entradatipus, idfestival, tipusentrada, preu, descripcio, nombremaxim, nombrevendes, datapertipusdia, percentatgemaximaforament) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                entradaTipus.getEntradaTipus().toString(),
                entradaTipus.getIdFestival(),
                entradaTipus.getEntradaTipus().toString(),
                entradaTipus.getPreu(),
                entradaTipus.getDescripcio(),
                entradaTipus.getNombreMaxim(),
                entradaTipus.getNombreVendes(),
                entradaTipus.getDataPerTipusDia(),
                entradaTipus.getPercentatgeMaximAforament());
    }

    public EntradaTipus getEntradaTipus(int idFestival, String tipusEntrada) {
        try {
            String sql = "SELECT * FROM entradatipus WHERE idfestival=? AND tipusentrada=?";
            return jdbcTemplate.queryForObject(sql, new Object[]{idFestival, tipusEntrada}, new EntradaTipusRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void addEntrada(Entrada entrada) {
        Integer maxId = jdbcTemplate.queryForObject("SELECT MAX(numero) FROM entrada", Integer.class);

        if (maxId == null) {
            maxId = 0;
        }

        int nextId = maxId + 1;
        entrada.setNumero(nextId);

        jdbcTemplate.update("INSERT INTO entrada VALUES (?, ?, ?, ?)", entrada.getNumero(), entrada.getPreuVendaEntradaIndividual(), entrada.getData(), entrada.getIdFestival());
    }

    public void addNumEntradasVendidasEntradaTipus(int idfestival, String tipusEntrada, int num) {
        jdbcTemplate.update("UPDATE entradatipus SET nombrevendes=nombrevendes + ? WHERE idfestival=? AND tipusentrada=?", num, idfestival, tipusEntrada);
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

    public int getEntradesVenudesPerDia(int idFestival, Date dataEntrada) {
        try {
            String sql = "SELECT COUNT(*) FROM entrada WHERE idfestival = ? AND data = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{idFestival, dataEntrada}, Integer.class);

        } catch (EmptyResultDataAccessException e) {
            return -1;
        }
    }
}
