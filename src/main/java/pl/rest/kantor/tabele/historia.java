package pl.rest.kantor.tabele;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class historia {

    @Id
    private Integer id;
    private Integer id_klienta;
    private String waluta_1;
    private Double ilość_1;
    private String waluta_2;
    private Double ilość_2;
    private LocalDateTime data;
}
