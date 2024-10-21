package es.uji.ei1027.TotFest.models;

public enum EntradaTipusEnum {
    DIA(1),
    FESTIVAL_COMPLETO(2);

    private final int value;

    EntradaTipusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // Método para obtener el enum desde un valor entero
    public static EntradaTipusEnum fromInt(int value) {
        for (EntradaTipusEnum type : EntradaTipusEnum.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Valor no válido para EntradaTipusEnum: " + value);
    }
}


