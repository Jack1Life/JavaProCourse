import java.lang.invoke.LambdaMetafactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ConsoleCommunicator {
    private static String scanLine(Scanner sc) {
        String res = null;
        for(res = sc.nextLine(); res.length() == 0; res = sc.nextLine());
        return res;
    }

    private static  Long scanLong(Scanner sc, String fieldName) throws NumberFormatException {
        System.out.println("Введіть " + fieldName + " або ! для виходу:");
        String val = scanLine(sc);
        if(val.equals("!")) {
            return null;
        }
        return  Long.parseLong(val);
    }

    private static  Double scanDouble(Scanner sc, String fieldName) throws NumberFormatException {
        System.out.println("Введіть " + fieldName + " або ! для виходу:");
        String val = scanLine(sc);
        if(val.equals("!")) {
            return null;
        }
        return  Double.parseDouble(val);
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

            String itemCode = scanLine(sc);
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
                String value = scanLine(sc);
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

    private static String getSimpleParameter(Scanner sc, String name) {
        System.out.println("Введіть " + name + " (! для виходу):");
        String value = scanLine(sc);
        return (value.equals("!")) ? null : value;
    }

    private static void delete(Scanner sc, Class<?> cl) {
        String id = null;
        while (true) {
            System.out.println("Введіть ID об'єкта (? перегляду списку об'єктів або ! для виходу): ");
            id = scanLine(sc);
            if (id.equals("?")) {
                printObjectsList((ArrayList<Object>) DbHandler.getList(cl));
                continue;
            } else if(id.equals("!")) {
                return;
            }
            DbHandler.delete(Long.parseLong(id), cl);
            return;
        }
    }

    private static Account getClientAccount(Client cl, Long accId) {
        for (Account acc : cl.getAccounts()) {
            if(acc.getId() == accId) {
                return acc;
            }
        }
        return null;
    }

    private static void createTransfer(Scanner sc, Client cl, TransactionType type) {
        while(true) {
            Account sourceAcc = null;
            if(type != TransactionType.TOP_UP) {
                printObjectsList(cl.getAccountsAsArrayList());
                Long srcAccId = scanLong(sc, "ID рахунку відправника");
                if(srcAccId == null) { return; }
                sourceAcc = getClientAccount(cl, srcAccId);
                if(sourceAcc == null) {
                    System.out.println("Некорректний ID рахунку відправника");
                    continue;
                }
            }

            HashMap<String, String> filters = new HashMap<String, String>();
            filters.put("owner", ((type == TransactionType.CLIENT2CLIENT) ? "~" : "") + cl.getId());
            ArrayList<?> accList = (ArrayList<Object>) DbHandler.getFilteredList(filters, Account.class);
            printObjectsList(accList);

            Long dstAccId = scanLong(sc, "ID рахунку призначення");
            if(dstAccId == null) { return; }
            Account targetAcc = (Account)DbHandler.get(dstAccId, Account.class);
            if(targetAcc == null) {
                System.out.println("Некорректний ID рахунку призначення");
                continue;
            }

            Double amount = scanDouble(sc, "сумму");
            if(amount == null) { return; }

            Account finalSourceAcc = sourceAcc;
            DbHandler.performUnderTransaction(() -> {
                Transaction transact = new Transaction(finalSourceAcc, targetAcc, amount);
                TransactionStatus status = transact.execute();
                System.out.println(status == TransactionStatus.DONE ? "Виконано" : "Помилка транзакції");
                return null;
            });
            break;
        }
    }
    private static void handleClientOperations(Scanner sc, Client cl) {
        while(true) {
            System.out.println("Введіть код операціїї: ");
            System.out.println("1) Поповнити рахунок");
            System.out.println("2) Переказ на рахунок іншого клієнта");
            System.out.println("3) Переказ між власними рахунками");
            System.out.println("4) Переглянути рахунки");
            System.out.println("!) Вихід");
            String operationId = scanLine(sc);
            switch (operationId) {
                case "1":
                    createTransfer(sc, cl, TransactionType.TOP_UP);
                    break;
                case "2":
                    createTransfer(sc, cl, TransactionType.CLIENT2CLIENT);
                    break;
                case "3":
                    createTransfer(sc, cl, TransactionType.INTERACCOUNT);
                    break;
                case "4":
                    printObjectsList(cl.getAccountsAsArrayList());
                    break;
                case "!":
                    return;
                default:
                    System.out.println("Невірний номер операції!");
                    break;
            }
        }
    }

    public static void run() {
      try(Scanner sc = new Scanner(System.in)) {
          while(true) {
              ArrayList<?> objList = (ArrayList<Object>) DbHandler.getList(Client.class);
              printObjectsList(objList);
              System.out.println("Введіть ID клієнта або ! для виходу: ");
              String clientId = scanLine(sc);
              if(clientId.equals("!")) {
                  return;
              }
              try {
                  Long id = Long.parseLong(clientId);
                  Client client = (Client) DbHandler.get(id, Client.class);
                  if(client == null) {
                      System.out.println("Невідомий ID клієнта");
                      continue;
                  }
                  handleClientOperations(sc, client);
              } catch (NumberFormatException e) {
                  System.out.println("Некоректний формат вводу");
              }

          }
      }
    }
}
