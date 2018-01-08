package dag.mobillabb4.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import dag.mobillabb4.Firebase.FirebaseMessage;
import dag.mobillabb4.Firebase.Messages;
import dag.mobillabb4.Model.AccountModel;
import dag.mobillabb4.R;
import dag.mobillabb4.Tasks.RequestTask;

public class ProfileActivity extends AppCompatActivity {

    private RequestTask profileTask;
    private TextView name;
    private ImageView profilePic;
    private TextView birthday;
    private ProgressBar progress;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = findViewById(R.id.name);
        birthday = findViewById(R.id.birthdayText);
        progress = findViewById(R.id.progressBar5);
        profileTask = new RequestTask(new RequestTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(FirebaseMessage result) {
                try {
                    if (Integer.parseInt(result.getInformation().get("id").toString())!=-1) {
                        AccountModel.initMyAcc(Integer.parseInt(result.getInformation().get("id").toString()));
                        birthday.setText(result.getInformation().get("birthday").toString());
                        name.setText(result.getInformation().get("name").toString());
                        birthday.setVisibility(View.VISIBLE);
                        name.setVisibility(View.VISIBLE);
                    } else
                        Toast.makeText(getApplicationContext(), "Couldn't load profile", Toast.LENGTH_LONG).show();
                }catch(NullPointerException|JSONException e){
                    Toast.makeText(getApplicationContext(), "Couldn't load profile", Toast.LENGTH_LONG).show();
                }
                progress.setVisibility(View.INVISIBLE);
            }
        }, Messages.getProfile(AccountModel.getTargetAccount().getId()));
        profileTask.execute();
        progress.setVisibility(View.VISIBLE);
    }
}
