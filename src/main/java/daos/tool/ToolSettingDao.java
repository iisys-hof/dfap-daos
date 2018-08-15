package daos.tool;


import daos.Dao;
import entities.ToolSetting;

import java.util.List;


public interface ToolSettingDao extends Dao{
    List readToolSettingsForMachineAndTool(String machine, String tool);

    //void writeToolSettings(List<ToolSetting> toolSettings, String machine, String tool);
}
