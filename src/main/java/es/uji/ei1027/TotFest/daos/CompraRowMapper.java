package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.Compra;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CompraRowMapper implements RowMapper<Compra> {

    @Override
    public Compra mapRow(ResultSet rs, int rowNum) throws SQLException {
        Compra compra = new Compra();
        compra.setIdcompra(rs.getInt("idcompra"));
        compra.setData(rs.getDate("data"));
        compra.setPreucompra(rs.getDouble("preucompra"));
        compra.setEmail(rs.getString("email"));
        compra.setTelefon(rs.getString("telefon"));
        return compra;
    }
}
