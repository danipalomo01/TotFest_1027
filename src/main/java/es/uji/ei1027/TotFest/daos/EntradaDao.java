package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.Entrada;
import es.uji.ei1027.TotFest.models.EntradaTipus;

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
public class EntradaDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addEntradaTipus(EntradaTipus entradaTipus) {
        jdbcTemplate.update("INSERT INTO entradatipus (entradatipus, idfestival, preu, descripcio, nombremaxim, datapertipusdia, percentatgemaximaforament) VALUES (?, ?, ?, ?, ?, ?, ?)",
                entradaTipus.getEntradaTipus().toString(),
                entradaTipus.getIdFestival(),
                entradaTipus.getPreu(),
                entradaTipus.getDescripcio(),
                entradaTipus.getNombreMaxim(),
                entradaTipus.getDataPerTipusDia(),
                entradaTipus.getPercentatgeMaximAforament());
    }

    public EntradaTipus getEntradaTipus(int idFestival, String entradatipus) {
        try {
            String sql = "SELECT * FROM entradatipus WHERE idfestival=? AND entradatipus=?";
            return jdbcTemplate.queryForObject(sql, new Object[]{idFestival, entradatipus}, new EntradaTipusRowMapper());
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

        jdbcTemplate.update("INSERT INTO entrada VALUES (?, ?, ?, ?, ?, ?, ?, ?)", entrada.getNumero(), entrada.getPreuVendaEntradaIndividual(), entrada.getData(), entrada.getIdFestival(), entrada.getDatacompra(), entrada.getEntradaTipus(), entrada.getEmail(), entrada.getTelefono());
    }

    public List<Entrada> getEntradas() {
        try {
            return jdbcTemplate.query("SELECT * FROM entrada order by numero", new EntradaRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<Entrada> getEntradas(int size, int page) {
        try {
            int offset = page * size;
            String sql = "SELECT * FROM entrada order by numero LIMIT ? OFFSET ?";
            return jdbcTemplate.query(sql, new EntradaRowMapper(), size, offset);
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

    public int getEntradesVenudesPerDiaDeDia(int idFestival, Date dataEntrada) {
        try {
            String sql = "SELECT COUNT(*) FROM entrada WHERE idfestival = ? AND datacompra = ? AND identradatipus=1";
            return jdbcTemplate.queryForObject(sql, new Object[]{idFestival, dataEntrada}, Integer.class);

        } catch (EmptyResultDataAccessException e) {
            return -1;
        }
    }

    public BigDecimal getPrecioDiaFestival(int idFestival) {
        try {
            String sql = "SELECT preu FROM entradatipus WHERE idfestival = ? AND entradatipus='dia'";
            return jdbcTemplate.queryForObject(sql, new Object[]{idFestival}, BigDecimal.class);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public BigDecimal getPrecioCompletoFestival(int idFestival) {
        try {
            String sql = "SELECT preu FROM entradatipus WHERE idfestival = ? AND entradatipus='festivalComplet'";
            return jdbcTemplate.queryForObject(sql, new Object[]{idFestival}, BigDecimal.class);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void updatePrecioEntradaTipus(int idFestival, BigDecimal precioDia, BigDecimal precioCompleto) {
        jdbcTemplate.update("UPDATE entradatipus SET preu=? WHERE idfestival=? AND entradatipus='dia'", precioDia, idFestival);
        jdbcTemplate.update("UPDATE entradatipus SET preu=? WHERE idfestival=? AND entradatipus='festivalComplet'", precioCompleto, idFestival);

    }

    public List<Entrada> getEntradasByFestival(int idFestival, int page, int size) {
        int offset = page * size;
        String sql = "SELECT * FROM entrada WHERE idfestival = ? order by numero LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new EntradaRowMapper(), idFestival, size, offset);
    }

    public List<Entrada> getEntradasByFestivalAndTipo(int idFestival, int tipoEntrada, int page, int size) {
        int offset = page * size;
        String sql = "SELECT * FROM entrada WHERE idfestival = ? AND identradatipus = ? order by numero LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new EntradaRowMapper(), idFestival, tipoEntrada, size, offset);
    }

    public int getTotalPagesByFestival(int idFestival, int size) {
        String sql = "SELECT COUNT(*) FROM entrada WHERE idfestival = ?";
        int totalEntries = jdbcTemplate.queryForObject(sql, Integer.class, idFestival);
        return (int) Math.ceil(totalEntries / (double) size);
    }

    public int getTotalPagesByFestivalAndTipo(int idFestival, int tipoEntrada, int size) {
        String sql = "SELECT COUNT(*) FROM entrada WHERE idfestival = ? AND identradatipus = ?";
        int totalEntries = jdbcTemplate.queryForObject(sql, Integer.class, idFestival, tipoEntrada);
        return (int) Math.ceil(totalEntries / (double) size);
    }

    public int getNumTotalEntradasFestival(int idFestival) {
        String sql = "SELECT COUNT(*) FROM entrada WHERE idfestival = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, idFestival);
    }

    public int getNumTotalEntradasFestivalPorTipo(int idFestival, int tipoEntrada) {
        String sql = "SELECT COUNT(*) FROM entrada WHERE idfestival = ? AND identradatipus = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, idFestival, tipoEntrada);
    }


    public List<Entrada> getEntradasByUsuario(String email, int page, int size) {
        int offset = page * size;
        String sql = "SELECT * FROM entrada WHERE email = ? order by numero LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new EntradaRowMapper(), email, size, offset);
    }

    public List<Entrada> getEntradasByUsuarioAndTipo(String email, int tipoEntrada, int page, int size) {
        int offset = page * size;
        String sql = "SELECT * FROM entrada WHERE email = ? AND identradatipus = ? order by numero LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new EntradaRowMapper(), email, tipoEntrada, size, offset);
    }

    public int getTotalPagesByUsuario(String email, int size) {
        String sql = "SELECT COUNT(*) FROM entrada WHERE email = ?";
        int totalEntries = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return (int) Math.ceil(totalEntries / (double) size);
    }

    public int getTotalPagesByUsuarioAndTipo(String email, int tipoEntrada, int size) {
        String sql = "SELECT COUNT(*) FROM entrada WHERE email = ? AND identradatipus = ?";
        int totalEntries = jdbcTemplate.queryForObject(sql, Integer.class, email, tipoEntrada);
        return (int) Math.ceil(totalEntries / (double) size);
    }

    public int getNumTotalEntradasUsuario(String email) {
        String sql = "SELECT COUNT(*) FROM entrada WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, email);
    }

    public int getNumTotalEntradasUsuarioPorTipo(String email, int tipoEntrada) {
        String sql = "SELECT COUNT(*) FROM entrada WHERE email = ? AND identradatipus = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, email, tipoEntrada);
    }

    public int getNumTotalEntradasByFestival(String email, String buscarFestival) {
        String sql = "SELECT COUNT(*) FROM entrada e JOIN festival f ON e.idfestival = f.idfestival WHERE e.email = ? AND LOWER(f.nom) LIKE LOWER(?)";
        return jdbcTemplate.queryForObject(sql, new Object[]{email, "%" + buscarFestival + "%"}, Integer.class);
    }

    public List<Entrada> getEntradasByUsuarioAndFestival(String email, String buscarFestival, int page, int size) {
        String sql = "SELECT e.* FROM entrada e JOIN festival f ON e.idfestival = f.idfestival WHERE e.email = ? AND LOWER(f.nom) LIKE LOWER(?) order by numero LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{email, "%" + buscarFestival + "%", size, page * size}, new EntradaRowMapper());
    }

    public void deleteEntrada(int numeroEntrada) {
        String sql = "DELETE FROM entrada WHERE numero = ?";
        jdbcTemplate.update(sql, numeroEntrada);
    }


}
