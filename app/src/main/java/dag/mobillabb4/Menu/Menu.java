package dag.mobillabb4.Menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import dag.mobillabb4.Activities.ChatRoomActivity;
import dag.mobillabb4.Activities.InterestActivity;
import dag.mobillabb4.Activities.LoginActivity;
import dag.mobillabb4.Activities.MainChatActivity;
import dag.mobillabb4.Activities.PreferencesActivity;
import dag.mobillabb4.Activities.ProfileActivity;
import dag.mobillabb4.Firebase.FirebaseMessage;
import dag.mobillabb4.Firebase.Messages;
import dag.mobillabb4.Model.AccountModel;
import dag.mobillabb4.R;
import dag.mobillabb4.Service.MapService;
import dag.mobillabb4.Tasks.RequestTask;

/**
 * Created by Dag on 1/4/2018.
 */

public class Menu implements android.support.v7.widget.Toolbar.OnMenuItemClickListener{
    private Activity activity;
    private Intent intent;
    private RequestTask addTask;
    private RequestTask stopMapTask;
    private RequestTask logoutTask;
    private RequestTask changeNameTask;
    private int PICK_IMAGE_REQUEST = 1;

    public Menu(Activity cont){
        activity = cont;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.newconv:
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Enter e-mail");
                    final EditText input = new EditText(activity);
                    input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addTask = new RequestTask(new RequestTask.OnTaskCompleted() {
                                @Override
                                public void onTaskCompleted(FirebaseMessage result) {
                                    try {
                                        Log.i("hejhejsan",":))");
                                        if (result.getInformation().get("status").toString().equals("success")) {
                                            Log.i("hejhejsan",":))!!!");
                                            AccountModel.setTargetAccount(new AccountModel(Integer.parseInt(
                                                    result.getInformation().get("id").toString())
                                                    ,result.getInformation().get("username").toString()));
                                            intent = new Intent(activity, ChatRoomActivity.class);
                                            activity.startActivity(intent);
                                        } else {
                                            Toast.makeText(activity, "Adding failed", Toast.LENGTH_LONG).show();
                                        }
                                    }catch(JSONException|NullPointerException e){
                                        Toast.makeText(activity, "Adding failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, Messages.addContact(AccountModel.getMyAccount().getId(),input.getText().toString()));
                            addTask.execute();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                    return true;
                case R.id.profile:
                    AccountModel.setTargetAccount(AccountModel.getMyAccount());
                    Intent intent = new Intent(activity, ProfileActivity.class);
                    activity.startActivity(intent);
                    return true;
                case R.id.addInterest:
                    AccountModel.setTargetAccount(AccountModel.getMyAccount());
                    Intent intent55 = new Intent(activity, InterestActivity.class);
                    activity.startActivity(intent55);
                    return true;
                case R.id.changeName:
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);
                    builder2.setTitle("Enter new username");
                    final EditText input2 = new EditText(activity);
                    input2.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    builder2.setView(input2);
                    builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            changeNameTask = new RequestTask(new RequestTask.OnTaskCompleted() {
                                @Override
                                public void onTaskCompleted(FirebaseMessage result) {
                                    try {
                                        if (result.getInformation().get("status").toString().equals("success")) {
                                            Toast.makeText(activity, "Name change successful", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(activity, "Name change failed", Toast.LENGTH_LONG).show();
                                        }
                                    }catch(JSONException|NullPointerException e){
                                        Toast.makeText(activity, "Name change failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, Messages.changeName(AccountModel.getMyAccount().getId(),input2.getText().toString()));
                            changeNameTask.execute();
                        }
                    });
                    builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder2.show();

                    return true;
                case R.id.uploadPicture:
                    Intent intentpicture = new Intent();
                    intentpicture.setType("image/*");
                    intentpicture.setAction(Intent.ACTION_GET_CONTENT);
                    activity.startActivityForResult(Intent.createChooser(intentpicture, "Select Picture"), PICK_IMAGE_REQUEST);
                    return true;
                case R.id.stopMap:
                    MapService.turnUpdateOff();
                    activity.getApplicationContext().stopService(new Intent(activity.getApplicationContext(), MapService.class));
                    Toast.makeText(activity,"Stopped map tracking",Toast.LENGTH_LONG).show();
                    return true;
                case R.id.logout:
                    activity.getApplicationContext().stopService(new Intent(activity.getApplicationContext(), MapService.class));
                    try {
                        Auth.GoogleSignInApi.signOut(LoginActivity.getGoogleApiClient());
                    }catch(IllegalStateException e){

                    }
                    logoutTask = new RequestTask(new RequestTask.OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted(FirebaseMessage result) {

                        }
                    }, Messages.logout(AccountModel.getMyAccount().getId()));
                    logoutTask.execute();
                    AccountModel.logOut();
                    Intent intent2 = new Intent(activity,LoginActivity.class);
                    activity.finish();
                    activity.startActivity(intent2);
                    return true;
                default:
                    return false;
            }
    }


}
