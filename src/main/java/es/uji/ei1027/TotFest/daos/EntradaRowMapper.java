package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.Entrada;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EntradaRowMapper implements RowMapper<Entrada> {

    @Override
    public Entrada mapRow(ResultSet rs, int rowNum) throws SQLException {
        Entrada entrada = new Entrada();
        entrada.setNumero(rs.getInt("numero"));
       // entrada.setIdFestival(rs.getInt("idfestival"));
        entrada.setDatacompra(rs.getDate("data"));
        entrada.setEntradaTipus(rs.getInt("identradatipus"));
        entrada.setIdcompra(rs.getInt("idcompra"));
        //entrada.setTelefono(rs.getString("telefono"));
        //entrada.setEmail(rs.getString("email"));
        return entrada;
    }
}

