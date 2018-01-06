package dag.mobillabb4.Menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;

import dag.mobillabb4.Activities.LoginActivity;
import dag.mobillabb4.Activities.PreferencesActivity;
import dag.mobillabb4.Activities.ProfileActivity;
import dag.mobillabb4.R;

/**
 * Created by Dag on 1/4/2018.
 */

public class Menu implements android.support.v7.widget.Toolbar.OnMenuItemClickListener{
    private Activity activity;

    public Menu(Activity cont){
        activity = cont;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;
            switch (item.getItemId()) {
                case R.id.newconv:
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Enter username");
                    final EditText input = new EditText(activity);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO: adda konversation
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
                    //TODO: hämta från databasen
                    //ProfileModel.setInstance();
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
