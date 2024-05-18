package daos;

import models.CompraSenseRegistre;
import org.springframework.jdbc.core.RowMapper;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;

public class CompraSenseRegistreRowMapper implements RowMapper<CompraSenseRegistre> {

    @Override
    public CompraSenseRegistre mapRow(ResultSet rs, int rowNum) throws SQLException {
        CompraSenseRegistre compra = new CompraSenseRegistre();
        compra.setIdCompra(rs.getInt("idCompra"));
        compra.setIdFestival(rs.getInt("idFestival"));
        compra.setData(rs.getDate("data"));
        compra.setPreuCompra(rs.getBigDecimal("preuCompra"));
        compra.setMail(rs.getString("mail"));
        compra.setTelefon(rs.getString("telefon"));
        compra.setImportTotal(rs.getBigDecimal("importTotal"));
        compra.setDataCompra(rs.getTimestamp("dataCompra"));
        return compra;
    }
}
