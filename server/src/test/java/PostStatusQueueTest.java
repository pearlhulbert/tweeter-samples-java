import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.lambda.PostUpdateFeeds;
import software.amazon.awssdk.regions.Region;

public class PostStatusQueueTest {

        @Test
        public void testHandleRequest() {
            SQSEvent sqsEvent = new SQSEvent();
            SQSEvent.SQSMessage message = new SQSEvent.SQSMessage();

            String prophetAlias = "@p";
            User theProphet = new User("Pearl", "Hulbert", prophetAlias,  "https://tweeterbackendbucket.s3/." + Region.US_WEST_2 + ".amazonaws.com/" + prophetAlias.substring(1));
            Status status = new Status("Choose the right!", theProphet, "1000000000L", new ArrayList<>(), new ArrayList<>());
            message.setBody(new Gson().toJson(status));
            List<SQSEvent.SQSMessage> records = new ArrayList<>();
            records.add(message);
            sqsEvent.setRecords(records);

            PostUpdateFeeds handler = new PostUpdateFeeds();
            handler.handleRequest(sqsEvent, null);


        }
}
