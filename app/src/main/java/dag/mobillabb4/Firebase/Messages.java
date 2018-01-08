package dag.mobillabb4.Firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Date;
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
        Log.i("Login",email+" "+password+" "+msgId);
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","login")
                .addData("e-mail", email)
                .addData("password",password)
                .setTtl(1200)
                .build());
        return msgId;
    }

    public static int register(String username,String password, String confirmPassword,String email,Date date){
        int msgId = rand.nextInt();

        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","register")
                .addData("username",username)
                .addData("password",password)
                .addData("confirmPassword",confirmPassword)
                .addData("e-mail", email)
                .addData("birthday", date.toString())
                .build());
        return msgId;
    }

    public static int sendMessage(int from, int to, String message){
        int msgId = rand.nextInt();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","send")
                .addData("from_user", Integer.toString(from))
                .addData("to_user",Integer.toString(to))
                .addData("message",message)
                .setTtl(1200)
                .build());
        return msgId;
    }

    public static int addContact(int from,String toEmail){
        int msgId = rand.nextInt();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","addContact")
                .addData("from_user", Integer.toString(from))
                .addData("to_user_email",toEmail)
                .addData("message","has added you!")
                .setTtl(1200)
                .build());
        return msgId;
    }

    public static int getChatContacts(int myId){
        int msgId = rand.nextInt();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","get_contacts")
                .addData("my_id", Integer.toString(myId))
                .setTtl(1200)
                .build());
        return msgId;
    }


    public static int getMessages(int myId,int targetId){
        int msgId = rand.nextInt();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","get_messages")
                .addData("my_id", Integer.toString(myId))
                .addData("target_id", Integer.toString(targetId))
                .setTtl(1200)
                .build());
        return msgId;
    }

    public static int getProfile(int profileId){
        int msgId = rand.nextInt();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","get_profile")
                .addData("target_id", Integer.toString(profileId))
                .setTtl(1200)
                .build());
        return msgId;
    }

}
