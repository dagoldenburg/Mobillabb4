package dag.mobillabb4.Firebase;

import org.json.JSONObject;

/**
 * Created by Dag on 1/7/2018.
 */

public class FirebaseMessage {
    int id;
    JSONObject json;
    long time;

    public FirebaseMessage(int id, JSONObject json, long time) {
        this.id = id;
        this.json= json;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public JSONObject getInformation() {
        return json;
    }

    public long getTime() {
        return time;
    }

}
