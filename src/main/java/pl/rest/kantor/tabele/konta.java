package pl.rest.kantor.tabele;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class konta {

    @Id
    private Integer id;
    private Integer id_klienta;
    private String waluta;
    private Double ilość;
    private LocalDateTime data_modyfikacji;
}
