package dag.mobillabb4.Menu;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import dag.mobillabb4.Activities.ChatRoomActivity;
import dag.mobillabb4.Firebase.FirebaseMessage;
import dag.mobillabb4.Firebase.Messages;
import dag.mobillabb4.Model.AccountModel;
import dag.mobillabb4.R;
import dag.mobillabb4.Tasks.RequestTask;

/**
 * Created by Dag on 1/15/2018.
 */

public class ProfileMenu implements android.support.v7.widget.Toolbar.OnMenuItemClickListener{

    private Activity activity;

    public ProfileMenu(Activity cont){
        activity = cont;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item){
        RequestTask addTask;
        switch (item.getItemId()) {
            case R.id.addUser:
            addTask = new RequestTask(new RequestTask.OnTaskCompleted() {
                @Override
                public void onTaskCompleted(FirebaseMessage result) {
                    try {
                        if (result.getInformation().get("status").toString().equals("success")) {
                            Toast.makeText(activity, "Adding successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(activity, ChatRoomActivity.class);
                            activity.startActivity(intent);
                        } else {
                            Toast.makeText(activity, "Adding failed", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException | NullPointerException e) {
                        Toast.makeText(activity, "Adding failed", Toast.LENGTH_LONG).show();
                    }
                }
            }, Messages.addContact(AccountModel.getMyAccount().getId(), AccountModel.getTargetAccount().getEmail()));
            addTask.execute();
            return true;
        }
        return false;
    }
}
