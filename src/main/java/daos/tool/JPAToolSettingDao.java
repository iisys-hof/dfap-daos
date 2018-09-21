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

package daos.tool;


import daos.JPADao;
import daos.machine.JPAMachineDao;
import entities.*;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
@RequestScoped
public class JPAToolSettingDao extends JPADao implements ToolSettingDao{

    private   List<Property> properties;



    @Inject
    private JPAPropertyDao propertyDao;
    @Inject
    private JPAToolDao toolDao;
    @Inject
    private JPAMachineDao machineDao;


    public JPAToolSettingDao() {


    }
    public List readToolSettingsForMachineAndTool(Integer machineId, Integer toolId) {

        //Query q = em.createQuery("SELECT DISTINCT ts from ToolSetting ts JOIN ts.settingValues sv where ts.tool.toolId= 1 and ts.machine.machineId = 2");
        TypedQuery q = em.createQuery("SELECT DISTINCT ts from ToolSetting ts where ts.machineId.machineId = :machineId and ts.toolId.toolId = :toolId", ToolSetting.class);
        q.setParameter("machineId", machineId);
        q.setParameter("toolId", toolId);

        return q.getResultList();
    }
    public List readToolSettingsForMachineAndToolAndArticle(Integer machineId, Integer toolId, Integer articleId) {
        TypedQuery q = em.createQuery("SELECT DISTINCT ts from ToolSetting ts where ts.machineId.machineId = :machineId and ts.toolId.toolId = :toolId and ts.articleId.idArticle = :articleId", ToolSetting.class);
        q.setParameter("machineId", machineId);
        q.setParameter("toolId", toolId);
        q.setParameter("articleId", articleId);

        return q.getResultList();
    }
    @Override
    public List<ToolSetting> readToolSettingsForMachineAndTool(String machine, String toolName) {
        Tool t = new Tool(null, toolName);
        TypedQuery q = em.createQuery("SELECT DISTINCT ts from ToolSetting ts where ts.machineId.name = :machineId and ts.toolId.geometry = :geometry and ts.toolId.variant = :variant", ToolSetting.class);
        q.setParameter("machineId", machine);
        q.setParameter("geometry", t.getGeometry());
        q.setParameter("variant", t.getVariant());

        return q.getResultList();
    }

    public List<ToolSetting> readToolSettingsForTool(String toolName) {
        Tool t = new Tool(null, toolName);

        TypedQuery<ToolSetting> q = em.createQuery("SELECT DISTINCT ts from ToolSetting ts where ts.toolId.geometry = :geometry and ts.toolId.variant = :variant", ToolSetting.class);
        q.setParameter("geometry", t.getGeometry());
        q.setParameter("variant", t.getVariant());

        return q.getResultList();
    }

    public List<ToolSetting> readToolSettingsForMachineToolAndVersion(String machineName, String toolName, String version) {
        Tool t = new Tool(null, toolName);

        TypedQuery<ToolSetting> q = em.createQuery("SELECT DISTINCT ts from ToolSetting ts where ts.machineId.name = :machineName and ts.toolId.geometry = :geometry and ts.toolId.variant = :variant and ts.toolId.version =:version", ToolSetting.class);
        q.setParameter("machineName", machineName);
        q.setParameter("geometry", t.getGeometry());
        q.setParameter("variant", t.getVariant());
        q.setParameter("version", version);

        return q.getResultList();
    }

    public List<ToolSetting> readToolSettingsForToolAndVersion(String toolName, String version) {
        Tool t = new Tool(null, toolName);
        System.out.println(t);
        TypedQuery<ToolSetting> q = em.createQuery("SELECT DISTINCT ts from ToolSetting ts where ts.toolId.geometry = :geometry and ts.toolId.variant = :variant and ts.toolId.version =:version ", ToolSetting.class);
        q.setParameter("geometry", t.getGeometry());
        q.setParameter("variant", t.getVariant());
        q.setParameter("version", version);

        return q.getResultList();
    }
/*
    @Override
    public void writeToolSettings(List <ToolSetting> toolSettings, String machine, String tool) {

        properties = propertyDao.readAllProperties();
        for(ToolSetting toolSetting : toolSettings) {
            ToolSetting x = em.find(ToolSetting.class, toolSetting.getToolSettingId());

            if(x == null) {
                em.getTransaction().begin();
                x = new ToolSetting();
                x.setDate(toolSetting.getDate());
                x.setMachineId(new Machine());
                x.setNote(toolSetting.getNote());
                x.setOntop(toolSetting.getOntop());

                System.out.println("MACHINE" + machineDao.findMachineIdForName(machine));
                x.getMachineId().setMachineId(machineDao.findMachineIdForName(machine));
                x.setToolId(new Tool());
                x.getToolId().setToolId(toolDao.findToolIdForName(tool));

                em.persist(x);
                em.flush();
                em.getTransaction().commit();
                //System.out.println(x.getToolSettingId());

            }
            em.getTransaction().begin();
            x.setNote(toolSetting.getNote());
            x.setOntop(toolSetting.getOntop());
            this.findPrId(toolSetting.getSettingValueList());

            List<SettingValue> filtered = toolSetting.getSettingValueList().stream()
                    .filter(b -> !b.getValue().equals(""))
                    .collect(Collectors.toList());
            toolSetting.setSettingValueList(filtered);

            for (SettingValue sv : toolSetting.getSettingValueList()) {

                sv.setToolSettingId(new ToolSetting());
                sv.getToolSettingId().setToolSettingId(x.getToolSettingId());
            }
            x.setSettingValueList(toolSetting.getSettingValueList());
            em.merge(x);
            em.getTransaction().commit();
        }
    }
*/
    public void writeToolSettings(List <ToolSetting> toolSettings, String machine, String tool, String version) {

        properties = propertyDao.readAllProperties();
        for(ToolSetting toolSetting : toolSettings) {
            ToolSetting x = em.find(ToolSetting.class, toolSetting.getToolSettingId());

            if(x == null) {
                em.getTransaction().begin();
                x = new ToolSetting();
                x.setDate(toolSetting.getDate());
                x.setMachineId(new Machine());
                x.setNote(toolSetting.getNote());
                x.setOntop(toolSetting.getOntop());

                System.out.println("MACHINE" + machineDao.findMachineIdForName(machine));
                x.getMachineId().setMachineId(machineDao.findMachineIdForName(machine));
                x.setToolId(new Tool());
                System.out.println("Tool" + toolDao.findToolForNameAndVersion(tool, version));
                x.getToolId().setToolId(toolDao.findToolForNameAndVersion(tool, version).getToolId());

                em.persist(x);
                em.flush();
                em.getTransaction().commit();

            }
            em.getTransaction().begin();
            x.setNote(toolSetting.getNote());
            x.setOntop(toolSetting.getOntop());
            this.findPrId(toolSetting.getSettingValueList());

            List<SettingValue> filtered = toolSetting.getSettingValueList().stream()
                    .filter(b -> !b.getValue().equals(""))
                    .collect(Collectors.toList());
            toolSetting.setSettingValueList(filtered);

            for (SettingValue sv : toolSetting.getSettingValueList()) {

                sv.setToolSettingId(new ToolSetting());
                sv.getToolSettingId().setToolSettingId(x.getToolSettingId());
            }
            x.setSettingValueList(toolSetting.getSettingValueList());
            em.merge(x);
            em.getTransaction().commit();
        }
    }

    private void findPrId(List<SettingValue> svs) {
        for (SettingValue sv : svs) {
            sv.getPropertyId().setPropertyId(findProperty(sv.getPropertyId().getName()));
        }
    }

    private long findProperty(String name) {
        for (Property pe : this.properties) {
            if(pe.getName().equals(name))
                return pe.getPropertyId();
        }
        return -1;
    }


    public List<Machine> getMachinesForTool(Long toolId) {
        TypedQuery<Machine> q = em.createQuery("SELECT DISTINCT o.machineId from Ordering o where o.toolId.toolId = :toolId", Machine.class);
        q.setParameter("toolId", toolId);
        return q.getResultList();
    }

    public List<Machine> getMachinesForToolViaTS(Long toolId) {
        TypedQuery<Machine> q = em.createQuery("SELECT DISTINCT ts.machineId from ToolSetting ts where ts.toolId.toolId = :toolId", Machine.class);
        q.setParameter("toolId", toolId);
        return q.getResultList();
    }

    public List<Machine> getMachinesForToolViaTS(String toolName, String version) {

        Tool t = new Tool(null, toolName);
        System.out.println(t);
        TypedQuery<Machine> q = em.createQuery("SELECT DISTINCT ts.machineId from ToolSetting ts where ts.toolId.geometry = :geometry " +
                                                                                                     "and ts.toolId.variant = :variant " +
                                                                                                     "and ts.toolId.version = :version", Machine.class);

        q.setParameter("geometry", t.getGeometry());
        q.setParameter("variant", t.getVariant());
        q.setParameter("version", version);


        return q.getResultList();
    }

    public List<Machine> getMachinesForToolViaTS(String toolName) {

        Tool t = new Tool(null, toolName);
        System.out.println(t);
        TypedQuery<Machine> q = em.createQuery("SELECT DISTINCT ts.machineId from ToolSetting ts where ts.toolId.geometry = :geometry " +
                "and ts.toolId.variant = :variant ", Machine.class);

        q.setParameter("geometry", t.getGeometry());
        q.setParameter("variant", t.getVariant());

        return q.getResultList();
    }

    public Machine getMachine(long machineId) {

        return em.find(Machine.class, machineId);
    }
}
