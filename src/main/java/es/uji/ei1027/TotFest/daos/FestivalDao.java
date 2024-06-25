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
import java.time.LocalDate;
import java.time.ZoneId;
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
                        "publicenfocat=?, requisitminimedat=?, datainicipublicacio=?, datainicivenda=?, num_entradas_vendidas=? WHERE idfestival=?",
                festival.getCifPromotor(), festival.getNom(), festival.getAnyo(), festival.getDataInici(), festival.getDataFi(),
                festival.getEstatFestival().name(), festival.getDescripcio(), festival.getCategoriaMusical(), festival.getPressupostContractacio(),
                festival.getAforamentMaxim(), festival.getLocalitzacioDescriptiva(), festival.getLocalitzacioGeografica(),
                festival.getPublicEnfocat(), festival.getRequisitMinimEdat(), festival.getDataIniciPublicacio(), festival.getDataIniciVenda(),
                festival.getNumEntradasVendidas(), festival.getIdFestival());
    }

    public int getNumEntradasVendidas(int idFestival) {
        Festival festival = jdbcTemplate.query("SELECT num_entradas_vendidas FROM Festival WHERE idfestival = ?", new FestivalRowMapper(), idFestival).get(0);
        return festival.getNumEntradasVendidas();
    }

    public void updateFestivalState(Festival festival) {
        java.sql.Date dataFiSql = festival.getDataFi();
        java.sql.Date dataIniciVendaSql = festival.getDataIniciVenda();
        java.sql.Date dataIniciPublicacioSql = festival.getDataIniciPublicacio();

        LocalDate dataFi = dataFiSql != null ? dataFiSql.toLocalDate() : null;
        LocalDate dataIniciVenda = dataIniciVendaSql != null ? dataIniciVendaSql.toLocalDate() : null;
        LocalDate dataIniciPublicacio = dataIniciPublicacioSql != null ? dataIniciPublicacioSql.toLocalDate() : null;
        LocalDate today = LocalDate.now();

        if (dataFi != null && today.isAfter(dataFi)) {
            festival.setEstatFestival(EstatFestivalEnum.finalitzat);
        } else if (dataIniciVenda != null && (dataIniciVenda.isBefore(today) || dataIniciVenda.equals(today))) {
            festival.setEstatFestival(EstatFestivalEnum.obertVenda);
        } else if (dataIniciPublicacio != null && (dataIniciPublicacio.isBefore(today) || dataIniciPublicacio.equals(today))) {
            festival.setEstatFestival(EstatFestivalEnum.publica);
        } else {
            festival.setEstatFestival(EstatFestivalEnum.enPreparacio);
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
            List<Festival> festivales = jdbcTemplate.query("SELECT * FROM festival", new FestivalRowMapper());
            for(Festival festival: festivales) {
                updateFestivalState(festival);
            }
            return festivales;
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Festival>();
        }
    }

    public Festival getFestival(int idFestival) {
        try {

            Festival festival = jdbcTemplate.queryForObject("SELECT * FROM festival WHERE idfestival=?", new FestivalRowMapper(), idFestival);
            updateFestivalState(festival);
            return festival;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Festival> getFestivals(int size, int offset) {

        List<Festival> festivales = jdbcTemplate.query("SELECT * FROM festival LIMIT ? OFFSET ?", new FestivalRowMapper(), size, offset);
        for(Festival festival: festivales) {
            updateFestivalState(festival);
        }
        return festivales;
    }
}
