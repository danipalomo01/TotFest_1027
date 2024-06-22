package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.Actuacio;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActuacioRowMapper implements RowMapper<Actuacio> {

    @Override
    public Actuacio mapRow(ResultSet rs, int rowNum) throws SQLException {
        Actuacio actuacio = new Actuacio();
        actuacio.setIdActuacio(rs.getInt("idActuacio"));
        actuacio.setIdContracte(rs.getInt("idContracte"));
        actuacio.setData(rs.getDate("data"));
        actuacio.setHoraInici(rs.getTime("horaInici"));
        actuacio.setHoraFiPrevista(rs.getTime("horaFiPrevista"));
        actuacio.setPreuContracteActuacio(rs.getBigDecimal("preuContracteActuacio"));
        actuacio.setComentaris(rs.getString("comentaris"));
        actuacio.setIdFestival(rs.getInt("idFestival"));
        actuacio.setNomartista(rs.getString("nomartista"));
        return actuacio;
    }
}

