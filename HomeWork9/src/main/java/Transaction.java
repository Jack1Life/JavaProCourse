import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @FilterField(type=FilterType.NUMERIC)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Account sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Account receiver;
    @FilterField(type=FilterType.NUMERIC)
    private Double exchangeRate;
    @FilterField(type=FilterType.NUMERIC)
    private Long date;
    @FilterField(type=FilterType.NUMERIC)
    private Double transferAmount;
    @FilterField(type=FilterType.NUMERIC)
    private  TransactionStatus status;

    public Transaction() {
        this.status = TransactionStatus.PREPARED;
    }

    public Transaction(Account sender, Account receiver, Double transferAmount) {
        this.sender = sender;
        this.receiver = receiver;
        this.transferAmount = transferAmount;
        this.status = TransactionStatus.PREPARED;
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public Account getReceiver() {
        return receiver;
    }

    public void setReceiver(Account receiver) {
        this.receiver = receiver;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }

    public TransactionStatus execute() {
        this.date = new Date().getTime();
        if(this.sender == null) {
            this.exchangeRate = 1.0;
        } else {
            Bank recBank = this.receiver.getBank();
            ExchangeRate outCurrencyRate = recBank.getRate(this.sender.getCurrency());
            ExchangeRate inCurrencyRate = recBank.getRate(this.receiver.getCurrency());
            if(inCurrencyRate == null || outCurrencyRate == null || this.sender.getBalance() < this.transferAmount) {
                this.status = TransactionStatus.DECLINED;
                return null;
            }
            this.exchangeRate = outCurrencyRate.getBuyRate() / inCurrencyRate.getBuyRate();
        }

        Double calculated = this.transferAmount * this.exchangeRate;
        if(this.sender != null) {
            this.sender.setBalance(this.sender.getBalance() - calculated);
            this.sender.getOutTransactions().add(this);
        }
        this.receiver.setBalance(this.receiver.getBalance() + calculated);
        this.receiver.getInTransactions().add(this);
        return this.status;
    }
}
