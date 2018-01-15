package dag.mobillabb4.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedList;

import dag.mobillabb4.Firebase.FirebaseMessage;
import dag.mobillabb4.Firebase.MyFirebaseMessaging;

/**
 * Created by Dag on 1/14/2018.
 */

public class BigRequestTask extends AsyncTask<String,Void,String> {
    private OnTaskCompleted listener;
    private int msgId;

    public interface OnTaskCompleted{
        void onTaskCompleted(String result);
    }

    public BigRequestTask(OnTaskCompleted listener,int msgId){
        this.listener=listener;
        this.msgId = msgId;
    }

    protected void onPostExecute(String result){
        listener.onTaskCompleted(result);
    }

    @Override
    protected String doInBackground(String... params) {
        Log.i("RequestTask","Running");
        long now = System.currentTimeMillis();
        FirebaseMessage removeMessage = null;
        LinkedList<FirebaseMessage> removeList = new LinkedList<>();
        boolean keepGoing = true;
        String result = new String();
        String result2 = new String();
        int msgIdCounter = 0;
        while(now+10000>System.currentTimeMillis() && keepGoing) {
            ArrayList<FirebaseMessage> local = MyFirebaseMessaging.getMessageHeap();
            try {
                for (FirebaseMessage fm : local) {
                    if (fm.getId() == msgId) {
                        result = fm.getInformation().get("part1").toString();
                        result = result.substring(1);
                        result.substring(0,result.length()-1);
                        removeList.add(fm);
                        msgIdCounter++;
                        break;
                    }else if(fm.getId()==msgId+1){
                        result2 = fm.getInformation().get("part2").toString();
                        result2.substring(1);
                        result2.substring(0,result.length()-1);
                    }
                    if(msgIdCounter==2){
                        result = result + result2;
                        Log.i("String",result);
                        keepGoing=false;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i("Firebase","size: "+MyFirebaseMessaging.getMessageHeap().size());
        MyFirebaseMessaging.getMessageHeap().remove(removeMessage);
        Log.i("Firebase","size2: "+MyFirebaseMessaging.getMessageHeap().size());
        for(FirebaseMessage fm :removeList) {
            MyFirebaseMessaging.getMessageHeap().remove(fm);
        }
        return result;
    }
}