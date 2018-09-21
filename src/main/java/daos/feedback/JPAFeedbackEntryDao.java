/*
 * Copyright 2018 Thomas Winkler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package daos.feedback;

import daos.JPADao;
import entities.FeedbackEntry;
import entities.Machine;
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

        System.out.println("fdhdhadhadtfhbnadgfhn" + q);
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
        fbOld.setEmployeeNumber(fbNew.getEmployeeNumber());
        fbOld.setShift(fbNew.getShift());

        SubProcess subProcess = em.find(SubProcess.class, fbNew.getSubProcessId().getSubProcessId());
        fbOld.setSubProcessId(subProcess);

        System.out.println("NUDUIDUDIDIDUDIDODUDI");
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


    public List<FeedbackEntry> getFeedbacksInDaterangeForMachine(Date startDate, Date endDate, Long m) {
        TypedQuery<FeedbackEntry> q = em.createQuery("SELECT DISTINCT fe from FeedbackEntry fe where fe.startTime >= :startDate and fe.startTime <= :endDate and fe.orderingId.machineId.machineId =:machineId order by fe.orderingId.orderingId, fe.startTime", FeedbackEntry.class);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        q.setParameter("machineId", m);


        return q.getResultList();

    }

    public void deleteFeedback(Long feedbackEntryId) {
        em.getTransaction().begin();
        FeedbackEntry e =  em.find(FeedbackEntry.class, feedbackEntryId);
        if (e != null) {
            em.remove(e);
        }
        em.getTransaction().commit();
    }
}
