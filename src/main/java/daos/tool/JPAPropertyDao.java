package daos.tool;


import daos.JPADao;
import entities.Property;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import java.util.List;

@RequestScoped
public class JPAPropertyDao extends JPADao implements PropertyDao  {
    @Override
    public List readAllProperties() {
        TypedQuery q = em.createQuery("SELECT DISTINCT pe from Property pe", Property.class);
        System.out.println("readAllProperties");
        return q.getResultList();
    }


}
