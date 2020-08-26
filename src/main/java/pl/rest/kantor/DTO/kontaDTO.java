package pl.rest.kantor.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class kontaDTO {


    public kontaDTO(Integer id, Double ilość) {
        this.id = id;
        this.ilość = ilość;
    }

    public kontaDTO(Double ilość) {
        this.ilość = ilość;
    }

    public kontaDTO(Integer id) {
        this.id = id;
    }

    public kontaDTO(Integer id, Integer id_klienta, String waluta, Double ilość, LocalDateTime data_modyfikacji) {
        this.id = id;
        this.id_klienta = id_klienta;
        this.waluta = waluta;
        this.ilość = ilość;
        this.data_modyfikacji = data_modyfikacji;
    }

    private Integer id;
    private Integer id_klienta;
    private String waluta;
    private Double ilość;
    private LocalDateTime data_modyfikacji;
}
