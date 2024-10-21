package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.Devolucion;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DevolucionRowMapper implements RowMapper<Devolucion> {

    @Override
    public Devolucion mapRow(ResultSet rs, int rowNum) throws SQLException {
        Devolucion devolucion = new Devolucion();
        devolucion.setId(rs.getInt("id"));
        devolucion.setNumero(rs.getInt("numero"));
        return devolucion;
    }
}