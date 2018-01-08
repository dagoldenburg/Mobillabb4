package dag.mobillabb4.Firebase;

/**
 * Created by Dag on 1/7/2018.
 */

public class FirebaseMessage {
    int id;
    String information;
    long time;

    public FirebaseMessage(int id, String information, long time) {
        this.id = id;
        this.information = information;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getInformation() {
        return information;
    }

    public long getTime() {
        return time;
    }

}
