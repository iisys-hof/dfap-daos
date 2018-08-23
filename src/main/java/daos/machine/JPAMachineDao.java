package daos.machine;

import daos.DfapEntity;
import daos.JPADao;
import entities.Machine;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import java.util.List;


@RequestScoped
public class JPAMachineDao extends JPADao implements MachineDao {

    @Override
    public List<Machine> getAllMachines() {
        TypedQuery<Machine> q = em.createQuery("SELECT s from Machine s", Machine.class);
        return  q.getResultList();
    }

    public long findMachineIdForName(String name) {
        //System.out.println("MM NAME: " + name);
        for (Machine pe : this.getAllMachines()) {
            //System.out.println("XX NAME: " + pe.getName());

            if(pe.getName().equals(name))
                return pe.getMachineId();
        }
        return -1;
    }

    public Machine findMachineForName(String name) {
        for (Machine pe : this.getAllMachines()) {
            if(pe.getName().equals(name))
                return pe;
        }
        return null;
    }


    public Machine findMachineForId(Long machineId) {
        return em.find(Machine.class, machineId);
    }


    public void update(Machine machine, String printerAddress) {
       em.getTransaction().begin();
       machine.setPrinterAddress(printerAddress);
       em.getTransaction().commit();
    }
}
