import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ConsoleCommunicator {


    private static String getLine(Scanner sc) {
        String res = null;
        for(res = sc.nextLine(); res.length() == 0; res = sc.nextLine());
        return res;
    }

    private static String getFieldInputMessage(Field fl) {
        String res = "Введіть значення для поля " + fl.getName();
        FilterType ft = fl.getAnnotation(FilterField.class).type();
        switch (ft) {
            case NUMERIC:
                res += " (символи > < / можуть бути вказані для проміжку значень)";
                break;
            case OBJ_ID:
                res += " (очікується ID об'єкта)";
                break;
        }
        res += " або ~ для видалення фільтра";
        return res;
    }
    private static  String getSpaces(int num) {
        String spaces = "";
        for (int i = 0; i < num; i++){
            spaces += " ";
        }
        return spaces;
    }

    private static String getPrettyTable(String[][] table) {
        String res = "";
        int[] widths = new int[table[0].length];
        for(int i = 0; i < table.length; i++) {
            for(int j = 0; j < table[i].length; j++) {
                if(widths[j] < table[i][j].length()) {
                    widths[j] = table[i][j].length();
                }
            }
        }

        /* Count all | symbols */
        int totalWidth = table[0].length + 1;
        for(int width : widths) {
            totalWidth += width + 2;
        }
        char[] separatorBuf = new char[totalWidth];
        for(int i = 0; i < totalWidth; i++) {
            separatorBuf[i] = '-';
        }
        String rowsSeparator = new String(separatorBuf);
        res += rowsSeparator + System.lineSeparator();
        for(int i = 0; i < table.length; i++) {
            res += "| ";
            for(int j = 0; j < table[i].length; j++) {
                res += table[i][j] + getSpaces(widths[j] - table[i][j].length()) + " | ";
            }
            res += System.lineSeparator() + rowsSeparator + System.lineSeparator();
        }
        return res;
    }

    private static void printObjectsList(ArrayList<?> objList) {
        if (objList.size() == 0) {
            return;
        }
        Object firstObj = objList.get(0);
        try {
            for (Method method : firstObj.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(ConsoleListOut.class)) {
                    String[][] table = (String[][]) method.invoke(null, objList);
                    System.out.println(getPrettyTable(table));
                    return;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    private static void get(Scanner sc, Class<?> cl) {
        HashMap<String, String> filters = new HashMap<String, String>();
        Field[] fields = cl.getDeclaredFields();
        while(true) {
            System.out.println("Введіть номер фільтра для редагування або код операції:");
            for (int i = 0; i < fields.length; i++) {
                String fName = fields[i].getName();
                if(fields[i].isAnnotationPresent(FilterField.class)) {
                    System.out.println(i + ") " + fName + ": " + (filters.containsKey(fName) ? filters.get(fName) : "<не вказано>"));
                }
            }
            System.out.println("*) Пошук");
            System.out.println("!) Вихід");

            String itemCode = getLine(sc);
            if(itemCode.equals("!")){
                return;
            }
            if(itemCode.equals("*")){
                ArrayList<?> objList = (ArrayList<Object>) DbHandler.getFilteredList(filters, cl);
                System.out.println("Знайдено " + objList.size() + " об'єктів");
                printObjectsList(objList);
                continue;
            }

            try {
                int itemNum = Integer.parseInt(itemCode);
                if(itemNum < 0 || itemNum >= fields.length) {
                    System.out.println("Невірний номер поля");
                    continue;
                }
                System.out.println(getFieldInputMessage(fields[itemNum]));
                String value = getLine(sc);
                if(value.equals("%")){
                    filters.remove(fields[itemNum].getName());
                    continue;
                }
                filters.put(fields[itemNum].getName(), value);
            } catch (NumberFormatException e) {
                System.out.println("Невірний формат поля");
            }
        }
    }

    private static String getFieldValue(Field fld, Object obj) throws IllegalAccessException{
        fld.setAccessible(true);
        if(fld.getType() == int.class){
            return "" + fld.getInt(obj);
        } else if(fld.getType() == double.class){
            return "" + fld.getDouble(obj);
        } else if(fld.getType() == long.class){
            return "" + fld.getLong(obj);
        } else if(fld.getType() == String.class) {
            return "" + fld.get(obj);
        }
        return "";
    }

    private static void setFieldValue(Field fld, Object obj, String value) throws IllegalAccessException{
        fld.setAccessible(true);
        if(fld.getType() == int.class){
            fld.setInt(obj, Integer.parseInt(value));
        } else if(fld.getType() == double.class){
            fld.setDouble(obj, Double.parseDouble(value));
        } else if(fld.getType() == long.class){
            fld.setLong(obj, Long.parseLong(value));
        } else if(fld.getType() == String.class) {
            fld.set(obj, value);
        }
    }

    private static void addOrder(Scanner sc) {
        Order newOrder = new Order();
        while(true) {
            System.out.println("Введіть Id клієнта  (? для перегляду списку клієнтів або ! для виходу):");
            String clId = getLine(sc);
            if(clId.equals("?")) {
                printObjectsList((ArrayList<Object>) DbHandler.getFilteredList(new HashMap<String,String>(), Client.class));
                continue;
            } else if(clId.equals("!")) {
                return;
            }
            newOrder.setClient((Client) DbHandler.get(Long.parseLong(clId), Client.class));
            break;
        }

        while(true) {
            System.out.println("Введіть Id товару  (? для перегляду списку товарів, * для продовження або ! для виходу):");
            String gId = getLine(sc);
            if(gId.equals("?")) {
                printObjectsList((ArrayList<Object>) DbHandler.getFilteredList(new HashMap<String,String>(), Good.class));
                continue;
            } else if(gId.equals("!")) {
                return;
            }  else if(gId.equals("*")) {
                break;
            }
            newOrder.addGood((Good) DbHandler.get(Long.parseLong(gId), Good.class));
        }
        DbHandler.add(newOrder);
    }
    private static String getSimpleParameter(Scanner sc, String name) {
        System.out.println("Введіть " + name + " (! для виходу):");
        String value = getLine(sc);
        return (value.equals("!")) ? null : value;
    }
    private static void addClient(Scanner sc) {
        Client newClient = new Client();
        String value = null;
        if((value = getSimpleParameter(sc, "ім'я клієнта")) == null) {
            return;
        }
        newClient.setName(value);

        if((value = getSimpleParameter(sc, "прізвище клієнта")) == null) {
            return;
        }
        newClient.setSurname(value);

        if((value = getSimpleParameter(sc, "вік клієнта")) == null) {
            return;
        }
        newClient.setAge(Integer.parseInt(value));
        DbHandler.add(newClient);
    }

    private static void addGood(Scanner sc) {
        Good newGood = new Good();
        String value = null;

        if((value = getSimpleParameter(sc, "назву товару")) == null) { return; }
        newGood.setName(value);
        if((value = getSimpleParameter(sc, "категорію товару")) == null) { return; }
        newGood.setCategory(value);
        if((value = getSimpleParameter(sc, "опис товару")) == null) { return; }
        newGood.setDescription(value);
        if((value = getSimpleParameter(sc, "ціну товару")) == null) { return; }
        newGood.setPrice(Long.parseLong(value));
        DbHandler.add(newGood);
    }

    private static void delete(Scanner sc, Class<?> cl) {
        String id = null;
        while (true) {
            System.out.println("Введіть ID об'єкта (? перегляду списку об'єктів або ! для виходу): ");
            id = getLine(sc);
            if (id.equals("?")) {
                printObjectsList((ArrayList<Object>) DbHandler.getFilteredList(new HashMap<String, String>(), cl));
                continue;
            } else if(id.equals("!")) {
                return;
            }
            DbHandler.delete(Long.parseLong(id), cl);
            return;
        }
    }

    public static void run() {
      try(Scanner sc = new Scanner(System.in)) {
          while(true) {
              System.out.println("Введіть номер операції: ");
              System.out.println("11 - створити замовлення");
              System.out.println("12 - переглянути замовлення");
              System.out.println("12 - видалити замовлення");
              System.out.println();
              System.out.println("21 - додати товар");
              System.out.println("22 - переглянути товари");
              System.out.println("23 - видалити товар");
              System.out.println();
              System.out.println("31 - додати кліента");
              System.out.println("32 - переглянути клієнтів");
              System.out.println("33 - видалити клієнта");
              System.out.println();
              System.out.println("! - вихід");

              String operation = getLine(sc);
              switch (operation) {
                  case "11":
                      addOrder(sc);
                      break;
                  case "12":
                      get(sc, Order.class);
                      break;
                  case "13":
                      delete(sc, Order.class);
                      break;
                  case "21":
                      addGood(sc);
                      break;
                  case "22":
                      get(sc, Good.class);
                      break;
                  case "23":
                      delete(sc, Good.class);
                      break;
                  case "31":
                      addClient(sc);
                      break;
                  case "32":
                      get(sc, Client.class);
                      break;
                  case "33":
                      delete(sc, Client.class);
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
