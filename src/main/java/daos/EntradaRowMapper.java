package daos;

import models.Entrada;
import org.springframework.jdbc.core.RowMapper;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EntradaRowMapper implements RowMapper<Entrada> {

    @Override
    public Entrada mapRow(ResultSet rs, int rowNum) throws SQLException {
        Entrada entrada = new Entrada();
        entrada.setNumero(rs.getInt("numero"));
        entrada.setIdEntradaTipus(rs.getString("idEntradaTipus"));
        entrada.setPreuVendaEntradaIndividual(rs.getBigDecimal("preuVendaEntradaIndividual"));
        return entrada;
    }
}

