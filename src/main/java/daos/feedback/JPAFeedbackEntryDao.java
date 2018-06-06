package daos.feedback;

import daos.JPADao;
import entities.FeedbackEntry;
import entities.Ordering;
import entities.SubProcess;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@RequestScoped
public class JPAFeedbackEntryDao extends JPADao implements FeedbackEntryDao {

    @Override
    public List readAllFeedbacks() {
        Query q = em.createQuery("SELECT s from FeedbackEntry s", FeedbackEntry.class);

        return q.getResultList();
    }

    @Override
    public List getFeedbackForOrder(long orderId) {
        Query q = em.createQuery("SELECT DISTINCT fe from FeedbackEntry fe where fe.orderingId.orderingId = :orderId", FeedbackEntry.class);
        q.setParameter("orderId", orderId);
        return q.getResultList();
    }

    public List<FeedbackEntry> getFeedbacksInDaterange(Date startDate, Date endDate) {
        TypedQuery<FeedbackEntry> q = em.createQuery("SELECT DISTINCT fe from FeedbackEntry fe where fe.startTime >= :startDate and fe.startTime <= :endDate order by fe.orderingId.orderingId, fe.startTime", FeedbackEntry.class);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);


        return q.getResultList();

    }

    public List<FeedbackEntry> getFinishedFeedbacksInDaterange(Date startDate, Date endDate) {
        TypedQuery<FeedbackEntry> q = em.createQuery("SELECT DISTINCT fe from FeedbackEntry fe where fe.startTime >= :startDate and fe.startTime <= :endDate and fe.orderingId.orderFinished =:finished order by fe.orderingId.orderingId, fe.startTime", FeedbackEntry.class);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        q.setParameter("finished", 1);


        return q.getResultList();

    }


    public void updateFeedback(FeedbackEntry fbNew) {
        FeedbackEntry fbOld = em.find(FeedbackEntry.class, fbNew.getFeedbackEntryId());
        em.getTransaction().begin();
        fbOld.setStartTime(fbNew.getStartTime());
        fbOld.setEndTime(fbNew.getEndTime());
        fbOld.setAccepted(fbNew.getAccepted());
        fbOld.setRejected(fbNew.getRejected());
        fbOld.setSpeed(fbNew.getSpeed());
        fbOld.setWeight(fbNew.getWeight());
        System.out.println(fbOld);
        System.out.println(fbNew);
        em.getTransaction().commit();
    }

    public void storeFeedback(FeedbackEntry fb, long orderId) {
        em.getTransaction().begin();
        Ordering order = em.find(Ordering.class, orderId);
        SubProcess subProcess = em.find(SubProcess.class, fb.getSubProcessId().getSubProcessId());

        fb.setOrderingId(order);
        fb.setSubProcessId(subProcess);

        System.out.println("sP  " +fb);

        em.persist(fb);
        em.getTransaction().commit();
    }
    public void storeFeedbackList(List<FeedbackEntry> fb, long orderId) {

        for (FeedbackEntry f: fb) {
            if(f.getFeedbackEntryId() == null || f.getFeedbackEntryId() == 0) {
               this.storeFeedback(f, orderId);
            }
        }
    }


}
