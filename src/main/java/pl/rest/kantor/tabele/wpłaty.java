package pl.rest.kantor.tabele;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class wpłaty {

    @Id
    private Integer id;
    private String numer_rachunku;
    private Double kwota;
    private LocalDateTime data;
}
