package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.Promotor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PromotorDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Método para añadir un promotor
    public void addPromotor(Promotor promotor) {
        jdbcTemplate.update("INSERT INTO promotor (cif, nomorganisme, domicilifiscal, sector, dataalta, databaixarelaciocomercial, datainicigestorfest) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                promotor.getCif(), promotor.getNomOrganisme(), promotor.getDomiciliFiscal(), promotor.getSector(),
                promotor.getDataAlta(), promotor.getDataBaixaRelacioComercial(), promotor.getDatainiciGestorFest());
    }

    // Método para borrar un promotor por su CIF
    public void deletePromotor(int id) {
        jdbcTemplate.update("DELETE FROM promotor WHERE id = ?", id);
    }

    // Método para actualizar los datos de un promotor
    public void updatePromotor(Promotor promotor) {
        jdbcTemplate.update("UPDATE promotor SET nomorganisme=?, domicilifiscal=?, sector=?, dataalta=?, " +
                        "databaixarelaciocomercial=?, datainicigestorfest=? WHERE id=?",
                promotor.getNomOrganisme(), promotor.getDomiciliFiscal(), promotor.getSector(),
                promotor.getDataAlta(), promotor.getDataBaixaRelacioComercial(), promotor.getDatainiciGestorFest(),
                promotor.getId());
    }

    // Obtener la lista de promotores con paginación
    public List<Promotor> getPromotores(int size, int page) {
        int offset = page * size;
        try {
            return jdbcTemplate.query("SELECT * FROM promotor ORDER BY id LIMIT ? OFFSET ?",
                    new PromotorRowMapper(), size, offset);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<Promotor> getPromotores() {
        try {
            return jdbcTemplate.query("SELECT * FROM promotor ORDER BY id",
                    new PromotorRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    // Obtener el total de promotores
    public int getTotalPromotores() {
        String sql = "SELECT COUNT(*) FROM promotor";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    // Obtener un promotor por su CIF
    public Promotor getPromotor(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM promotor WHERE id=?", new PromotorRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Obtener el número total de páginas para los promotores
    public int getTotalPages(int size) {
        int totalPromotores = getTotalPromotores();
        return (int) Math.ceil(totalPromotores / (double) size);
    }
}
