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
