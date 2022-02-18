import task1.Calculator;
import task1.Test;
import task2.Saver;
import task2.TextContainer;
import task3.Human;
import task3.Serializer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void task1() throws java.lang.IllegalAccessException, InvocationTargetException {
        for(Method m : Calculator.class.getMethods()){
            if(m.isAnnotationPresent(Test.class)){
                Test annotation = m.getAnnotation(Test.class);
                int paramA = annotation.a();
                int paramB = annotation.b();
                int result = (Integer) m.invoke(null, paramA, paramB);
                System.out.println("Task 1: Method '" + m.getName()
                        +  "' was called with parameters " + paramA + " and " + paramB + ". Returned value: " + result);
            }
        }
    }

    public static void task2() throws java.lang.IllegalAccessException, InvocationTargetException {
        Saver.saveTextContainer( new TextContainer("Tested content for second task of home work 3."));
    }
    public static void task3() throws java.lang.IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        System.out.println("Task 3:");

        System.out.println("Serialization:");
        Human toSerialize = new Human("Oleg", "Tsap", 22, 77);
        String out = Serializer.serialize(toSerialize);
        System.out.println(out);

        System.out.println("Deserialization:");
        Human toDeserialize = new Human();
        System.out.println("Before: " + toDeserialize.toString());
        Serializer.deserialize(out, toDeserialize);
        System.out.println("After: " + toDeserialize.toString());
    }

    public static void main(String[] args)  {
        try {
            task1();
            task2();
            task3();
        } catch (java.lang.IllegalAccessException | InvocationTargetException | NoSuchFieldException e){
            e.printStackTrace();
        }
    }
}
