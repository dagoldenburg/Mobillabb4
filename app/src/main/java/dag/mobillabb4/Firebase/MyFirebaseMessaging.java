package dag.mobillabb4.Firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Map;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    public MyFirebaseMessaging() {
    }

    private static ArrayList<FirebaseMessage> messages = new ArrayList<>();

    public static ArrayList<FirebaseMessage> getMessages() {
        return messages;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("Firebase", "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("Firebase", "Message data payload: " + remoteMessage.getData() + " Message id: "+remoteMessage.getMessageId());
            Map<String,String> map = remoteMessage.getData();
            if(map.containsKey("login")){
                messages.add(new FirebaseMessage(Integer.parseInt(remoteMessage.getMessageId()),remoteMessage.getData().toString(),System.currentTimeMillis()));
            }else if(map.containsKey("message")){
                MyNotificationManager.getInstance(this).displayNotification("Firebase", remoteMessage.getData().toString());
            }
        }

        // Check if message contains a notification payload.
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
