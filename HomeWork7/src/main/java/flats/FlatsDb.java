package flats;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.*;

public class FlatsDb {
    private static String persistenceUnitName = "JPAFlats";
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

    public static void addFlat(Flat fl) {
        em.getTransaction().begin();
        try {
            em.persist(fl);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public static void deleteFlat(long id) {
        Flat fl = em.getReference(Flat.class, id);
        if (fl == null) {
            System.out.println("Flat not found!");
            return;
        }

        em.getTransaction().begin();
        try {
            em.remove(fl);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public static void updateFlatsInfo() {
        em.flush();
    }

    public static Flat getFlat(long id) {
        try {
            Query query = em.createQuery("SELECT x FROM Flat x WHERE x.id = :id", Flat.class);
            query.setParameter("id", id);
            return (Flat) query.getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("Flat not found!");
            return null;
        } catch (NonUniqueResultException ex) {
            System.out.println("Non unique result!");
            return null;
        }
    }

    private static void setQueryParameter(String fieldName, String value, Query query) throws NoSuchFieldException, IllegalArgumentException {
        Field field = Flat.class.getDeclaredField(fieldName);
        if(field.getType() == int.class){
            query.setParameter(fieldName, Integer.parseInt(value));
        } else if(field.getType() == double.class){
            query.setParameter(fieldName, Double.parseDouble(value));
        } else if(field.getType() == long.class){
            query.setParameter(fieldName, Long.parseLong(value));
        } else if(field.getType() == String.class) {
            query.setParameter(fieldName, value);
        } else {
            throw new IllegalArgumentException();
        }
    }


    public static ArrayList<Flat> getFlatsList(HashMap<String, String> filters) {
        String queryStr = "SELECT f FROM Flat f";
        if(filters.size() > 0) {
            queryStr += " WHERE ";
        }
        boolean first = true;
        for(String key : filters.keySet()){
            queryStr += (first ? "f." : " AND f.") + key + "=:" + key;
            if(first) { first = false; }
        }
        Query query = em.createQuery(queryStr);
        try {
            for (Map.Entry<String, String> pair : filters.entrySet()) {
                setQueryParameter(pair.getKey(), pair.getValue(), query);
            }
        } catch (NoSuchFieldException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return (ArrayList<Flat>) query.getResultList();
    }

    public static ArrayList<Flat> getFlatsList() {
        return getFlatsList(new HashMap<String, String>());
    }
}
