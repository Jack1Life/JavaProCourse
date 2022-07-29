import java.util.Date;

public class Main {

    public static void main(String[] args)  {
        System.out.println("Start");
        DbHandler.openConnection();
        Good[] goods = {
                new Good("Футболка Синя", 100L, "Одяг", "Синя футболка. Виробник: Adidas"),
                new Good("Футболка Біла", 110L, "Одяг", "Біла футболка. Виробник: Puma"),
                new Good("Кросівки Чорні", 500L, "Взуття", "Кросівки чорні чоловічі")
        };
        Client[] clients = {
                new Client("Висиль", "Гупало", 44),
                new Client("Марія", "Стожар", 23),
                new Client("Михайло", "Крона", 19)
        };

        Order[] orders = {
                new Order(new Date().getTime() - (1000 * 60 * 60 * 13)),
                new Order(new Date().getTime() - (1000 * 60 * 60 * 53)),
                new Order(new Date().getTime() - (1000 * 60 * 60 * 123)),
                new Order(new Date().getTime() - (1000 * 60 * 60 * 12223))
        };

        for(Good good : goods) {
            DbHandler.add(good);
        }
        for(Order or : orders) {
            DbHandler.add(or);
        }
        for(Client cl : clients) {
            DbHandler.add(cl);
        }

        DbHandler.performUnderTransaction(() -> {
            orders[0].setClient(clients[0]);
            orders[1].setClient(clients[1]);
            orders[2].setClient(clients[2]);
            orders[3].setClient(clients[2]);

            orders[0].addGood(goods[0]);
            orders[0].addGood(goods[1]);
            orders[1].addGood(goods[2]);
            orders[1].addGood(goods[0]);

            orders[2].addGood(goods[0]);
            orders[2].addGood(goods[1]);
            orders[2].addGood(goods[2]);

            orders[3].addGood(goods[0]);
            orders[3].addGood(goods[2]);
            return null;
        });
        ConsoleCommunicator.run();
        DbHandler.closeConnection();
    }
}
