package dag.mobillabb4.Menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import dag.mobillabb4.Activities.ChatRoomActivity;
import dag.mobillabb4.Activities.LoginActivity;
import dag.mobillabb4.Activities.MainChatActivity;
import dag.mobillabb4.Activities.PreferencesActivity;
import dag.mobillabb4.Activities.ProfileActivity;
import dag.mobillabb4.Firebase.FirebaseMessage;
import dag.mobillabb4.Firebase.Messages;
import dag.mobillabb4.Model.AccountModel;
import dag.mobillabb4.R;
import dag.mobillabb4.Tasks.RequestTask;

/**
 * Created by Dag on 1/4/2018.
 */

public class Menu implements android.support.v7.widget.Toolbar.OnMenuItemClickListener{
    private Activity activity;
    private Intent intent;
    private RequestTask addTask;

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
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addTask = new RequestTask(new RequestTask.OnTaskCompleted() {
                                @Override
                                public void onTaskCompleted(FirebaseMessage result) {
                                    try {
                                        if (result.getInformation().get("status").toString().equals("success")) {
                                            //TODO: result get id & get name, set target och öppna
                                            AccountModel.setTargetAccount(new AccountModel(Integer.parseInt(
                                                    result.getInformation().get("id").toString())
                                                    ,result.getInformation().get("username").toString()));
                                            intent = new Intent(activity, ChatRoomActivity.class);
                                            activity.startActivity(intent);
                                        } else {
                                            Toast.makeText(activity, "Adding failed", Toast.LENGTH_LONG).show();
                                        }
                                    }catch(JSONException e){
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
                    intent = new Intent(activity, ProfileActivity.class);
                    activity.startActivity(intent);
                    return true;
                case R.id.preferences:
                    intent = new Intent(activity, PreferencesActivity.class);
                    activity.startActivity(intent);
                    return true;
                case R.id.logout:
                    activity.finish();
                    return true;
                default:
                    return false;
            }
    }

}
