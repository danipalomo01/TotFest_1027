package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.ContracteArtista;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContracteArtistaRowMapper implements RowMapper<ContracteArtista> {

    @Override
    public ContracteArtista mapRow(ResultSet rs, int rowNum) throws SQLException {
        ContracteArtista contracte = new ContracteArtista();
        contracte.setIdContracte(rs.getInt("idContracte"));
        contracte.setIdArtista(rs.getInt("idArtista"));
        contracte.setDataInici(rs.getDate("dataInici"));
        contracte.setDataFi(rs.getDate("dataFi"));
        contracte.setCondicionsDescriptiu(rs.getString("condicionsDescriptiu"));
        contracte.setNumActuacionsAny(rs.getInt("numActuacionsAny"));
        contracte.setImportContracte(rs.getBigDecimal("importContracte"));
        contracte.setIdComercial(rs.getInt("idGestorComercial"));
        return contracte;
    }
}

