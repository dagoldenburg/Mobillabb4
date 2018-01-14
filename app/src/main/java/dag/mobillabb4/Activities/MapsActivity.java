package dag.mobillabb4.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import dag.mobillabb4.CustomAdapters.MessageViewAdapter;
import dag.mobillabb4.Firebase.FirebaseMessage;
import dag.mobillabb4.Firebase.Messages;
import dag.mobillabb4.Model.AccountModel;
import dag.mobillabb4.Model.MapUserModel;
import dag.mobillabb4.R;
import dag.mobillabb4.Service.MapService;
import dag.mobillabb4.Tasks.RequestTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private Intent serviceIntent;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("Supertest","reqcode "+requestCode);
        for(String s : permissions){
            Log.i("Supertest","permissions" + s);
        }
        for(int s : grantResults){
            Log.i("Supertest","grantResults " + s);
        }

        chooseTable();

    }

    private void chooseTable(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final Activity act = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose activity");
        builder.setItems(new CharSequence[]
                        {"Social", "Sports", "Music", "Other","Cancel"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==4)
                            finish();

                        startService(which+1);
                    }
                });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    1001);
            return;
        }else{
            chooseTable();
        }
        MapService.setLm(lm);
    }

    private void startService(int choice){
        Log.i("choice",choice+"");
        stopService(new Intent(this, MapService.class));
        MapService.setMapChoice(choice);
        serviceIntent = new Intent(getApplicationContext(), MapService.class);
        startService(serviceIntent);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    LinkedList<Marker> markers = new LinkedList<>();
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final Handler handler = new Handler();

        handler.postDelayed( new Runnable() {

            @Override
            public void run() {
                if(MapService.isChange()){
                    //TODO:skriv över i den listan som visas
                    Log.i("Change","change done");
                    int i = 0;
                    for(MapUserModel mum : MapUserModel.getMapUsers()){
                        Log.i("Change","name "+mum.getOwner().getUsername()
                                +" lat " +mum.getMarker().getPosition().latitude
                                +" long "+mum.getMarker().getPosition().longitude);
                        try{
                            Marker marker = mMap.addMarker(mum.getMarker());
                            marker.setTag(mum.getOwner().getId());
                            markers.set(i++,marker);
                        }catch(Exception e){
                            Marker marker = mMap.addMarker(mum.getMarker());
                            marker.setTag(mum.getOwner().getId());
                            markers.add(marker);

                        }
                    }
                    for(;i<MapUserModel.getMapUsers().size();i++) {
                        try {
                            markers.get(i).remove();
                        }catch(IndexOutOfBoundsException e){

                        }
                    }
                }
                handler.postDelayed( this,  5000 );
            }
        },  1000 );
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.i("haha",((Integer) marker.getTag())+" "+marker.getTitle());
        AccountModel.setTargetAccount(new AccountModel((Integer) marker.getTag(),marker.getTitle()));
        Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        markers.clear();
        MapUserModel.setMapUsers(new LinkedList<MapUserModel>());
    }
}
