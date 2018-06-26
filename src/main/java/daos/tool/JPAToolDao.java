package daos.tool;

import daos.JPADao;
import entities.TestInstruction;
import entities.Tool;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class JPAToolDao extends JPADao implements ToolDao {
    @Override
    public List<Tool> readAllTools() {
        TypedQuery q = em.createQuery("SELECT DISTINCT pe from Tool pe", Tool.class);
        System.out.println("readAllTools");
        return q.getResultList();
    }

    public List<Tool> readAllBaseTools() {
        TypedQuery q = em.createQuery("SELECT DISTINCT pe from Tool pe where pe.version =: version", Tool.class);
        q.setParameter("version", null);
        System.out.println("readAllTools");
        return q.getResultList();
    }


    public List<String> readAllVersionsOfTool(String name) {
        Tool t = new Tool(null, name);
        String geo = t.getGeometry();
        String var = t.getVariant();
        TypedQuery<Tool> q = em.createQuery("select distinct t From Tool t where t.geometry =:geometry and t.variant =:variant", Tool.class);
        q.setParameter("geometry", geo);
        q.setParameter("variant", var);

        List<Tool> toolEntities = q.getResultList();
        List<String> versions = new ArrayList<>();
        for (Tool te: toolEntities) {
            if(te.getVersion() != null)
                versions.add(te.getVersion());
        }

        return versions;
    }

    public List<Tool> findVersionsOfTool(String toolName){
        Tool t = new Tool(null, toolName);
        System.out.println(t);
        TypedQuery<Tool> q = em.createQuery("SELECT DISTINCT pe from Tool pe where pe.geometry = :geometry and pe.variant = :variant", Tool.class);
        q.setParameter("geometry", t.getGeometry());
        q.setParameter("variant", t.getVariant());
        System.out.println(q.getResultList().size());
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

    public Tool findToolForNameAndVersion(String name, String version) {
        Tool t = new Tool(null, name);
        String geo = t.getGeometry();
        String var = t.getVariant();

        TypedQuery<Tool> q = em.createQuery("select distinct t From Tool t where t.geometry =:geometry and t.variant =:variant and t.version =:version", Tool.class);
        q.setParameter("geometry", geo);
        q.setParameter("variant", var);
        q.setParameter("version", version);

        if (q.getResultList().size() == 1) {
            return q.getResultList().get(0);
        }
        else {
            return null;
        }

    }

    public Tool readToolForId(long id) {
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
