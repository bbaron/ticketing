package ticketing.messaging.test;

import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;

public class MessageIO {
    public final InputDestination input;
    public final OutputDestination output;

    public MessageIO(InputDestination input, OutputDestination output) {
        this.input = input;
        this.output = output;
    }

    public boolean neverReceived(long timeout, String bindingName) {
        try {
            var msg = output.receive(timeout, bindingName);
            return msg == null;
        } catch (Exception e) {
            return true;
        }
    }
}
