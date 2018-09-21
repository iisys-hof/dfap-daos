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
