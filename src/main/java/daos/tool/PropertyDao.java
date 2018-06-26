package daos.tool;


import daos.Dao;

import java.util.List;

public interface PropertyDao extends Dao {
    List readAllProperties();
}