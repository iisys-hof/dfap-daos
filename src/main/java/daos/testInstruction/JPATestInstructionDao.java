package daos.testInstruction;

import daos.JPADao;
import entities.TestInstruction;
import entities.TestInstructionEntry;
import entities.TestInstructionValue;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@RequestScoped
public class JPATestInstructionDao extends JPADao implements TestInstructionDao {
    @Override
    public List readAllTestInstructions() {

        TypedQuery<TestInstruction>q  = em.createQuery("select t from TestInstruction t", TestInstruction.class);
        return  q.getResultList();
    }

    @Override
    public TestInstruction readTestInstruction(long id) {
        return em.find(TestInstruction.class, id);
    }

    public TestInstruction readTestInstructionForName(String name) {
        TypedQuery<TestInstruction>q  = em.createQuery("select t from TestInstruction t where t.name =:name", TestInstruction.class);
        q.setParameter("name", name);
        List<TestInstruction> tiList = q.getResultList();
        if(!tiList.isEmpty()) {
            return tiList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public TestInstruction readTestInstructionForToolId(long id) {
        TypedQuery<TestInstruction>q  = em.createQuery("select t.testInstructionId from Tool t where t.toolId =:toolId", TestInstruction.class);
        q.setParameter("toolId", id);
        List<TestInstruction> tiList = q.getResultList();
        if(!tiList.isEmpty()) {
            return tiList.get(0);
        } else {
            return null;
        }
    }

    public List<TestInstructionEntry> readTestInstructionEntitiesForToolId(long id) {
        TypedQuery<TestInstructionEntry> q  = em.createQuery("select s from TestInstructionEntry s where s.toolId.toolId =:toolId", TestInstructionEntry.class);
        q.setParameter("toolId", id);
        return q.getResultList();
    }
    public List<TestInstructionEntry> readTestInstructionEntitiesForToolId(Date startDate, Date endDate, long id) {
        TypedQuery<TestInstructionEntry> q  = em.createQuery("select s from TestInstructionEntry s where s.toolId.toolId =:toolId and s.date >= :startDate and s.date <= :endDate", TestInstructionEntry.class);
        q.setParameter("toolId", id);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }

    public void updateTestInstruction(TestInstruction e, TestInstructionEntry tie) {
        em.getTransaction().begin();
        e.getTestInstructionEntryList().add(tie);
        em.flush();
        em.getTransaction().commit();
    }

    @Override
    public void updateTestInstruction(TestInstruction e) {
        em.getTransaction().begin();
        em.merge(e);
        em.getTransaction().commit();
    }

    @Override
    public void updateTestInstructionValues(TestInstructionValue e) {

    }

    public void storeTestInstruction(TestInstruction e) {
        em.getTransaction().begin();
        em.persist(e);
        em.flush();
        em.getTransaction().commit();

    }
}