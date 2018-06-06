package daos.testInstruction;

import entities.TestInstructionProperty;

import java.util.List;

public interface TestInstructionPropertyDao {

    TestInstructionProperty readTestInstructionProperty(long id);

    List<TestInstructionProperty> readTestInstructionPropertiesForToolId(long id);

    void storeTestInstructionProperty(TestInstructionProperty e);

    void updateTestInstructionProperty(TestInstructionProperty e);

}
