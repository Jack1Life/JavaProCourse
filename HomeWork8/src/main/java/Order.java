import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @FilterField(type=FilterType.NUMERIC)
    private Long id;
    private Long date;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "order2goods",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "good_id"))
    @FilterField(type=FilterType.OBJ_ID)
    private List<Good> goods;
    @FilterField(type=FilterType.NUMERIC)
    private Long totalPrice;
    @ManyToOne
    @JoinColumn(name = "client_id")
    @FilterField(type=FilterType.OBJ_ID)
    private Client client;

    public Order() {
        this.goods = new ArrayList<Good>();
        this.date = new Date().getTime();
    }

    public Order(long date) {
        this.date = date;
        this.goods = new ArrayList<Good>();
    }

    public Order(long date, ArrayList<Good> goods, long totalPrice) {
        this.date = date;
        this.goods = goods;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public String getDateString() {
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm");
        return date == null ? null : dateFormat.format(new Date(date));
    }

    public void setDate(long date) {
        this.date = date;
    }

    public List<Good> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<Good> goods) {
        this.goods = goods;
    }

    public void addGood(Good good) {
        this.goods.add(good);
        good.addOrder(this);
        good.addBuyer(this.client);
        this.calculateTotalPrice();
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
        this.client.addOrder(this);
        for(Good good : goods) {
            good.addBuyer(client);
        }
    }

    public void calculateTotalPrice() {
        this.totalPrice = 0L;
        if(this.goods == null) {
            return;
        }
        for(Good good : this.goods) {
            this.totalPrice += good.getPrice();
        }
    }

    @Override
    public String toString() {
        String res =  "Замовлення "     + this.id + System.lineSeparator()
                + "Замовник: "          + (this.client == null ? "" : (client.getName() + " " + client.getSurname())) + System.lineSeparator()
                + "Дата замовлення: "   + this.getDateString() + System.lineSeparator()
                + "Товари: ";
        for(Good g : this.goods) {
            res += g.getName() + ",";
        }
        return res;
    }

    private String getOneLineGoods() {
        String res = "";
        boolean first = true;
        for(Good g : this.goods) {
            if(first) {
                first = false;
            } else {
                res += ",";
            }
            res += g.getName()  + "(" + g.getId() + ")";
        }
        return res;
    }
    @ConsoleListOut
    public static String[][] getGoodsTable(ArrayList<?> list) {
        ArrayList<Order> ordersList = (ArrayList<Order>) list;
        String[] header = {"Id", "Дата", "Замовник", "Товари", "Сума до сплати"};
        String[][] table = new String[ordersList.size() + 1][header.length];
        table[0] = header;

        int idx = 1;
        for(Order order : ordersList) {
            Client cl = order.getClient();
            table[idx][0] = "" + order.getId();
            table[idx][1] = order.getDateString();
            table[idx][2] = cl.getName() + " " + cl.getSurname() + "(" + cl.getId() + ")";
            table[idx][3] = "" + order.getOneLineGoods();
            table[idx][4] = "" + order.getTotalPrice();
            idx++;
        }
        return table;
    }
}
