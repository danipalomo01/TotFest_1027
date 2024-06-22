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
        entrada.setIdFestival(rs.getInt("idfestival"));
        entrada.setData(rs.getDate("data"));
        entrada.setPreuVendaEntradaIndividual(rs.getBigDecimal("preuVendaEntradaIndividual"));
        return entrada;
    }
}

