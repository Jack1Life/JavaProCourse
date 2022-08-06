import java.util.Date;

public class Main {
    public static void main(String[] args) {
        System.out.println("Start");
        DbHandler.openConnection();

        Bank bank = new Bank("Ukrsib");
        bank.getRates().add(new ExchangeRate(Currency.UAH, 1.0, 1.0, bank));
        bank.getRates().add(new ExchangeRate(Currency.USD, 41.21, 39.2, bank));
        bank.getRates().add(new ExchangeRate(Currency.EUR, 43.12, 41.22, bank));

        Client[] clients = {
            new Client("Артем", "Логов"),
            new Client("Василь", "Вальтер"),
            new Client("Альбіна", "Орел"),
            new Client("Олег", "Палій")
        };
        Account[] accounts = {
                new Account(clients[0], bank, Currency.UAH, 15589.2),
                new Account(clients[0], bank, Currency.EUR, 1249.0),
                new Account(clients[1], bank, Currency.UAH, 87967.92),
                new Account(clients[2], bank, Currency.USD, 23450.0)
        };
        Transaction[] transactions = {
                new Transaction(accounts[0], accounts[1], 4000.0),
                new Transaction(accounts[2], accounts[3], 4000.0)
        };
        DbHandler.add(bank);
        for(Client client : clients) {
            DbHandler.add(client);
        }
        for(Account account : accounts) {
            DbHandler.add(account);
        }
        for(Transaction transaction : transactions) {
            DbHandler.add(transaction);
        }

        DbHandler.performUnderTransaction(() -> {
            for(Account account : accounts) {
                account.getBank().getAccounts().add(account);
                account.getOwner().getAccounts().add(account);
            }
            for(Transaction transaction : transactions) {
                transaction.getReceiver().getInTransactions().add(transaction);
                transaction.getSender().getOutTransactions().add(transaction);
            }
            return null;
        });

        ConsoleCommunicator.run();
        DbHandler.closeConnection();
    }
}
