package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.EntradaTipus;
import es.uji.ei1027.TotFest.models.EntradaTipusEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EntradaTipusRowMapper implements RowMapper<EntradaTipus> {

    @Override
    public EntradaTipus mapRow(ResultSet rs, int rowNum) throws SQLException {
        EntradaTipus entradaTipus = new EntradaTipus();
        entradaTipus.setId(rs.getInt("id"));
        entradaTipus.setEntradaTipus(EntradaTipusEnum.fromInt(rs.getInt("entradaTipus")));
        entradaTipus.setIdFestival(rs.getInt("idFestival"));
        entradaTipus.setPreu(rs.getBigDecimal("preu"));
        entradaTipus.setDescripcio(rs.getString("descripcio"));
        entradaTipus.setPercentatgeMaximAforament(rs.getBigDecimal("percentatgeMaximAforament"));
        return entradaTipus;
    }
}

