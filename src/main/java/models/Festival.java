package models;

import java.math.BigDecimal;
import java.util.Date;

public class Festival {

    private int idFestival;
    private String cifPromotor;
    private String nom;
    private int anyo;
    private Date dataInici;
    private Date dataFi;
    private EstatFestivalEnum estatFestival;
    private String descripcio;
    private String categoriaMusical;
    private BigDecimal pressupostContractacio;
    private int aforamentMaxim;
    private String localitzacioDescriptiva;
    private String localitzacioGeografica;
    private String publicEnfocat;
    private int requisitMinimEdat;
    private Date dataIniciPublicacio;
    private Date dataIniciVenda;

    public Festival(){

    }
    public Festival(int idFestival, String cifPromotor, String nom, int anyo, Date dataInici, Date dataFi, EstatFestivalEnum estatFestival, String descripcio, String categoriaMusical, BigDecimal pressupostContractacio, int aforamentMaxim, String localitzacioDescriptiva, String localitzacioGeografica, String publicEnfocat, int requisitMinimEdat, Date dataIniciPublicacio, Date dataIniciVenda) {
        this.idFestival = idFestival;
        this.cifPromotor = cifPromotor;
        this.nom = nom;
        this.anyo = anyo;
        this.dataInici = dataInici;
        this.dataFi = dataFi;
        this.estatFestival = estatFestival;
        this.descripcio = descripcio;
        this.categoriaMusical = categoriaMusical;
        this.pressupostContractacio = pressupostContractacio;
        this.aforamentMaxim = aforamentMaxim;
        this.localitzacioDescriptiva = localitzacioDescriptiva;
        this.localitzacioGeografica = localitzacioGeografica;
        this.publicEnfocat = publicEnfocat;
        this.requisitMinimEdat = requisitMinimEdat;
        this.dataIniciPublicacio = dataIniciPublicacio;
        this.dataIniciVenda = dataIniciVenda;
    }

    public int getIdFestival() {
        return idFestival;
    }

    public void setIdFestival(int idFestival) {
        this.idFestival = idFestival;
    }

    public String getCifPromotor() {
        return cifPromotor;
    }

    public void setCifPromotor(String cifPromotor) {
        this.cifPromotor = cifPromotor;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getAnyo() {
        return anyo;
    }

    public void setAnyo(int anyo) {
        this.anyo = anyo;
    }

    public Date getDataInici() {
        return dataInici;
    }

    public void setDataInici(Date dataInici) {
        this.dataInici = dataInici;
    }

    public Date getDataFi() {
        return dataFi;
    }

    public void setDataFi(Date dataFi) {
        this.dataFi = dataFi;
    }

    public EstatFestivalEnum getEstatFestival() {
        return estatFestival;
    }

    public void setEstatFestival(EstatFestivalEnum estatFestival) {
        this.estatFestival = estatFestival;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public String getCategoriaMusical() {
        return categoriaMusical;
    }

    public void setCategoriaMusical(String categoriaMusical) {
        this.categoriaMusical = categoriaMusical;
    }

    public BigDecimal getPressupostContractacio() {
        return pressupostContractacio;
    }

    public void setPressupostContractacio(BigDecimal pressupostContractacio) {
        this.pressupostContractacio = pressupostContractacio;
    }

    public int getAforamentMaxim() {
        return aforamentMaxim;
    }

    public void setAforamentMaxim(int aforamentMaxim) {
        this.aforamentMaxim = aforamentMaxim;
    }

    public String getLocalitzacioDescriptiva() {
        return localitzacioDescriptiva;
    }

    public void setLocalitzacioDescriptiva(String localitzacioDescriptiva) {
        this.localitzacioDescriptiva = localitzacioDescriptiva;
    }

    public String getLocalitzacioGeografica() {
        return localitzacioGeografica;
    }

    public void setLocalitzacioGeografica(String localitzacioGeografica) {
        this.localitzacioGeografica = localitzacioGeografica;
    }

    public String getPublicEnfocat() {
        return publicEnfocat;
    }

    public void setPublicEnfocat(String publicEnfocat) {
        this.publicEnfocat = publicEnfocat;
    }

    public int getRequisitMinimEdat() {
        return requisitMinimEdat;
    }

    public void setRequisitMinimEdat(int requisitMinimEdat) {
        this.requisitMinimEdat = requisitMinimEdat;
    }

    public Date getDataIniciPublicacio() {
        return dataIniciPublicacio;
    }

    public void setDataIniciPublicacio(Date dataIniciPublicacio) {
        this.dataIniciPublicacio = dataIniciPublicacio;
    }

    public Date getDataIniciVenda() {
        return dataIniciVenda;
    }

    public void setDataIniciVenda(Date dataIniciVenda) {
        this.dataIniciVenda = dataIniciVenda;
    }
}

