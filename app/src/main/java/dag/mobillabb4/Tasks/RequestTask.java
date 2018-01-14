package dag.mobillabb4.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import dag.mobillabb4.Firebase.FirebaseMessage;
import dag.mobillabb4.Firebase.MyFirebaseMessaging;

/**
 * Created by Dag on 1/8/2018.
 */

public class RequestTask extends AsyncTask<String,Void,FirebaseMessage> {
    private OnTaskCompleted listener;
    private int msgId;

    public interface OnTaskCompleted{
        void onTaskCompleted(FirebaseMessage result);
    }

    public RequestTask(OnTaskCompleted listener,int msgId){
        this.listener=listener;
        this.msgId = msgId;
    }

    protected void onPostExecute(FirebaseMessage result){
        listener.onTaskCompleted(result);
    }

    @Override
    protected FirebaseMessage doInBackground(String... params) {
        Log.i("RequestTask","Running");
        long now = System.currentTimeMillis();
        FirebaseMessage removeMessage = null;
        boolean keepGoing = true;
        ArrayList<FirebaseMessage> local = MyFirebaseMessaging.getMessageHeap();
        while(now+10000>System.currentTimeMillis() && keepGoing) {
            for (FirebaseMessage fm : local) {
                if (fm.getId() == msgId) {
                    removeMessage = fm;
                    keepGoing=false;
                    break;
                }
            }
        }
        Log.i("Firebase","size: "+MyFirebaseMessaging.getMessageHeap().size());
        MyFirebaseMessaging.getMessageHeap().remove(removeMessage);
        Log.i("Firebase","size2: "+MyFirebaseMessaging.getMessageHeap().size());
        return removeMessage;
    }
}