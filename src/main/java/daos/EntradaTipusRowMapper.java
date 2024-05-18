package daos;

import models.EntradaTipus;
import models.EntradaTipusEnum;
import org.springframework.jdbc.core.RowMapper;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class EntradaTipusRowMapper implements RowMapper<EntradaTipus> {

    @Override
    public EntradaTipus mapRow(ResultSet rs, int rowNum) throws SQLException {
        EntradaTipus entradaTipus = new EntradaTipus();
        entradaTipus.setEntradaTipus(EntradaTipusEnum.valueOf(rs.getString("entradaTipus")));
        entradaTipus.setIdFestival(rs.getInt("idFestival"));
        entradaTipus.setPreu(rs.getBigDecimal("preu"));
        entradaTipus.setDescripcio(rs.getString("descripcio"));
        entradaTipus.setNombreMaxim(rs.getInt("nombreMaxim"));
        entradaTipus.setNombreVendes(rs.getInt("nombreVendes"));
        entradaTipus.setDataPerTipusDia(rs.getDate("dataPerTipusDia"));
        entradaTipus.setPercentatgeMaximAforament(rs.getBigDecimal("percentatgeMaximAforament"));
        return entradaTipus;
    }
}

