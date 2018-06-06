package daos;

public interface Dao {
    void create(DfapEntity entity);
    void update(DfapEntity entity);
    DfapEntity    read(DfapEntity id);
    void delete(DfapEntity entity);
}
