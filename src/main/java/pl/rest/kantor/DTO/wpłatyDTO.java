package pl.rest.kantor.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class wpłatyDTO {

    public wpłatyDTO(Integer id, String numer_rachunku, Double kwota) {
        this.id = id;
        this.numer_rachunku = numer_rachunku;
        this.kwota = kwota;
    }

    private Integer id;
    private String numer_rachunku;
    private Double kwota;
}
