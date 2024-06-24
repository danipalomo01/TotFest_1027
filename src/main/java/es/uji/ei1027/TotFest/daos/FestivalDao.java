package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.Actuacio;
import es.uji.ei1027.TotFest.models.Festival;
import es.uji.ei1027.TotFest.models.EstatFestivalEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class FestivalDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addFestival(Festival festival, HttpSession session) {
        Integer maxId = jdbcTemplate.queryForObject("SELECT MAX(idfestival) FROM festival", Integer.class);

        if (maxId == null) {
            maxId = 0;
        }

        int nextId = maxId + 1;
        festival.setIdFestival(nextId);
        updateFestivalState(festival);
        festival.setCifPromotor(session.getAttribute("cif").toString());
        jdbcTemplate.update("INSERT INTO festival VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                festival.getIdFestival(), festival.getCifPromotor(), festival.getNom(), festival.getAnyo(),
                festival.getDataInici(), festival.getDataFi(), festival.getEstatFestival().name(),
                festival.getDescripcio(), festival.getCategoriaMusical(), festival.getPressupostContractacio(),
                festival.getAforamentMaxim(), festival.getLocalitzacioDescriptiva(), festival.getLocalitzacioGeografica(),
                festival.getPublicEnfocat(), festival.getRequisitMinimEdat(), festival.getDataIniciPublicacio(),
                festival.getDataIniciVenda(), 0);
    }

    public void deleteFestival(int idFestival) {
        jdbcTemplate.update("DELETE FROM festival WHERE idfestival = ?", idFestival);
    }

    public void addNumEntradasVendidas(int idfestival, int num) {
        jdbcTemplate.update("UPDATE festival SET num_entradas_vendidas=num_entradas_vendidas + ? WHERE idfestival=?", num, idfestival);
    }

    public void updateFestival(Festival festival) {
        updateFestivalState(festival);
        jdbcTemplate.update("UPDATE festival SET cif_promotor=?, nom=?, anyo=?, datainici=?, datafi=?, estatfestival=?, descripcio=?, " +
                        "categoriamusical=?, pressupostcontractacio=?, aforamentmaxim=?, localitzaciodescriptiva=?, localitzaciogeografica=?, " +
                        "publicenfocat=?, requisitminimedat=?, datainicipublicacio=?, datainicivenda=?, num_entradas_vendidas=?, WHERE idfestival=?",
                festival.getCifPromotor(), festival.getNom(), festival.getAnyo(), festival.getDataInici(), festival.getDataFi(),
                festival.getEstatFestival().name(), festival.getDescripcio(), festival.getCategoriaMusical(), festival.getPressupostContractacio(),
                festival.getAforamentMaxim(), festival.getLocalitzacioDescriptiva(), festival.getLocalitzacioGeografica(),
                festival.getPublicEnfocat(), festival.getRequisitMinimEdat(), festival.getDataIniciPublicacio(), festival.getDataIniciVenda(),
                festival.getIdFestival(), festival.getNumEntradasVendidas());
    }

    public int getNumEntradasVendidas(int idFestival) {
        Festival festival = jdbcTemplate.query("SELECT num_entradas_vendidas FROM Festival WHERE idfestival = ?", new FestivalRowMapper(), idFestival).get(0);
        return festival.getNumEntradasVendidas();
    }

    public void updateFestivalState(Festival festival) {
        Date dataFi = festival.getDataFi();
        Date dataInici = festival.getDataInici();
        Date dataIniciVenda = festival.getDataIniciVenda();
        Date dataIniciPublicacio = festival.getDataIniciPublicacio();
        Date today = new Date();

        if (dataFi != null && dataFi.before(today)) {
            festival.setEstatFestival(EstatFestivalEnum.finalitzat);
        } else if (dataIniciVenda != null && dataIniciVenda.before(today) && (dataFi == null || dataFi.after(today))) {
            festival.setEstatFestival(EstatFestivalEnum.obertVenda);
        } else if (dataInici != null && dataInici.after(today) && (dataIniciPublicacio == null || dataIniciPublicacio.after(today))) {
            festival.setEstatFestival(EstatFestivalEnum.enPreparacio);
        } else if (dataInici != null && dataInici.after(today) && dataIniciPublicacio != null && dataIniciPublicacio.before(today)) {
            festival.setEstatFestival(EstatFestivalEnum.publica);
        }else {
            festival.setEstatFestival(EstatFestivalEnum.finalitzat);
        }
    }


    public List<Actuacio> getActuacionesFestival(int idFestival) {
        try {
            return jdbcTemplate.query("SELECT * FROM Actuacio WHERE idfestival = ?", new ActuacioRowMapper(), idFestival);
            // List<Actuacio> actuaciones = jdbcTemplate.queryForObject("select * from actuacio where idactuacio")
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Actuacio>();
        }
    }

    public List<Festival> getFestivals() {
        try {
            return jdbcTemplate.query("SELECT * FROM festival", new FestivalRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Festival>();
        }
    }

    public Festival getFestival(int idFestival) {
        try {

            return jdbcTemplate.queryForObject("SELECT * FROM festival WHERE idfestival=?", new FestivalRowMapper(), idFestival);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Festival> getFestivals(int size, int offset) {
        return jdbcTemplate.query("SELECT * FROM festival LIMIT ? OFFSET ?", new FestivalRowMapper(), size, offset);

    }
}
