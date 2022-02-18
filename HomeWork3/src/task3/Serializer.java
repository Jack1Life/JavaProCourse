package task3;

import java.lang.reflect.Field;

public class Serializer {
    private static String FIELDS_SEPARATOR = System.lineSeparator();
    private static String PAIR_SEPARATOR = ": ";

    private static String getFieldValue(Field field, Object ob) {
        try{
            if(field.getType() == int.class){
                return "" + field.getInt(ob);
            } else if(field.getType() == String.class) {
                return (String) field.get(ob);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void setFieldValue(Field field, Object ob, String value) {
        try{
            if(field.getType() == int.class){
                field.setInt(ob, Integer.parseInt(value));
            } else if(field.getType() == String.class) {
                field.set(ob, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static String serialize(Object object) {
        String res = "";
        for(Field field : object.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(Save.class)){
                field.setAccessible(true);
                res += field.getName() + Serializer.PAIR_SEPARATOR + getFieldValue(field, object) + Serializer.FIELDS_SEPARATOR;
            }
        }
        return res;
    }



    public static void deserialize(String input, Object output) throws IllegalArgumentException, NoSuchFieldException {
        for(String field : input.split(Serializer.FIELDS_SEPARATOR)) {
            String[] params = field.split(Serializer.PAIR_SEPARATOR);
            if(params.length < 2){
                throw new IllegalArgumentException();
            }
            Field fl = output.getClass().getDeclaredField(params[0]);
            fl.setAccessible(true);
            setFieldValue(fl, output, params[1]);
        }
    }
}
