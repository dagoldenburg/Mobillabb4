package dag.mobillabb4.Firebase;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import dag.mobillabb4.Activities.ChatRoomActivity;
import dag.mobillabb4.Model.AccountModel;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    public MyFirebaseMessaging() {
    }

    private static ArrayList<FirebaseMessage> messageHeap = new ArrayList<>();

    public synchronized static ArrayList<FirebaseMessage> getMessageHeap() {
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
    private static boolean msgReceived;

    public static boolean isMsgReceived() {
        return msgReceived;
    }

    public static void setMsgReceived(boolean msgReceived) {
        MyFirebaseMessaging.msgReceived = msgReceived;
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
            Log.i("hejhej",hej);
            String id=null;
            try {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(hej);
                    Log.i("Firebase",hej);
                    id = obj.get("messageId").toString();
                    Log.i("Firebase", obj.get("messageId").toString());
                    Log.i("Firebase", map.toString() + "   " + hej);
                    if(obj.get("type").toString().equals("msg")) {
                        MyNotificationManager.getInstance(this).displayNotification("MapChat", remoteMessage.getData().get("messageBody"));
                        if (Integer.parseInt(obj.get("userId").toString()) == AccountModel.getTargetAccount().getId()) {
                            Log.i("gotmsg","gotmsg");
                            String msg = "{\"userid\":\"" + Integer.parseInt(obj.get("userId").toString()) + "\",\"username\": \"" +
                                    AccountModel.getTargetAccount().getUsername() + "\",\"messageBody\":\"" +
                                    obj.get("messageBody").toString() + "\",\"timeStamp\":\""+obj.get("timeStamp").toString()+"\"}";
                            Messages.getMessages().add(new JSONObject(msg));
                            msgReceived = true;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(obj!=null)
                    messageHeap.add(new FirebaseMessage(Integer.parseInt(id), obj, System.currentTimeMillis()));
            }catch(NullPointerException e){
                e.printStackTrace();
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.d("Firebase", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

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
