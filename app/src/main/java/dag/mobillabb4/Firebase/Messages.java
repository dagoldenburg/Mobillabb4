package dag.mobillabb4.Firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * Created by Dag on 1/7/2018.
 */

public class Messages {

    private static ArrayList<JSONObject> messages;

    public static ArrayList<JSONObject> getMessages() {
        return messages;
    }
    public static void resetMessages(){
        messages = new ArrayList<>();
    }
    private static Random rand = new Random();
    private static FirebaseMessaging fm = FirebaseMessaging.getInstance();

    public static int login(String email, String password){
        int msgId = getFreeMsgId();
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

    public static int register(String username,String password, String email,Date date){
        int msgId = getFreeMsgId();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","register")
                .addData("username",username)
                .addData("password",password)
                .addData("e-mail", email)
                .addData("birthday", date.toString())
                .build());
        return msgId;
    }
    public static int registerGoogle(String username,String password, String email){
        int msgId = getFreeMsgId();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","registerGoogle")
                .addData("username",username)
                .addData("password",password)
                .addData("e-mail", email)
                .addData("birthday", "")
                .build());
        return msgId;
    }

    public static int getIdByEmail(String email){
        int msgId = getFreeMsgId();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","get_id_by_email")
                .addData("e-mail",email)
                .setTtl(1200)
                .build());
        return msgId;
    }

    public static int sendMessage(int from, int to, String message){
        int msgId = getFreeMsgId();
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
    public static  int logout (int id){
        int msgId = getFreeMsgId();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","logout")
                .addData("id",Integer.toString(id))
                .setTtl(1200)
                .build());
        return  msgId;
    }

    public static int addContact(int from,String toEmail){
        int msgId = getFreeMsgId();
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
    public static int deleteContact(int from,int to){
        int msgId = getFreeMsgId();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","deleteContact")
                .addData("from_user", Integer.toString(from))
                .addData("to_user",Integer.toString(to))
                .setTtl(1200)
                .build());
        return msgId;
    }

    public static int getChatContacts(int myId){
        int msgId = getFreeMsgId();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","get_contacts")
                .addData("my_id", Integer.toString(myId))
                .setTtl(1200)
                .build());
        return msgId;
    }

    public static int changeName(int myId,String newUsername){
        int msgId = getFreeMsgId();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","change_name")
                .addData("my_id", Integer.toString(myId))
                .addData("username",newUsername)
                .setTtl(1200)
                .build());
        return msgId;
    }

    public static int getMessages(int myId,int targetId){
        int msgId = getFreeMsgId();
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
        int msgId = getFreeMsgId();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","get_profile")
                .addData("target_id", Integer.toString(profileId))
                .setTtl(1200)
                .build());
        return msgId;
    }

    public static int addUserToMap(int myId,int tableId){
        int msgId = getFreeMsgId();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","add_user_to_map")
                .addData("my_id", Integer.toString(myId))
                .addData("table_id", Integer.toString(tableId))
                .setTtl(1200)
                .build());
        return msgId;
    }

    public static int removeUserFromMap(int myId,int tableId){
        int msgId = getFreeMsgId();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","remove_user_from_map")
                .addData("my_id", Integer.toString(myId))
                .addData("table_id", Integer.toString(tableId))
                .setTtl(1200)
                .build());
        return msgId;
    }

    public static int getUserOnMap(int myId,int tableId){
        int msgId = getFreeMsgId();
        fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId))
                .addData("type","get_users_on_map")
                .addData("my_id", Integer.toString(myId))
                .addData("table_id", Integer.toString(tableId))
                .setTtl(1200)
                .build());
        return msgId;
    }
    
    private static int getFreeMsgId(){
        boolean keepGoing=true;
        int msgId =  rand.nextInt();
        while(keepGoing) {
            msgId = rand.nextInt();
            if(loop(msgId)){
                break;
            }
        }
        return msgId;
    }
    
    private static boolean loop(int msgId){
        for (FirebaseMessage fb : MyFirebaseMessaging.getMessageHeap()) {
            if (fb.getId() == msgId) {
                return false;
            }
        }
        return true;
    }

}
