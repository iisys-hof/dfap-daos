package daos.testInstruction;

import daos.JPADao;
import entities.TestInstructionEntry;

import javax.enterprise.context.RequestScoped;
import java.util.List;

@RequestScoped
public class JPATestInstructionEntryDao extends JPADao implements TestInstructionEntryDao {


    @Override
    public TestInstructionEntry readTestInstructionEntry(long id) {
        return em.find(TestInstructionEntry.class, id);
    }

    @Override
    public List<TestInstructionEntry> readTestInstructionEntryForToolId(long id) {
        return null;
    }

    @Override
    public void storeTestInstructionEntry(TestInstructionEntry e) {

    }

    @Override
    public void updateTestInstructionEntry(TestInstructionEntry e) {
        TestInstructionEntry tie = em.find(TestInstructionEntry.class, e.getTestInstructionEntryId());
        System.out.println(tie.getTestInstructionEntryId());
        em.getTransaction().begin();
        tie.setTestInstructionValueList(e.getTestInstructionValueList());
        em.merge(tie);
        em.flush();
        em.getTransaction().commit();

    }
}
