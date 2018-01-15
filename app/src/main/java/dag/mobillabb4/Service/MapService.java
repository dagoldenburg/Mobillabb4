package dag.mobillabb4.Service;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import dag.mobillabb4.Activities.MainChatActivity;
import dag.mobillabb4.Firebase.FirebaseMessage;
import dag.mobillabb4.Firebase.Messages;
import dag.mobillabb4.Model.AccountModel;
import dag.mobillabb4.Model.MapUserModel;
import dag.mobillabb4.Tasks.RequestTask;

/**
 * Created by Dag on 1/12/2018.
 */

public class MapService extends Service implements LocationListener {


    private static int mapChoice;

    private static LinkedList<AccountModel> usersOnMap;

    public static LinkedList<AccountModel> getUsersOnMap() {
        return usersOnMap;
    }

    public static void setUsersOnMap(LinkedList<AccountModel> usersOnMap) {
        MapService.usersOnMap = usersOnMap;
    }

    public static int getMapChoice() {
        return mapChoice;
    }

    public static void setMapChoice(int mapChoice) {
        MapService.mapChoice = mapChoice;
    }

    public static void setLm(LocationManager lm) {
        MapService.lm = lm;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("dead","dead");
        t.interrupt();
        run=false;
    }

    Thread t;
    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {
        update = true;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("otur","otur");
            return Service.START_NOT_STICKY;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 5, this);
        t = new Thread("MyService(" + startId + ")") {
            @Override
            public void run() {
                _onStart(intent, startId);
                stopSelf();
            }
        };
        t.start();
        return Service.START_NOT_STICKY;
    }
    boolean run = true;
    private void _onStart(final Intent intent, final int startId) {
        run = true;
        long now = System.currentTimeMillis();
        updateUsersTaskImplementation(mapChoice);
        while (run) {
            if (System.currentTimeMillis() > now + 10000) {
                now = System.currentTimeMillis();
                updateUsersTaskImplementation(mapChoice);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private static boolean change = false;
    private static LocationManager lm;
    private static RequestTask addUserTask;
   // Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    double lng = 0;
    double lat = 0;


    public void addUserTaskImplementation(int choice) {

        addUserTask = new RequestTask(new RequestTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(FirebaseMessage result) {
                Log.i("klar","klar "+lat+" "+lng);
            }
        }, Messages.addUserToMap(AccountModel.getMyAccount().getId(), choice,lat,lng));
        addUserTask.execute();
    }

    private static RequestTask updateUsersTask;

    public void updateUsersTaskImplementation(int choice) {

        updateUsersTask = new RequestTask(new RequestTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(FirebaseMessage result) {
                try {
                    if (result.getInformation().get("status").equals("success")) {
                        JSONArray arr = result.getInformation().getJSONArray("user_on_map");
                        usersOnMap = new LinkedList<>();
                        for(int i = 0;i<arr.length();i++){
                            JSONObject json = arr.getJSONObject(i);

                            AccountModel temp = new AccountModel(Integer.parseInt(json.get("userId").toString()),
                                    json.get("username").toString());
                            int interest = Integer.parseInt(json.get("interest").toString());
                            BitmapDescriptor color;
                            if(interest>=0 && interest < 3){
                                color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                            }else if(interest>=3 && interest <6){
                                color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                            }else if(interest>=6 && interest <9){
                                color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                            }else{
                                 color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                            }
                            MapUserModel.getMapUsers().add(new MapUserModel(new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(json.get("lat").toString()),
                                                    Double.parseDouble(json.get("lng").toString())))
                                            .title(temp.getUsername())
                                            .snippet("You have "+interest+" interests in common")
                                            .icon(color),temp));
                        }
                        change = true;
                    }
                }catch(JSONException|NullPointerException e){
                    e.printStackTrace();
                }
            }
        }, Messages.getUserOnMap(AccountModel.getMyAccount().getId(),choice));
        updateUsersTask.execute();
    }

    public static boolean isChange() {
        return change;
    }

    public static void setChange(boolean change) {
        MapService.change = change;
    }

    static boolean update = true;
    public static void turnUpdateOff(){
        update = false;
    }
    @Override
    public void onLocationChanged(Location location) {
        if(update) {
            MapUserModel.setMapUsers(new LinkedList<MapUserModel>());
            lng = location.getLongitude();
            lat = location.getLatitude();
            Log.i("longlat", lng + " " + lat);
            addUserTaskImplementation(mapChoice);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
