package dag.mobillabb4.Firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * Created by Dag on 1/7/2018.
 */

public class Messages {
    private static Random rand = new Random();
    private static FirebaseMessaging fm = FirebaseMessaging.getInstance();

    public static int login(String email, String password){
        int msgId = rand.nextInt();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Long.toString(msgId))
                .addData("type","register")
                .addData("e-mail", email)
                .addData("password",password)
                .setTtl(1200)
                .build());
        return msgId;
    }

    public static void sendMessage(int from, int to, String message){
        int msgId = rand.nextInt();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Long.toString(msgId))
                .addData("type","send")
                .addData("from_user", Integer.toString(from))
                .addData("to_user",Integer.toString(to))
                .addData("message",message)
                .setTtl(1200)
                .build());


    }

    public static void addConversation(int myId,int addedId){
        int msgId = rand.nextInt();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Long.toString(msgId))
                .addData("type","addconv")
                .addData("my_id", Integer.toString(myId))
                .addData("added_id",Integer.toString(addedId))
                .setTtl(1200)
                .build());
    }

    public static void getConversation(int myId,int targetId){
        int msgId = rand.nextInt();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Long.toString(msgId))
                .addData("type","getconv")
                .addData("my_id", Integer.toString(myId))
                .addData("added_id",Integer.toString(targetId))
                .setTtl(1200)
                .build());
    }

}
