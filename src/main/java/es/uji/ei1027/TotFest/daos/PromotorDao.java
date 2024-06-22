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

    public void addPromotor(Promotor promotor) {

        jdbcTemplate.update("INSERT INTO promotor VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                promotor.getCif(), promotor.getNomOrganisme(), promotor.getDomiciliFiscal(), promotor.getSector(),
                promotor.getDataAlta(), promotor.getDataBaixaRelacio(), promotor.getNomGestorFestivals(),
                promotor.getPwd(), promotor.getMailGestorFestActual(), promotor.getTelefonGestorFest(),
                promotor.getRazonSocial(), promotor.getTipusOrganisme());
    }

    public void deletePromotor(String cif) {
        jdbcTemplate.update("DELETE FROM promotor WHERE cif = ?", cif);
    }

    public void updatePromotor(Promotor promotor) {
        jdbcTemplate.update("UPDATE promotor SET nomorganisme=?, domicilifiscal=?, sector=?, dataalta=?, " +
                        "databaixarelacio=?, nomgestorfestivals=?, pwd=?, mailgestorfestactual=?, telefongestorfest=?, " +
                        "razonsocial=?, tipusorganisme=? WHERE cif=?",
                promotor.getNomOrganisme(), promotor.getDomiciliFiscal(), promotor.getSector(),
                promotor.getDataAlta(), promotor.getDataBaixaRelacio(), promotor.getNomGestorFestivals(),
                promotor.getPwd(), promotor.getMailGestorFestActual(), promotor.getTelefonGestorFest(),
                promotor.getRazonSocial(), promotor.getTipusOrganisme(), promotor.getCif());
    }

    public List<Promotor> getPromotores() {
        try {
            return jdbcTemplate.query("SELECT * FROM promotor", new PromotorRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public Promotor getPromotor(String cif) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM promotor WHERE cif=?", new PromotorRowMapper(), cif);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
