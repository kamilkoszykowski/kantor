package pl.rest.kantor.tabele;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class kursy {

    @Id
    private Integer id;
    private Double GBP;
    private Double HKD;
    private Double IDR;
    private Double ILS;
    private Double DKK;
    private Double INR;
    private Double CHF;
    private Double MXN;
    private Double CZK;
    private Double SGD;
    private Double THB;
    private Double HRK;
    private Double EUR;
    private Double MYR;
    private Double NOK;
    private Double CNY;
    private Double BGN;
    private Double PHP;
    private Double PLN;
    private Double ZAR;
    private Double CAD;
    private Double ISK;
    private Double BRL;
    private Double RON;
    private Double NZD;
    private Double TRY;
    private Double JPY;
    private Double RUB;
    private Double KRW;
    private Double USD;
    private Double AUD;
    private Double HUF;
    private Double SEK;
    private LocalDateTime data;
}
