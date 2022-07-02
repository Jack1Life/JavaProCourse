package task3;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Serializer {
    private static String FIELDS_SEPARATOR = System.lineSeparator();
    private static String PAIR_SEPARATOR = ": ";
    private static String CONTAINER_START = "{";
    private static String CONTAINER_END =  "}";
    private static String NULL_OBJECT =  "null";

    private static String getFieldValue(Field field, Object ob) {
        try{
            if(field.getType() == int.class){
                return "" + field.getInt(ob);
            } else if(field.getType() == double.class){
                return "" + field.getDouble(ob);
            } else if(field.getType() == long.class){
                return "" + field.getLong(ob);
            } else if(field.getType() == String.class) {
                return "" + (field.get(ob) == null ? NULL_OBJECT : field.get(ob));
            }
            return field.get(ob) == null ? NULL_OBJECT : Serializer.serialize(field.get(ob));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String serialize(Object object) {
        String res = CONTAINER_START + FIELDS_SEPARATOR;
        for(Field field : object.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(Save.class)){
                field.setAccessible(true);
                res += field.getName() + Serializer.PAIR_SEPARATOR + getFieldValue(field, object) + Serializer.FIELDS_SEPARATOR;
            }
        }
        return res + CONTAINER_END;
    }

    private static void setFieldValue(Field field, Object ob, String value) {
        try{
            if(field.getType() == int.class){
                field.setInt(ob, Integer.parseInt(value));
            } else if(field.getType() == double.class){
                field.setDouble(ob, Double.parseDouble(value));
            } else if(field.getType() == long.class){
                field.setLong(ob, Long.parseLong(value));
            } else if(field.getType() == String.class) {
                field.set(ob, (value.equals(NULL_OBJECT)) ? null : value);
            } else {
                field.set(ob, (value.equals(NULL_OBJECT)) ? null : deserialize(value, field.getType()));
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static Object deserialize(String input, Class<?> cl) throws IllegalArgumentException, NoSuchFieldException {
        String startSeq = CONTAINER_START + FIELDS_SEPARATOR;
        int start = input.indexOf(startSeq);
        int end = input.lastIndexOf(CONTAINER_END);
        input = input.substring(start + startSeq.length(), end - CONTAINER_END.length());
        try {
            Constructor<?> constr = cl.getConstructor();
            Object output = constr.newInstance();

            String internalObjectMarker = PAIR_SEPARATOR + CONTAINER_START;
            int nestedObjects = 0;
            Field field = null;
            String value = "";
            for(String row : input.split(Serializer.FIELDS_SEPARATOR)) {
                if(nestedObjects != 0) {
                    if(row.indexOf(internalObjectMarker) > 0) {
                        nestedObjects++;
                    }
                    if(row.equals(CONTAINER_END)) {
                        nestedObjects--;
                    }
                    value += row + FIELDS_SEPARATOR;
                    if(nestedObjects == 0) {
                        setFieldValue(field, output, value);
                    }
                    continue;
                }

                String[] params = row.split(Serializer.PAIR_SEPARATOR);
                if(params.length < 2){
                    throw new IllegalArgumentException();
                }
                field = output.getClass().getDeclaredField(params[0]);
                field.setAccessible(true);
                value = params[1];
                if(value.equals(CONTAINER_START)) {
                    value += FIELDS_SEPARATOR;
                    nestedObjects++;
                } else {
                    setFieldValue(field, output, params[1]);
                }
            }
            return output;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
