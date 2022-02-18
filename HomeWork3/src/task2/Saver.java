package task2;

import task1.Calculator;
import task1.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Saver {
    public static void saveTextContainer(TextContainer tc) throws java.lang.IllegalAccessException, InvocationTargetException {
        String path = TextContainer.class.getAnnotation(SaveTo.class).path();
        Method[] methods = TextContainer.class.getMethods();
        for(Method m : methods){
            if(m.isAnnotationPresent(SaveMethod.class)){
                m.invoke(tc, path);
                System.out.println("Task 2: Method '" + m.getName() +  "' was called with path '" + path + "'. Now check saved file.");
            }
        }
    }
}
