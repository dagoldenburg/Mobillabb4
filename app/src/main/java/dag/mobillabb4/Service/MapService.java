package dag.mobillabb4.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;

import java.util.LinkedList;

import dag.mobillabb4.Activities.MainChatActivity;
import dag.mobillabb4.Firebase.FirebaseMessage;
import dag.mobillabb4.Firebase.Messages;
import dag.mobillabb4.Model.AccountModel;
import dag.mobillabb4.Tasks.RequestTask;

/**
 * Created by Dag on 1/12/2018.
 */

public class MapService extends Service {

    private RequestTask addUser;
    private RequestTask updateUsers;

    private static LinkedList<AccountModel> usersOnMap;

    public static LinkedList<AccountModel> getUsersOnMap() {
        return usersOnMap;
    }

    public static void setUsersOnMap(LinkedList<AccountModel> usersOnMap) {
        MapService.usersOnMap = usersOnMap;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        addUser = new RequestTask(new RequestTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(FirebaseMessage result) {

            }
        }, 1);
        addUser.execute();
        long now = System.currentTimeMillis();
        while(true){
            if(System.currentTimeMillis() > now+30000){
                now = System.currentTimeMillis();
                updateUsers = new RequestTask(new RequestTask.OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(FirebaseMessage result) {
                        //Todo: updatera usersOnMap med result arrayen
                    }
                }, 1);
                updateUsers.execute();
            }
        }

    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
