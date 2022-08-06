import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @FilterField(type=FilterType.NUMERIC)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @FilterField(type=FilterType.OBJ_ID)
    private Client owner;
    @ManyToOne
    @JoinColumn(name = "bank_id")
    @FilterField(type=FilterType.OBJ_ID)
    private Bank bank;
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.PERSIST)
    private Set<Transaction> inTransactions;
    @OneToMany(mappedBy = "sender", cascade = CascadeType.PERSIST)
    private Set<Transaction> outTransactions;
    @FilterField(type=FilterType.NUMERIC)
    private Currency currency;
    @FilterField(type=FilterType.NUMERIC)
    private Double balance;

    public Account() {
        this.inTransactions = new HashSet<>();
        this.outTransactions = new HashSet<>();
    }

    public Account(Client owner, Bank bank, Currency currency, Double balance) {
        this.owner = owner;
        this.bank = bank;
        this.inTransactions = new HashSet<>();
        this.outTransactions = new HashSet<>();
        this.currency = currency;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Set<Transaction> getInTransactions() {
        return inTransactions;
    }

    public void setInTransactions(Set<Transaction> inTransactions) {
        this.inTransactions = inTransactions;
    }

    public Set<Transaction> getOutTransactions() {
        return outTransactions;
    }

    public void setOutTransactions(Set<Transaction> outTransactions) {
        this.outTransactions = outTransactions;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @ConsoleListOut
    public static String[][] getAccountsTable(ArrayList<?> list) {
        ArrayList<Account> accList = (ArrayList<Account>) list;
        String[] header = {"Id", "Банк", "Валюта", "Власник", "Баланс", "К-ть вхідних/вихідних транзакцій"};
        String[][] table = new String[accList.size() + 1][header.length];
        table[0] = header;
        int idx = 1;
        for(Account acc : accList) {
            table[idx][0] = "" + acc.getId();
            table[idx][1] = acc.getBank().getName();
            table[idx][2] = Currency.getStrValue(acc.getCurrency());
            table[idx][3] = acc.getOwner().getName() + " " + acc.getOwner().getSurname();
            table[idx][4] = "" + acc.getBalance();
            table[idx][5] = "" + acc.getInTransactions().size() + "/" + acc.getOutTransactions().size();
            idx++;
        }
        return table;
    }
}
