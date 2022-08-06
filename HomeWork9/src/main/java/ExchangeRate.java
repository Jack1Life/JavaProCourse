import javax.persistence.*;

@Entity
@Table(name = "exchange_rates")
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @FilterField(type=FilterType.NUMERIC)
    private Long id;
    private Currency currency;
    @FilterField(type=FilterType.NUMERIC)
    private Double buyRate;
    @FilterField(type=FilterType.NUMERIC)
    private Double sellRate;
    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    public ExchangeRate() {
    }

    public ExchangeRate(Currency currency, Double buyRate, Double sellRate, Bank bank) {
        this.currency = currency;
        this.buyRate = buyRate;
        this.sellRate = sellRate;
        this.bank = bank;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(Double buyRate) {
        this.buyRate = buyRate;
    }

    public Double getSellRate() {
        return sellRate;
    }

    public void setSellRate(Double sellRate) {
        this.sellRate = sellRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
}
