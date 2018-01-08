package dag.mobillabb4.Tasks;

import android.os.AsyncTask;

import dag.mobillabb4.Firebase.FirebaseMessage;
import dag.mobillabb4.Firebase.Messages;
import dag.mobillabb4.Firebase.MyFirebaseMessaging;

/**
 * Created by Dag on 1/7/2018.
 */

public class LoginTask extends AsyncTask<String,Void,Boolean> {
    private OnTaskCompleted listener;

    public interface OnTaskCompleted{
        void onTaskCompleted(boolean result);
    }

    public LoginTask(OnTaskCompleted listener){
        this.listener=listener;
    }

    protected void onPostExecute(Boolean result){
        listener.onTaskCompleted(result);
    }

    @Override
    protected Boolean doInBackground(String... params) {
         int msgId = Messages.login(params[0],params[1]);
         long now = System.currentTimeMillis();
         FirebaseMessage removeMessage = null;
         boolean keepGoing = true;
         while(System.currentTimeMillis()>now+5000 && keepGoing) {
             for (FirebaseMessage fm : MyFirebaseMessaging.getMessages()) {
                 if (fm.getId() == msgId) {
                     removeMessage = fm;
                     keepGoing=false;
                     break;
                 }
             }
         }
        MyFirebaseMessaging.getMessages().remove(removeMessage);
        if(removeMessage.getInformation().contains("success")){
            return true;
        }else
            return false;
    }
}
