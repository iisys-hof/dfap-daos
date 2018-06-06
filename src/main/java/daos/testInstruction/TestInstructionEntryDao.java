package daos.testInstruction;

import entities.TestInstructionEntry;

import java.util.List;

public interface TestInstructionEntryDao {

    TestInstructionEntry readTestInstructionEntry(long id);

    List<TestInstructionEntry> readTestInstructionEntryForToolId(long id);

    void storeTestInstructionEntry(TestInstructionEntry e);

    void updateTestInstructionEntry(TestInstructionEntry e);
}
