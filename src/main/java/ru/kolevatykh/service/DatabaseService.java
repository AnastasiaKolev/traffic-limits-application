package ru.kolevatykh.service;

import lombok.Data;
import ru.kolevatykh.entity.LimitsPerHour;
import ru.kolevatykh.util.HibernateUtil;

import javax.persistence.EntityManager;

@Data
public class DatabaseService {

    private EntityManager entityManager;

    public void persist(LimitsPerHour limitPerHour) throws Exception {
        if (limitPerHour == null) throw new Exception("Limit not found.");
        entityManager = HibernateUtil.getEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(limitPerHour);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }

    public Integer findLimitByNameAndDate(String name) {
        entityManager = HibernateUtil.getEntityManager();
        return entityManager
                .createQuery("SELECT t.limitValue FROM limits_per_hour t " +
                        "WHERE t.limitName = :name ORDER BY t.effectiveDate DESC", Integer.class)
                .setParameter("name", name)
                .getResultList()
                .get(0);
    }
}
