package ru.kolevatykh.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import ru.kolevatykh.entity.LimitsPerHour;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class HibernateUtil {

    private static final EntityManagerFactory entityManagerFactory = factory();

    public static EntityManagerFactory factory() {
        Properties props = new Properties();
        InputStream is = HibernateUtil.class.getResourceAsStream("/db.properties");
        try {
            props.load(is);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        final Map<String, String> settings = new HashMap<>();
        settings.put(Environment.DRIVER, props.getProperty("db.driver"));
        settings.put(Environment.URL, props.getProperty("db.url"));
        settings.put(Environment.USER, props.getProperty("db.username"));
        settings.put(Environment.PASS, props.getProperty("db.password"));
        settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        settings.put(Environment.HBM2DDL_AUTO, "update");
        settings.put(Environment.SHOW_SQL, "true");
        final StandardServiceRegistryBuilder registryBuilder
                = new StandardServiceRegistryBuilder();
        registryBuilder.applySettings(settings);
        final StandardServiceRegistry registry = registryBuilder.build();
        final MetadataSources sources = new MetadataSources(registry);
        sources.addAnnotatedClass(LimitsPerHour.class);
        final Metadata metadata = sources.getMetadataBuilder().build();
        return metadata.getSessionFactoryBuilder().build();
    }

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
