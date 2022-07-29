import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "goods")
public class Good {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @FilterField(type=FilterType.NUMERIC)
    private Long id;
    @FilterField(type=FilterType.STRING)
    private String name;
    @FilterField(type=FilterType.NUMERIC)
    private Long price;
    @FilterField(type=FilterType.STRING)
    private String category;
    @FilterField(type=FilterType.STRING)
    private String description;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "clients2goods",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "buyer_id"))
    @FilterField(type=FilterType.OBJ_ID)
    private Set<Client> buyers;
    @ManyToMany(mappedBy = "goods")
    @FilterField(type=FilterType.OBJ_ID)
    private Set<Order> orders;

    public Good() {
        this.buyers = new HashSet<>();
        this.orders = new HashSet<>();
    }

    public Good(String name, Long price, String category, String description) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.buyers = new HashSet<>();
        this.orders = new HashSet<>();
    }

    public Good(String name, Long price, String category, String description, Set<Client> buyers, Set<Order> orders) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.buyers = buyers;
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Client> getBuyers() {
        return buyers;
    }

    public void setBuyers(Set<Client> buyers) {
        this.buyers = buyers;
    }

    public void addBuyer(Client buyer) { this.buyers.add(buyer); }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) { this.orders.add(order); }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String res =  "Товар "              + this.id + System.lineSeparator()
                + "Назва: "                 + this.name + System.lineSeparator()
                + "Кількість замовлень: "   + this.orders.size() + System.lineSeparator()
                + "Кількість покупців: "    + this.buyers.size() + System.lineSeparator()
                + "Ціна: "                  + this.price + System.lineSeparator();
        return res;
    }

    private String getLastOrderDate() {
        Order lastOrder = null;
        for(Order or : this.getOrders()) {
            if(lastOrder == null || or.getDate() > lastOrder.getDate()) {
                lastOrder = or;
            }
        }
        return lastOrder == null ? "" : lastOrder.getDateString();
    }

    @ConsoleListOut
    public static String[][] getGoodsTable(ArrayList<?> list) {
        ArrayList<Good> goodsList = (ArrayList<Good>) list;
        String[] header = {"Id", "Назва", "Категорія", "Опис", "Ціна", "К-ть замовлень", "К-ть покупців", "Дата останнього замовлення"};
        String[][] table = new String[goodsList.size() + 1][header.length];
        table[0] = header;

        int idx = 1;
        for(Good good : goodsList) {
            table[idx][0] = "" + good.getId();
            table[idx][1] = good.getName();
            table[idx][2] = good.getCategory();
            table[idx][3] = good.getDescription();
            table[idx][4] = "" + good.getPrice();
            table[idx][5] = "" + good.getOrders().size();
            table[idx][6] = "" + good.getBuyers().size();
            table[idx][7] = "" + good.getLastOrderDate();
            idx++;
        }
        return table;
    }
}
