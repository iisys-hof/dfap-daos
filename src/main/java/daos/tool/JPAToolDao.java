package daos.tool;

import daos.JPADao;
import entities.TestInstruction;
import entities.Tool;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import java.util.List;

@RequestScoped
public class JPAToolDao extends JPADao implements ToolDao {
    @Override
    public List<Tool> readAllTools() {
        TypedQuery q = em.createQuery("SELECT DISTINCT pe from Tool pe", Tool.class);
        System.out.println("readAllTools");
        return q.getResultList();
    }

    public long findToolIdForName(String name) {
        System.out.println("TT NAME: " + name);
        for (Tool pe : this.readAllTools()) {
            System.out.println("YY NAME: " + pe.getName());
            if(pe.getName().equals(name))
                return pe.getToolId();
        }
        return -1;
    }

    public Tool findToolForName(String name) {
        for (Tool pe : this.readAllTools()) {
            if(pe.getName().equals(name))
                return pe;
        }
        return null;
    }

    public Tool readToolForId(int id) {
        return em.find(Tool.class, id);
    }

    public Tool storeTool(Tool tool) {
        em.getTransaction().begin();
        em.persist(tool);
        em.flush();
        em.getTransaction().commit();
        return tool;
    }

    public void updateTool(Tool tool) {
        em.getTransaction().begin();
        em.merge(tool);
        em.getTransaction().commit();
    }
    public void updateTool(Tool tool, TestInstruction ti) {
        em.getTransaction().begin();
        TestInstruction tin = em.merge(ti);
        tool.setTestInstructionId(tin);
        em.flush();
        em.getTransaction().commit();
    }
}
