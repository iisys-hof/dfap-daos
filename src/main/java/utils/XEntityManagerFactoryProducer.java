package utils;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@RequestScoped
public class XEntityManagerFactoryProducer {

    @PersistenceUnit(unitName="dfap")
    private EntityManagerFactory factory;
    public static EntityManager em;

    @Produces
    @XEntityManagerFactory
    public EntityManager produceEntityManager(final InjectionPoint injectionPoint) {

        em = factory.createEntityManager();
        return em;
    }
}