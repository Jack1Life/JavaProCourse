import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class DbHandler {
    private final static String persistenceUnitName = "OrdersUnit";
    private static EntityManagerFactory emf;
    private static EntityManager em;

    public static void openConnection() {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        em = emf.createEntityManager();
    }

    public static void closeConnection() {
        em.close();
        emf.close();
    }

    public static void add(Object fl) {
        em.getTransaction().begin();
        try {
            em.persist(fl);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public static void delete(long id, Class<?> cl) {
        Object obj = em.getReference(cl, id);
        if (obj == null) {
            System.out.println("Object not found!");
            return;
        }
        em.getTransaction().begin();
        try {
            em.remove(obj);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public static void update() {
        em.getTransaction().begin();
        try {
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public static <T> T performUnderTransaction(Callable<T> action) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            T result = action.call();
            transaction.commit();

            return result;
        } catch (Exception ex) {
            if (transaction.isActive())
                transaction.rollback();

            throw new RuntimeException(ex);
        }
    }

    public static Object get(long id, Class<?> cl) {
        try {
            Query query = em.createQuery("SELECT x FROM " + cl.getName() + " x WHERE x.id = :id", cl);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("Flat not found!");
            return null;
        } catch (NonUniqueResultException ex) {
            System.out.println("Non unique result!");
            return null;
        }
    }

    private static void setQueryParameter(String fieldName, String paramName, String value, Query query, Class<?> cl) throws NoSuchFieldException, IllegalArgumentException {
        Field field = cl.getDeclaredField(fieldName);
        if(field.getType() == Integer.class){
            query.setParameter(paramName, Integer.parseInt(value));
        } else if(field.getType() == Double.class){
            query.setParameter(paramName, Double.parseDouble(value));
        } else if(field.getType() == Long.class){
            query.setParameter(paramName, Long.parseLong(value));
        } else if(field.getType() == String.class) {
            query.setParameter(paramName, value);
        }else {
            /*Id parameters by default*/
            query.setParameter(paramName, Long.parseLong(value));
        }
    }

    private static void setQueryParameter(String fieldName, String value, Query query, Class<?> cl) throws NoSuchFieldException, IllegalArgumentException {
        setQueryParameter(fieldName, fieldName, value, query, cl);
    }

    private static FilterType getFilterType(Class<?> cl, String fieldName) throws NoSuchFieldException {
        Field field = cl.getDeclaredField(fieldName);
        if(field == null) {
            System.out.println("Unknown filter field: "  + fieldName);
            return  null;
        }
        if(!field.isAnnotationPresent(FilterField.class)) {
            System.out.println("Unknown filter: "  + fieldName);
            return  null;
        }
        return field.getAnnotation(FilterField.class).type();

    }
    private static String formQueryString(HashMap<String, String> filters, Class<?> cl) throws NoSuchFieldException {
        String queryStart = "SELECT x FROM " + cl.getName() + " x";
        String queryJoins = "";
        String queryFilters = "";
        int joinNum = 1;

        boolean first = true;
        for(String key : filters.keySet()) {
            queryFilters += (first ? " WHERE " : " AND ");
            FilterType type = getFilterType(cl, key);
            String value = filters.get(key);
            queryFilters += "(";
            switch (type) {
                case NUMERIC:
                    queryFilters += "x." + key;
                    if(value.indexOf(">") >= 0) {
                        queryFilters += " > :" + key + "_level";
                    }  else if(value.indexOf("<") >= 0) {
                        queryFilters += " < :" + key + "_level";
                    } else if (value.indexOf("/") >= 0) {
                        String[] between = value.split("/");
                        queryFilters += " BETWEEN " + " :" + key + "_bottom" + " AND " + " :" + key + "_top";
                    } else {
                        queryFilters +=  "=:" + key;
                    }
                    break;
                case OBJ_ID:
                    String joinName = "x" + joinNum;
                    queryJoins += " JOIN x." + key + " " + joinName ;
                    queryFilters += joinName + ".id=:" + key + "_id";
                    joinNum++;
                    break;
                default:
                    queryFilters += "x." + key;
                    queryFilters +=  "=:" + key;
            }
            queryFilters += ")";
            if(first) { first = false; }
        }
        return queryStart + queryJoins + queryFilters;
    }

    private static void setFilterParameters(HashMap<String, String> filters, Class<?> cl, Query query) throws NoSuchFieldException {
        for (Map.Entry<String, String> pair : filters.entrySet()) {
            FilterType type = getFilterType(cl, pair.getKey());
            String value = pair.getValue();
            switch (type) {
                case NUMERIC:
                    String[] numValues = value.replaceAll("[^-?.?0-9]+", " ").trim().split(" ");
                    if(value.indexOf("/") >= 0) {
                        setQueryParameter(pair.getKey(), pair.getKey() + "_bottom", numValues[0], query, cl);
                        setQueryParameter(pair.getKey(), pair.getKey() + "_top", numValues[1], query, cl);
                    } else if(value.indexOf(">") >= 0 || value.indexOf("<") >= 0){
                        setQueryParameter(pair.getKey(), pair.getKey() + "_level", numValues[0], query, cl);
                    } else{
                        setQueryParameter(pair.getKey(), numValues[0], query, cl);
                    }
                    break;
                case OBJ_ID:
                    setQueryParameter(pair.getKey(), pair.getKey() + "_id", value, query, cl);
                    break;
                default:
                    setQueryParameter(pair.getKey(), value, query, cl);
            }
        }
    }

    public static Object getFilteredList(HashMap<String, String> filters, Class<?> cl) {
        try {
            String queryStr = formQueryString(filters, cl);
            System.out.println(queryStr);
            Query query = em.createQuery(queryStr);
            setFilterParameters(filters, cl, query);
            return query.getResultList();
        } catch (NoSuchFieldException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getList(Class<?> cl) {
        return getFilteredList(new HashMap<String, String>(), cl);
    }
}
