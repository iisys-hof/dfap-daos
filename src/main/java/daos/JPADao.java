package daos;


import utils.XEntityManagerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;


public abstract class JPADao implements Dao {
    protected Class entityClass;
    //  @PersistenceContext(unitName = "dfap")
    @Inject
    @XEntityManagerFactory
    protected EntityManager em;


    public JPADao() {

    }

    @Override
    public void create(DfapEntity entity) {

    }

    @Override
    public void update(DfapEntity entity) {

    }

    @Override
    public DfapEntity read(DfapEntity id) {
        return null;
    }

    @Override
    public void delete(DfapEntity entity) {

    }
}
