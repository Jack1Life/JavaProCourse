import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @FilterField(type=FilterType.NUMERIC)
    private Long id;
    @FilterField(type=FilterType.STRING)
    private String name;
    @FilterField(type=FilterType.STRING)
    private String surname;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.PERSIST)
    @FilterField(type=FilterType.OBJ_ID)
    private Set<Account> accounts;

    public Client() {
        this.accounts = new HashSet<>();
    }

    public Client(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.accounts = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public ArrayList<Account> getAccountsAsArrayList() {
        ArrayList<Account> accountsList = new ArrayList<Account>();
        for (Account acc : this.accounts) {
            accountsList.add(acc);
        }
        return accountsList;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ConsoleListOut
    public static String[][] getClientsTable(ArrayList<?> list) {
        ArrayList<Client> cliList = (ArrayList<Client>) list;
        String[] header = {"Id", "Прізвище", "Ім'я"};
        String[][] table = new String[cliList.size() + 1][header.length];
        table[0] = header;

        int idx = 1;
        for(Client cl : cliList) {
            table[idx][0] = "" + cl.getId();
            table[idx][1] = cl.getSurname();
            table[idx][2] = cl.getName();
            idx++;
        }
        return table;
    }
}
