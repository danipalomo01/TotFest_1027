package es.uji.ei1027.TotFest.daos;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import es.uji.ei1027.TotFest.models.ActuacionsFestival;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActuacionsFestivalRowMapper implements RowMapper<ActuacionsFestival> {

    @Override
    public ActuacionsFestival mapRow(ResultSet rs, int rowNum) throws SQLException {
        ActuacionsFestival actuacio = new ActuacionsFestival();
        actuacio.setIdActuacio(rs.getInt("idActuacion"));
        actuacio.setIdFestival(rs.getInt("idFestival"));

        return actuacio;
    }
}

