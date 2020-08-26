package pl.rest.kantor.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class historiaDTO {

    public historiaDTO(Integer id, Integer id_klienta, String waluta_1, Double ilość_1, String waluta_2, Double ilość_2, LocalDateTime data) {
        this.id = id;
        this.id_klienta = id_klienta;
        this.waluta_1 = waluta_1;
        this.ilość_1 = ilość_1;
        this.waluta_2 = waluta_2;
        this.ilość_2 = ilość_2;
        this.data = data;
    }

    private Integer id;
    private Integer id_klienta;
    private String waluta_1;
    private Double ilość_1;
    private String waluta_2;
    private Double ilość_2;
    private LocalDateTime data;
}
