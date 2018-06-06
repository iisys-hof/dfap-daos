package daos.ordering;


import entities.Ordering;

import java.util.List;

public interface OrderingDao {
    List readAllOrders();
    Ordering readOrdering(long orderId);
}
