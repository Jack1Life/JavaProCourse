package flats;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ConsoleCommunicator {


    private static String getLine(Scanner sc) {
        String res = null;
        for(res = sc.nextLine(); res.length() == 0; res = sc.nextLine());
        return res;
    }
    private static void getFlat(Scanner sc) {
        HashMap<String, String> filters = new HashMap<String, String>();
        Field[] fields = Flat.class.getDeclaredFields();
        while(true) {
            System.out.println("Введіть номер фільтра для редагування або код операції:");
            for (int i = 0; i < fields.length; i++) {
                String fName = fields[i].getName();
                System.out.println(i + ") " + fName + ": " + (filters.containsKey(fName) ? filters.get(fName) : "<не вказано>"));
            }
            System.out.println("*) Пошук");
            System.out.println("!) Вихід");

            String itemCode = getLine(sc);
            if(itemCode.equals("!")){
                return;
            }
            if(itemCode.equals("*")){
                ArrayList<Flat> list = FlatsDb.getFlatsList(filters);
                System.out.println("Знайдено " + list.size() + " квартир:");
                for(Flat fl : list) {
                    System.out.println(fl.toString());
                }
                continue;
            }

            int itemNum = 0;
            try {
                itemNum = Integer.parseInt(itemCode);
                if(itemNum < 0 || itemNum >= fields.length) {
                    System.out.println("Невірний номер поля");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Невірний формат поля");
                continue;
            }

            System.out.println("Введіть значення для поля " + fields[itemNum].getName());
            String value = getLine(sc);
            filters.put(fields[itemNum].getName(), value);
        }
    }

    private static String getFieldValue(Field fld, Flat flat) throws IllegalAccessException{
        fld.setAccessible(true);
        if(fld.getType() == int.class){
            return "" + fld.getInt(flat);
        } else if(fld.getType() == double.class){
            return "" + fld.getDouble(flat);
        } else if(fld.getType() == long.class){
            return "" + fld.getLong(flat);
        } else if(fld.getType() == String.class) {
            return "" + fld.get(flat);
        }
        return "";
    }

    private static void setFieldValue(Field fld, Flat flat, String value) throws IllegalAccessException{
        fld.setAccessible(true);
        if(fld.getType() == int.class){
            fld.setInt(flat, Integer.parseInt(value));
        } else if(fld.getType() == double.class){
            fld.setDouble(flat, Double.parseDouble(value));
        } else if(fld.getType() == long.class){
            fld.setLong(flat, Long.parseLong(value));
        } else if(fld.getType() == String.class) {
            fld.set(flat, value);
        }
    }

    private static void addFlat(Scanner sc) {
        Flat newFlat = new Flat();
        Field[] fields = Flat.class.getDeclaredFields();
        try {
            while(true) {
                System.out.println("Введіть номер параметра для редагування або код операції:");
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].isAnnotationPresent(GeneratedValue.class)
                            && fields[i].getAnnotation(GeneratedValue.class).strategy() == GenerationType.AUTO) {
                        continue;
                    }
                    System.out.println(i + ") " + fields[i].getName() + ": " + getFieldValue(fields[i], newFlat));
                }
                System.out.println("*) Додати");
                System.out.println("!) Вихід");

                String inOper = getLine(sc);

                if(inOper.equals("!")){
                    return;
                }

                if(inOper.equals("*")){
                    FlatsDb.addFlat(newFlat);
                    return;
                }

                int fNum = 0;
                try {
                    fNum = Integer.parseInt(inOper);
                    if(fNum < 0 || fNum >= fields.length) {
                        System.out.println("Невірний номер поля");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Невірний формат поля");
                    continue;
                }

                System.out.println("Введіть значення для поля " + fields[fNum].getName());
                String value = getLine(sc);
                setFieldValue(fields[fNum], newFlat, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void delFlat(Scanner sc) {
        System.out.println("Введіть ID квартири: ");
        long flatNum = sc.nextLong();
        FlatsDb.deleteFlat(flatNum);
    }

    private static void getAllFlats() {
        ArrayList<Flat> filtered =  FlatsDb.getFlatsList();
        for(Flat fl : filtered) {
            System.out.println(fl.toString());
        }
    }

    public static void run() {
      try(Scanner sc = new Scanner(System.in)) {
          while(true) {
              System.out.println("Введіть номер операції: ");
              System.out.println("1 - знайти квартиру");
              System.out.println("2 - додати квартиру");
              System.out.println("3 - видалити квартиру");
              System.out.println("4 - перелічити всі наявні квартири");
              System.out.println("! - вихід");
              String operation = getLine(sc);

              switch (operation) {
                  case "1":
                      getFlat(sc);
                      break;
                  case "2":
                      addFlat(sc);
                      break;
                  case "3":
                      delFlat(sc);
                      break;
                  case "4":
                      getAllFlats();
                      break;
                  case "!":
                      return;
                  default:
                      System.out.println("Невірний номер операції!");
                      break;
              }
          }

      }
    }
}
