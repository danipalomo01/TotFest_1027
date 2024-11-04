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

    // Añadir un nuevo tipo de entrada
    public void addEntradaTipus(EntradaTipus entradaTipus) {
        jdbcTemplate.update("INSERT INTO entradatipus (entradatipus, idfestival, preu, descripcio, percentatgemaximaforament) " +
                        "VALUES (?, ?, ?, ?, ?)",
                entradaTipus.getEntradaTipus().getValue(),
                entradaTipus.getIdFestival(),
                entradaTipus.getPreu(),
                entradaTipus.getDescripcio(),
                entradaTipus.getPercentatgeMaximAforament());
    }

    // Obtener tipo de entrada por idFestival y tipo
    public EntradaTipus getEntradaTipus(int idFestival, int entradatipus) {
        try {
            String sql = "SELECT * FROM entradatipus WHERE idfestival=? AND entradatipus=?";
            return jdbcTemplate.queryForObject(sql, new Object[]{idFestival, entradatipus}, new EntradaTipusRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public EntradaTipus getEntradaTipus(int identradatipus) {
        try {
            String sql = "SELECT * FROM entradatipus WHERE id=?";
            return jdbcTemplate.queryForObject(sql, new Object[]{identradatipus}, new EntradaTipusRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void addDevolucio(int numeroEntrada) {
        jdbcTemplate.update("INSERT INTO devolucio (numero) " +
                        "VALUES (?)",
                numeroEntrada);
    }

    // Añadir nueva entrada
    public void addEntrada(Entrada entrada) {
        Integer maxId = jdbcTemplate.queryForObject("SELECT MAX(numero) FROM entrada", Integer.class);

        if (maxId == null) {
            maxId = 0;
        }

        int nextId = maxId + 1;
        entrada.setNumero(nextId);

        jdbcTemplate.update("INSERT INTO entrada (numero, data, identradatipus, idcompra) " +
                        "VALUES (?, ?, ?, ?)",
                entrada.getNumero(), entrada.getData(),
                entrada.getEntradaTipus(), entrada.getIdcompra());
    }

    // Obtener todas las entradas
    public List<Entrada> getEntradas() {
        try {
            String sql = "SELECT e.numero, et.preu, e.data, e.identradatipus, e.idcompra, c.data " +
                    "FROM entrada e " +
                    "JOIN entradatipus et ON e.identradatipus = et.id " +
                    "JOIN compra c ON e.idcompra = c.idcompra " +
                    "ORDER BY e.numero";
            return jdbcTemplate.query(sql, new EntradaRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    // Obtener entradas paginadas
    public List<Entrada> getEntradas(int size, int page) {
        try {
            int offset = page * size;
            String sql = "SELECT e.numero, et.preu, e.data, e.identradatipus, e.idcompra, c.data " +
                    "FROM entrada e " +
                    "JOIN entradatipus et ON e.identradatipus = et.id " +
                    "JOIN compra c ON e.idcompra = c.idcompra " +
                    "ORDER BY e.numero LIMIT ? OFFSET ?";
            return jdbcTemplate.query(sql, new EntradaRowMapper(), size, offset);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    // Obtener entrada por número
    public Entrada getEntrada(int numero) {
        try {
            String sql = "SELECT e.numero, et.preu, e.data, e.identradatipus, e.idcompra, c.data " +
                    "FROM entrada e " +
                    "JOIN entradatipus et ON e.identradatipus = et.id " +
                    "JOIN compra c ON e.idcompra = c.idcompra " +
                    "WHERE e.numero = ?";
            return jdbcTemplate.queryForObject(sql, new EntradaRowMapper(), numero);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Obtener número de entradas vendidas por día
    public int getEntradesVenudesPerDiaDeDia(int idFestival, Date dataEntrada) {
        try {
            String sql = "SELECT COUNT(*) FROM entrada e " +
                    "JOIN entradatipus et ON e.identradatipus = et.id " +
                    "JOIN compra c ON e.idcompra = c.idcompra " +
                    "WHERE et.entradatipus = 1 AND et.idfestival = ? AND c.data = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{idFestival, dataEntrada}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return -1;
        }
    }

    // Obtener precio de un día del festival
    public BigDecimal getPrecioDiaFestival(int idFestival) {
        try {
            String sql = "SELECT preu FROM entradatipus WHERE idfestival = ? AND entradatipus = 1";
            return jdbcTemplate.queryForObject(sql, new Object[]{idFestival}, BigDecimal.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Obtener precio completo del festival
    public BigDecimal getPrecioCompletoFestival(int idFestival) {
        try {
            String sql = "SELECT preu FROM entradatipus WHERE idfestival = ? AND entradatipus = 2";
            return jdbcTemplate.queryForObject(sql, new Object[]{idFestival}, BigDecimal.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Actualizar precios de las entradas por tipo
    public void updatePrecioEntradaTipus(int idFestival, BigDecimal precioDia, BigDecimal precioCompleto) {
        jdbcTemplate.update("UPDATE entradatipus SET preu=? WHERE idfestival=? AND entradatipus='dia'", precioDia, idFestival);
        jdbcTemplate.update("UPDATE entradatipus SET preu=? WHERE idfestival=? AND entradatipus='festivalComplet'", precioCompleto, idFestival);
    }

    public void updateEntradaTipus(EntradaTipus entradaTipus) {
        String sql = "UPDATE entradatipus SET entradatipus = ?, preu = ?, descripcio = ?, percentatgemaximaforament = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sql,
                entradaTipus.getEntradaTipus().getValue(),  // Actualiza el tipo de entrada
                entradaTipus.getPreu(),                     // Actualiza el precio
                entradaTipus.getDescripcio(),               // Actualiza la descripción
                entradaTipus.getPercentatgeMaximAforament(),// Actualiza el porcentaje máximo de aforo
                entradaTipus.getId()                        // Usa el ID para identificar el registro a actualizar
        );
    }

    // Obtener entradas por festival
    public List<Entrada> getEntradasByFestival(int idFestival, int page, int size) {
        int offset = page * size;
        String sql = "SELECT e.numero, et.preu, e.data, e.identradatipus, e.idcompra, c.data " +
                "FROM entrada e " +
                "JOIN entradatipus et ON e.identradatipus = et.id " +
                "JOIN compra c ON e.idcompra = c.idcompra " +
                "WHERE et.idfestival = ? ORDER BY e.numero LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new EntradaRowMapper(), idFestival, size, offset);
    }

    // Obtener entradas por festival y tipo
    public List<Entrada> getEntradasByFestivalAndTipo(int idFestival, int tipoEntrada, int page, int size) {
        int offset = page * size;
        String sql = "SELECT e.numero, et.preu, e.data, e.identradatipus, e.idcompra, c.data " +
                "FROM entrada e " +
                "JOIN entradatipus et ON e.identradatipus = et.id " +
                "JOIN compra c ON e.idcompra = c.idcompra " +
                "WHERE et.idfestival = ? AND et.entradatipus = ? ORDER BY e.numero LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new EntradaRowMapper(), idFestival, tipoEntrada, size, offset);
    }

    // Obtener número total de páginas por festival
    public int getTotalPagesByFestival(int idFestival, int size) {
        String sql = "SELECT COUNT(*) FROM entrada e " +
                "JOIN entradatipus et ON e.identradatipus = et.id " +
                "WHERE et.idfestival = ?";
        int totalEntries = jdbcTemplate.queryForObject(sql, Integer.class, idFestival);
        return (int) Math.ceil(totalEntries / (double) size);
    }

    // Obtener número total de páginas por festival y tipo
    public int getTotalPagesByFestivalAndTipo(int idFestival, int tipoEntrada, int size) {
        String sql = "SELECT COUNT(*) FROM entrada e " +
                "JOIN entradatipus et ON e.identradatipus = et.id " +
                "WHERE et.idfestival = ? AND et.entradatipus = ?";
        int totalEntries = jdbcTemplate.queryForObject(sql, Integer.class, idFestival, tipoEntrada);
        return (int) Math.ceil(totalEntries / (double) size);
    }

    // Obtener número total de entradas por festival
    public int getNumTotalEntradasFestival(int idFestival) {
        String sql = "SELECT COUNT(*) FROM entrada e " +
                "JOIN entradatipus et ON e.identradatipus = et.id " +
                "WHERE et.idfestival = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, idFestival);
    }

    // Obtener número total de entradas por festival y tipo
    public int getNumTotalEntradasFestivalPorTipo(int idFestival, int tipoEntrada) {
        String sql = "SELECT COUNT(*) FROM entrada e " +
                "JOIN entradatipus et ON e.identradatipus = et.id " +
                "WHERE et.idfestival = ? AND e.identradatipus = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, idFestival, tipoEntrada);
    }

    // Obtener entradas por usuario
    public List<Entrada> getEntradasByUsuario(String email, int page, int size) {
        int offset = page * size;
        String sql = "SELECT e.numero, et.preu, e.data, e.identradatipus, e.idcompra, c.data " +
                "FROM entrada e " +
                "JOIN entradatipus et ON e.identradatipus = et.id " +
                "JOIN compra c ON e.idcompra = c.idcompra " +
                "WHERE c.email = ? ORDER BY e.numero LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new EntradaRowMapper(), email, size, offset);
    }

    // Obtener entradas por usuario y tipo
    public List<Entrada> getEntradasByUsuarioAndTipo(String email, int tipoEntrada, int page, int size) {
        int offset = page * size;
        String sql = "SELECT e.numero, et.preu, e.data, e.identradatipus, e.idcompra, c.data " +
                "FROM entrada e " +
                "JOIN entradatipus et ON e.identradatipus = et.id " +
                "JOIN compra c ON e.idcompra = c.idcompra " +
                "WHERE c.email = ? AND et.entradatipus = ? ORDER BY e.numero LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new EntradaRowMapper(), email, tipoEntrada, size, offset);
    }

    // Obtener número total de entradas por usuario
    public int getNumTotalEntradasUsuario(String email) {
        String sql = "SELECT COUNT(*) FROM entrada e " +
                "JOIN entradatipus et ON e.identradatipus = et.id " +
                "JOIN compra c ON e.idcompra = c.idcompra " +
                "WHERE c.email = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, email);
    }

    // Obtener número total de páginas por usuario
    public int getTotalPagesByUsuario(String email, int size) {
        String sql = "SELECT COUNT(*) FROM entrada e " +
                "JOIN entradatipus et ON e.identradatipus = et.id " +
                "JOIN compra c ON e.idcompra = c.idcompra " +
                "WHERE c.email = ?";
        int totalEntries = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return (int) Math.ceil(totalEntries / (double) size);
    }

    // Obtener número total de páginas por usuario y tipo
    public int getTotalPagesByUsuarioAndTipo(String email, int tipoEntrada, int size) {
        String sql = "SELECT COUNT(*) FROM entrada e " +
                "JOIN entradatipus et ON e.identradatipus = et.id " +
                "JOIN compra c ON e.idcompra = c.idcompra " +
                "WHERE c.email = ? AND et.entradatipus = ?";
        int totalEntries = jdbcTemplate.queryForObject(sql, Integer.class, email, tipoEntrada);
        return (int) Math.ceil(totalEntries / (double) size);
    }

    // Obtener número total de entradas por usuario y tipo
    public int getNumTotalEntradasUsuarioPorTipo(String email, int tipoEntrada) {
        String sql = "SELECT COUNT(*) FROM entrada e " +
                "JOIN entradatipus et ON e.identradatipus = et.id " +
                "JOIN compra c ON e.idcompra = c.idcompra " +
                "WHERE c.email = ? AND e.identradatipus = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, email, tipoEntrada);
    }

    // Obtener entradas por usuario y festival
    public List<Entrada> getEntradasByUsuarioAndFestival(String email, String buscarFestival, int page, int size) {
        String sql = "SELECT e.numero, et.preu, e.data, e.identradatipus, e.idcompra, c.data " +
                "FROM entrada e " +
                "JOIN compra c ON e.idcompra = c.idcompra " +
                "JOIN entradatipus et ON e.identradatipus = et.id " +  // Añadimos el JOIN con entradatipus
                "JOIN festival f ON et.idfestival = f.idfestival " +
                "WHERE c.email = ? AND LOWER(f.nom) LIKE LOWER(?) " +
                "ORDER BY e.numero LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new EntradaRowMapper(), email, "%" + buscarFestival + "%", size, page * size);
    }

    // Obtener número total de entradas por usuario y festival
    public int getNumTotalEntradasByFestival(String email, String buscarFestival) {
        String sql = "SELECT COUNT(*) FROM entrada e " +
                "JOIN compra c ON e.idcompra = c.idcompra " +
                "JOIN entradatipus et ON e.identradatipus = et.id " + // Aquí añadimos el JOIN con entradatipus
                "JOIN festival f ON et.idfestival = f.idfestival " +
                "WHERE c.email = ? AND LOWER(f.nom) LIKE LOWER(?)";
        return jdbcTemplate.queryForObject(sql, new Object[]{email, "%" + buscarFestival + "%"}, Integer.class);
    }

    // Eliminar una entrada por su número
    public void deleteEntrada(int numeroEntrada) {
        String sql = "DELETE FROM entrada WHERE numero = ?";
        jdbcTemplate.update(sql, numeroEntrada);
    }
}
