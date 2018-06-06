package daos.feedback;


import java.util.List;

public interface FeedbackEntryDao  {

    List readAllFeedbacks();
    List getFeedbackForOrder(long orderId);

}
