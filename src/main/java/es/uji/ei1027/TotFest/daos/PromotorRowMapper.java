package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.Promotor;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PromotorRowMapper implements RowMapper<Promotor> {

    @Override
    public Promotor mapRow(ResultSet rs, int rowNum) throws SQLException {
        Promotor promotor = new Promotor();
        promotor.setCif(rs.getString("cif"));
        promotor.setNomOrganisme(rs.getString("nomorganisme"));
        promotor.setDomiciliFiscal(rs.getString("domicilifiscal"));
        promotor.setSector(rs.getString("sector"));
        promotor.setDataAlta(rs.getDate("dataalta"));
        promotor.setDataBaixaRelacioComercial(rs.getDate("databaixarelaciocomercial"));
        promotor.setDatainiciGestorFest(rs.getDate("datainicigestorfest"));
        return promotor;
    }
}
