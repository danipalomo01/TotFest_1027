package daos;

import models.Promotor;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class PromotorRowMapper implements RowMapper<Promotor> {

    @Override
    public Promotor mapRow(ResultSet rs, int rowNum) throws SQLException {
        Promotor promotor = new Promotor();
        promotor.setCif(rs.getString("CIF"));
        promotor.setNomOrganisme(rs.getString("nomOrganisme"));
        promotor.setDomiciliFiscal(rs.getString("domiciliFiscal"));
        promotor.setSector(rs.getString("sector"));
        promotor.setDataAlta(rs.getDate("dataAlta"));
        promotor.setDataBaixaRelacio(rs.getDate("dataBaixaRelacio"));
        promotor.setNomGestorFestivals(rs.getString("nomGestorFestivals"));
        promotor.setPwd(rs.getString("pwd"));
        promotor.setMailGestorFestActual(rs.getString("mailGestorFestActual"));
        promotor.setTelefonGestorFest(rs.getString("telefonGestorFest"));
        promotor.setRazonSocial(rs.getString("razonSocial"));
        promotor.setTipusOrganisme(rs.getString("tipusOrganisme"));
        return promotor;
    }
}

