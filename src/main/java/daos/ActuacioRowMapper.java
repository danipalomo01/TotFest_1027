package daos;

import models.Actuacio;
import org.springframework.jdbc.core.RowMapper;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;

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
        return actuacio;
    }
}

