package dag.mobillabb4.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import dag.mobillabb4.CustomAdapters.InterestViewAdapter;
import dag.mobillabb4.CustomAdapters.ListViewAdapter;
import dag.mobillabb4.CustomAdapters.MessageViewAdapter;
import dag.mobillabb4.Firebase.FirebaseMessage;
import dag.mobillabb4.Firebase.Messages;
import dag.mobillabb4.Model.AccountModel;
import dag.mobillabb4.Model.ConversationModel;
import dag.mobillabb4.Model.InterestModel;
import dag.mobillabb4.R;
import dag.mobillabb4.Tasks.RequestTask;

public class InterestActivity extends AppCompatActivity {

    private RequestTask getInterestsTask;

    private RequestTask getUserInterestsTask;
    private ProgressBar progressbar;
    private ListView listView;
    private InterestViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        progressbar = findViewById(R.id.progressBar6);
        ImageButton backBut =findViewById(R.id.imageButton3);
        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = findViewById(R.id.interests);
        getInterestsTask = new RequestTask(new RequestTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(FirebaseMessage result) {
                try{
                    if(result.getInformation().get("status").equals("success")){
                        Log.i("haha","lolo");
                        InterestModel.initInterests(result.getInformation().getJSONArray("Interests"));
                        getUserInterestsTask = new RequestTask(new RequestTask.OnTaskCompleted(){
                            @Override
                            public void onTaskCompleted(FirebaseMessage result) {
                                try {
                                    if(result.getInformation().get("status").equals("success")) {
                                            InterestModel.initUsersInterests(result.getInformation().getJSONArray("userInterests"));
                                            adapter = new InterestViewAdapter(getApplicationContext(),
                                                    InterestModel.getInterests(),
                                                    InterestModel.getUserInterests());
                                            listView.setAdapter(adapter);

                                    }
                                } catch (NullPointerException|JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },Messages.getAllUsersInterests(AccountModel.getMyAccount().getId()));
                        getUserInterestsTask.execute();
                    }else{
                        Toast.makeText(getApplicationContext(),"Couldn't get interests",Toast.LENGTH_LONG);
                    }
                }catch(NullPointerException|JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Couldn't get interests",Toast.LENGTH_LONG);
                }
                progressbar.setVisibility(View.INVISIBLE);
            }
        }, Messages.getAllInterests());
        getInterestsTask.execute();
        progressbar.setVisibility(View.VISIBLE);
        progressbar.bringToFront();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,final View v, final int position, long id){

                RequestTask updateInterestTask = new RequestTask(new RequestTask.OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(FirebaseMessage result) {
                        try {
                            if(result.getInformation().get("status").equals("remove")) {
                                    ImageView img4 = (ImageView) ((RelativeLayout) v).getChildAt(0);
                                    //img4 = findViewById(R.id.interestIcon);
                                    img4.setImageResource(android.R.drawable.checkbox_off_background);
                                } else {
                                    ImageView img4 = (ImageView) ((RelativeLayout) v).getChildAt(0);
                                    //img4 = findViewById(R.id.interestIcon);
                                    img4.setImageResource(android.R.drawable.checkbox_on_background);
                            }
                        } catch (NullPointerException|JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Couldn't update interest",Toast.LENGTH_LONG);
                        }
                    }
                },Messages.updateUserInterest(AccountModel.getMyAccount().getId(),
                        InterestModel.getInterests().get(position).getId()));
                updateInterestTask.execute();
            }
        });
    }



}
