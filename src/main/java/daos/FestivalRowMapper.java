package daos;

import models.EstatFestivalEnum;
import models.Festival;
import org.springframework.jdbc.core.RowMapper;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class FestivalRowMapper implements RowMapper<Festival> {

    @Override
    public Festival mapRow(ResultSet rs, int rowNum) throws SQLException {
        Festival festival = new Festival();
        festival.setIdFestival(rs.getInt("idFestival"));
        festival.setCifPromotor(rs.getString("CIF_promotor"));
        festival.setNom(rs.getString("nom"));
        festival.setAnyo(rs.getInt("anyo"));
        festival.setDataInici(rs.getDate("dataInici"));
        festival.setDataFi(rs.getDate("dataFi"));
        festival.setEstatFestival(EstatFestivalEnum.valueOf(rs.getString("estatFestival")));
        festival.setDescripcio(rs.getString("descripcio"));
        festival.setCategoriaMusical(rs.getString("categoriaMusical"));
        festival.setPressupostContractacio(rs.getBigDecimal("pressupostContractacio"));
        festival.setAforamentMaxim(rs.getInt("aforamentMaxim"));
        festival.setLocalitzacioDescriptiva(rs.getString("localitzacioDescriptiva"));
        festival.setLocalitzacioGeografica(rs.getString("localitzacioGeografica"));
        festival.setPublicEnfocat(rs.getString("publicEnfocat"));
        festival.setRequisitMinimEdat(rs.getInt("requisitMinimEdat"));
        festival.setDataIniciPublicacio(rs.getDate("dataIniciPublicacio"));
        festival.setDataIniciVenda(rs.getDate("dataIniciVenda"));
        return festival;
    }
}

