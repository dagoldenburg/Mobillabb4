package dag.mobillabb4.Firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    public MyFirebaseMessaging() {
    }

    private static ArrayList<FirebaseMessage> messageHeap = new ArrayList<>();

    public static ArrayList<FirebaseMessage> getMessageHeap() {
        return messageHeap;
    }

    public static void cleanUpMessageHeap(){
        ArrayList<FirebaseMessage> cleanUpList = new ArrayList<>();
        Log.i("Firebase","heap cleanup");
        for(FirebaseMessage fm : messageHeap){
            if(fm.getTime()+60000>System.currentTimeMillis()){
                cleanUpList.add(fm);
            }
        }
        for(FirebaseMessage fm : cleanUpList){
            messageHeap.remove(fm);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messageHeap here.
        // Not getting messageHeap here? See why this may be: https://goo.gl/39bRNJ
        Log.d("Firebase", "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("Firebase", "Message data payload: " + remoteMessage.getData() + " Message id: "+remoteMessage.getMessageId());
            Map<String,String> map = remoteMessage.getData();
            String hej = map.get("message");
            try {
                JSONObject obj = new JSONObject(hej);
                Log.i("Firebase",obj.get("messageId").toString());
                Log.i("Firebase",map.toString() + "   "+hej);
                messageHeap.add(new FirebaseMessage(Integer.parseInt(obj.get("messageId").toString()),obj,System.currentTimeMillis()));
                //TODO: notification när man receive message
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.d("Firebase", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            MyNotificationManager.getInstance(this).displayNotification("Firebase", remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
        Log.i("FirebaseError",e.getMessage());
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
