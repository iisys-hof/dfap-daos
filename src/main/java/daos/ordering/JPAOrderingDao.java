package daos.ordering;

import daos.JPADao;
import entities.Ordering;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@RequestScoped
public class JPAOrderingDao extends JPADao implements OrderingDao {
    @Override
    public List readAllOrders() {
        Query q = em.createQuery("SELECT s from Ordering s", Ordering.class);

        return q.getResultList();
    }

    @Override
    public Ordering readOrdering(long orderId) {
        return em.find(Ordering.class, orderId);
    }

    public List<Ordering> getOrdersInDaterange(Date startDate, Date endDate) {
        TypedQuery<Ordering> q = em.createQuery("SELECT DISTINCT o from Ordering o where o.date >= :startTime and o.date <= :endTime", Ordering.class);
        q.setParameter("startTime", startDate);
        q.setParameter("endTime", endDate);
        return q.getResultList();

    }

   
}
