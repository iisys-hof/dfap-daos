package daos.testInstruction;


import entities.TestInstruction;
import entities.TestInstructionValue;

import java.util.List;

public interface TestInstructionDao {
    List readAllTestInstructions();

    TestInstruction readTestInstruction(long id);

    TestInstruction readTestInstructionForToolId(long id);

    void updateTestInstruction(TestInstruction e);

    void updateTestInstructionValues(TestInstructionValue e);
}
