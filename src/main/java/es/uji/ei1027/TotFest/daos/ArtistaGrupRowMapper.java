package es.uji.ei1027.TotFest.daos;

import es.uji.ei1027.TotFest.models.ArtistaGrup;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArtistaGrupRowMapper implements RowMapper<ArtistaGrup> {

    @Override
    public ArtistaGrup mapRow(ResultSet rs, int rowNum) throws SQLException {
        ArtistaGrup artistaGrup = new ArtistaGrup();
        artistaGrup.setIdArtista(rs.getInt("idArtista"));
        artistaGrup.setNom(rs.getString("nom"));
        artistaGrup.setTipusMusica(rs.getString("tipusMusica"));
        artistaGrup.setDescripcio(rs.getString("descripcio"));
        artistaGrup.setCachetActual(rs.getBigDecimal("cachetActual"));
        return artistaGrup;
    }
}

