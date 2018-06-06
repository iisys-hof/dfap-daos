package daos.testInstruction;

import daos.JPADao;
import entities.TestInstructionProperty;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import java.util.List;

@RequestScoped
public class JPATestInstructionPropertyDao extends JPADao implements TestInstructionPropertyDao {
    @Override
    public TestInstructionProperty readTestInstructionProperty(long id) {
        return em.find(TestInstructionProperty.class, id);
    }

    @Override
    public List<TestInstructionProperty> readTestInstructionPropertiesForToolId(long id) {
        TypedQuery<TestInstructionProperty> q = em.createQuery("select t.testInstructionId.testInstructionPropertyList from Tool t where t.toolId =:toolId", TestInstructionProperty.class);
        q.setParameter("toolId", id);
        return q.getResultList();
    }

    @Override
    public void storeTestInstructionProperty(TestInstructionProperty e) {
        em.getTransaction().begin();
        em.persist(e);
        em.flush();
        em.getTransaction().commit();
    }

    @Override
    public void updateTestInstructionProperty(TestInstructionProperty e) {
      TestInstructionProperty tipOld =  em.find(TestInstructionProperty.class, e.getTestInstructionPropertyId());
      if(tipOld != null) {
          em.getTransaction().begin();
          tipOld.setActive(e.getActive());
          tipOld.setAdditionalInfo(e.getAdditionalInfo());
          tipOld.setName(e.getName());
          tipOld.setType(e.getType());
          em.flush();
          em.getTransaction().commit();
      }
    }
}

