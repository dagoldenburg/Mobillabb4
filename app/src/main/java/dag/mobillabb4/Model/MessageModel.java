package dag.mobillabb4.Model;

import java.util.Date;

/**
 * Created by Dag on 1/3/2018.
 */

public class MessageModel {
    private Date recieved;
    private String message;

    public MessageModel(Date recieved, String message) {
        this.recieved = recieved;
        this.message = message;
    }

    public Date getRecieved() {
        return recieved;
    }

    public void setRecieved(Date recieved) {
        this.recieved = recieved;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
