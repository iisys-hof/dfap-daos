package daos.ordering;

import daos.JPADao;
import entities.*;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    public List<Ordering> getOrderingsForMachine(long machineId) {
        Query q = em.createQuery("SELECT s from Ordering s where s.machineId.machineId = :id", Ordering.class);
        q.setParameter("id", machineId);
        return q.getResultList();
    }

    public void updateOrdering(Ordering entity) {
        em.getTransaction().begin();
        em.merge(entity);
        em.flush();
        em.getTransaction().commit();
    }

    public void updateOrdering(Ordering entity, Tool te) {
        em.getTransaction().begin();
        entity.setToolId(em.find(Tool.class, te.getToolId()));
        em.merge(entity);

        em.getTransaction().commit();
    }

    public void updateFromCSV(Ordering entity) {
        Ordering old =  em.find(Ordering.class, entity.getOrderingId());

        if(old != null) {
            update(entity, old);
        }
        else {
            create(entity);
        }
    }

    private void create(Ordering entity) {
        //System.out.println("Old is null");

        entity.setDate(new Timestamp(new Date().getTime()));
        //System.out.println("Check Article");
        Article article = this.getArticle(entity.getArticleId());
        if (article != null)
            entity.setArticleId(article);

        //System.out.println("Check Machine");
        Machine machine = this.getMachine(entity.getMachineId());
        if (machine != null)
            entity.setMachineId(machine);

        //System.out.println("Check Tool");
        Tool tool = this.getTool(entity.getToolId());
        if (tool != null)
            entity.setToolId(tool);


        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
    }

    private void update(Ordering entity, Ordering old) {
        //System.out.println("Old is not null");

        updateCheckOrderValues(entity, old);
        updateCheckArticleToolAndMachine(entity, old);
        updateCheckOrderParts(entity, old);


    }

    private void updateCheckOrderValues(Ordering entity, Ordering old) {
        boolean sameEntity = old.isSame(entity);

        // Update entity if any of the values have been changed since last update
        if(!sameEntity) {
            em.getTransaction().begin();
            old.setSnr(entity.getSnr());
            old.setTotalQuantity(entity.getTotalQuantity());
            old.setTotalQuantityUnit(entity.getTotalQuantityUnit());
            old.setTypeOfContainer(entity.getTypeOfContainer());
            old.setQuantityPerContainer(entity.getQuantityPerContainer());
            old.setPiecesPerContainer(entity.getPiecesPerContainer());
            old.setQuantityOfContainer(entity.getQuantityOfContainer());
            old.setShortage(entity.getShortage());
            old.setLengthOfArticle(entity.getLengthOfArticle());
            old.setPrintDescription(entity.getPrintDescription());
            old.setProfileBody(entity.getProfileBody());
            old.setProfileGasket(entity.getProfileGasket());
            old.setProfileGasketQuantity(entity.getProfileGasketQuantity());
            old.setSchufoO(entity.getSchufoO());
            old.setSchufoU(entity.getSchufoU());
            //  System.out.println("ENTITY IS NOT SAME");
            em.getTransaction().commit();
        }
        //System.out.println("IS SAME: " + old.isSame(entity));
    }

    private void updateCheckArticleToolAndMachine(Ordering entity, Ordering old) {
        // Update Article if it has been changed since last update
        if(!Objects.equals(old.getArticleId().getName().trim(), entity.getArticleId().getName().trim())) {
            System.out.println("ENTITY ARTICLE IS NOT SAME");
            System.out.println("Check Article");
            em.getTransaction().begin();

            Article article = this.getArticle(entity.getArticleId());
            if (article != null)
                old.setArticleId(article);
            else
                old.setArticleId(entity.getArticleId());

            em.getTransaction().commit();

        }
        // Update Tool if it has been changed since last update
        if(!Objects.equals(old.getToolId().getName().trim(), entity.getToolId().getName().trim())) {
            System.out.println("ENTITY TOOL IS NOT SAME");

            em.getTransaction().begin();

            Tool tool = this.getTool(entity.getToolId());
            if (tool != null) {
                old.setToolId(tool);
            }
            else {
                old.setToolId(entity.getToolId());
            }

            em.getTransaction().commit();

        }
        // Update Machine if it has been changed since last update
        if(!Objects.equals(old.getMachineId().getName().trim(), entity.getMachineId().getName().trim())) {
            System.out.println("ENTITY MACHINE IS NOT SAME");
            System.out.println("Check Machine");
            em.getTransaction().begin();

            Machine machine = this.getMachine(entity.getMachineId());
            if (machine != null)
                old.setMachineId(machine);
            else
                old.setMachineId(entity.getMachineId());

            em.getTransaction().commit();
        }
    }

    private void updateCheckOrderParts(Ordering entity, Ordering old) {
        // Check if any OrderParts have been changed
        List<OrderingPartsList> parts = old.getOrderingPartsListList();

        boolean sameParts = true;
        sameParts = entity.getOrderingPartsListList().size() == old.getOrderingPartsListList().size();
        //System.out.println("SAME PARTS SIZE: " + sameParts);

        //Check for wrong entry, if one is wrong delete them and renew
        for (int i = 0; i < entity.getOrderingPartsListList().size(); i++) {
            for (int j = 0; j < parts.size(); j++) {
                if (entity.getOrderingPartsListList().get(i).isSame(parts.get(j))) {
                    break;
                }
                if(j == parts.size() - 1) {
                    sameParts = false;
                }
            }
            if(!sameParts) {
                System.out.println("WRONG ENTRY: " + entity.getOrderingPartsListList().get(i));
                break;
            }
        }
        // System.out.println("SAME PARTS: " +sameParts);

        if(!sameParts) {
            //DELETE and new
            TypedQuery<OrderingPartsList> q = em.createQuery("SELECT s from OrderingPartsList s where s.orderingId.orderingId = :id", OrderingPartsList.class);
            q.setParameter("id", entity.getOrderingId());
            System.out.println(q.getResultList());

            em.getTransaction().begin();
            for (OrderingPartsList e: q.getResultList() ) {
                em.remove(e);
            }
            em.getTransaction().commit();
            em.getTransaction().begin();
            for (OrderingPartsList e:entity.getOrderingPartsListList()) {
                e.setOrderingId(old);
                em.persist(e);
            }
            em.getTransaction().commit();
        }
    }

    private Article getArticle(Article name) {
        if(name !=null) {
            TypedQuery q = em.createQuery("SELECT s from Article s where s.name = :name", Article.class);
            q.setParameter("name", name.getName());
            if(q.getResultList().size() == 1)
                return (Article) q.getSingleResult();
        }
        return null;

    }

    private Machine getMachine(Machine name) {
        if(name !=null) {
            TypedQuery q = em.createQuery("SELECT s from Machine s where s.name = :name", Machine.class);
            q.setParameter("name", name.getName());
            if(q.getResultList().size() == 1)
                return (Machine) q.getSingleResult();
        }
        return null;

    }

    private Tool getTool(Tool name) {
        if(name !=null) {
            TypedQuery q = em.createQuery("SELECT s from Tool s where s.geometry = :geometry and s.variant =:variant and s.version =:version", Tool.class);
            q.setParameter("geometry", name.getGeometry());
            q.setParameter("variant", name.getVariant());
            q.setParameter("version", null);
            if(q.getResultList().size() == 1)
                return (Tool) q.getSingleResult();
        }
        return null;
    }

    private OrderingPartsList getPart(OrderingPartsList name) {
        if(name !=null) {
            TypedQuery q = em.createQuery("SELECT s from OrderingPartsList s where s.component = :name", OrderingPartsList.class);
            q.setParameter("name", name.getComponent());
            if(q.getResultList().size() == 1)
                return (OrderingPartsList) q.getSingleResult();
        }
        return null;
    }


}
