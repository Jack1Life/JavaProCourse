import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "banks")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @FilterField(type=FilterType.NUMERIC)
    private Long id;
    @FilterField(type=FilterType.STRING)
    private String name;

    @OneToMany(mappedBy = "bank", cascade = CascadeType.PERSIST)
    @FilterField(type=FilterType.OBJ_ID)
    private Set<Account> accounts;
    @OneToMany(mappedBy = "bank", cascade = CascadeType.PERSIST)
    @FilterField(type=FilterType.OBJ_ID)
    private Set<ExchangeRate> rates;

    public Bank() {
        this.accounts = new HashSet<>();
        this.rates = new HashSet<>();
    }

    public Bank(String name) {
        this.name = name;
        this.accounts = new HashSet<>();
        this.rates = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Set<ExchangeRate> getRates() {
        return rates;
    }

    public ExchangeRate getRate(Currency currency) {
        for(ExchangeRate rate : this.rates) {
            if(rate.getCurrency() == currency) {
                return rate;
            }
        }
        return null;
    }

    public void setRates(Set<ExchangeRate> rates) {
        this.rates = rates;
    }
}
