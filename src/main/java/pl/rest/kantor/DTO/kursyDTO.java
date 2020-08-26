package pl.rest.kantor.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class kursyDTO {

    public kursyDTO(LocalDateTime data) {
        this.data = data;
    }

    public kursyDTO(Integer id, Double GBP, Double HKD, Double IDR, Double ILS, Double DKK, Double INR, Double CHF, Double MXN, Double CZK, Double SGD, Double THB, Double HRK, Double EUR, Double MYR, Double NOK, Double CNY, Double BGN, Double PHP, Double PLN, Double ZAR, Double CAD, Double ISK, Double BRL, Double RON, Double NZD, Double TRY, Double JPY, Double RUB, Double KRW, Double USD, Double AUD, Double HUF, Double SEK, LocalDateTime data) {
        this.id = id;
        this.GBP = GBP;
        this.HKD = HKD;
        this.IDR = IDR;
        this.ILS = ILS;
        this.DKK = DKK;
        this.INR = INR;
        this.CHF = CHF;
        this.MXN = MXN;
        this.CZK = CZK;
        this.SGD = SGD;
        this.THB = THB;
        this.HRK = HRK;
        this.EUR = EUR;
        this.MYR = MYR;
        this.NOK = NOK;
        this.CNY = CNY;
        this.BGN = BGN;
        this.PHP = PHP;
        this.PLN = PLN;
        this.ZAR = ZAR;
        this.CAD = CAD;
        this.ISK = ISK;
        this.BRL = BRL;
        this.RON = RON;
        this.NZD = NZD;
        this.TRY = TRY;
        this.JPY = JPY;
        this.RUB = RUB;
        this.KRW = KRW;
        this.USD = USD;
        this.AUD = AUD;
        this.HUF = HUF;
        this.SEK = SEK;
        this.data = data;
    }

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
