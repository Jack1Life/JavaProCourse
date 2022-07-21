package flats;

import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        System.out.println("start!");
        try {
            FlatsDb.openConnection();
            Flat[] fls = {
                    new Flat("Київ", "Дніпровський", "Сверстюка", "10/2", 1999, 3, 62.2, 3, 15000),
                    new Flat("Київ", "Дніпровський", "Шептицького", "22", 2005, 5, 32.2, 1, 19000),
                    new Flat("Київ", "Печерський", "бульвар Лесі Українки", "17", 2010, 21, 97.6, 5, 88000),
                    new Flat("Харків", "Салтівський", "Академіка Павлова", "49", 2011, 1, 55.8, 2, 12000)
            };
            for(Flat fl : fls) {
                FlatsDb.addFlat(fl);
            }
            /*
            HashMap<String, String> filters = new HashMap<String, String>();
            filters.put("city", "Київ");
            filters.put("roomsNum", "3");
            ArrayList<Flat> filtered =  FlatsDb.getFlatsList(filters);
            for(Flat fl : filtered) {
                System.out.println(fl.toString());
            }
            */
             ConsoleCommunicator.run();

        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        } finally {
            FlatsDb.closeConnection();
        }
    }
}
