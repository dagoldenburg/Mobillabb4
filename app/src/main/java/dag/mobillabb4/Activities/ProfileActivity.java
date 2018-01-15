package dag.mobillabb4.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import dag.mobillabb4.Firebase.FirebaseMessage;
import dag.mobillabb4.Firebase.Messages;
import dag.mobillabb4.Menu.Menu;
import dag.mobillabb4.Menu.ProfileMenu;
import dag.mobillabb4.Model.AccountModel;
import dag.mobillabb4.R;
import dag.mobillabb4.Tasks.BigRequestTask;
import dag.mobillabb4.Tasks.RequestTask;

public class ProfileActivity extends AppCompatActivity {

    private BigRequestTask pictureTask;
    private RequestTask profileTask;
    private TextView name;
    private ImageView profilePic;
    private TextView birthday;
    private ProgressBar progress;
    private String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = findViewById(R.id.name);
        ImageButton backBut =findViewById(R.id.imageButton3);
        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Toolbar toolbar =  findViewById(R.id.top_toolbar);
        toolbar.inflateMenu(R.menu.profile_menu);
        toolbar.setOnMenuItemClickListener(new ProfileMenu(this));
        profilePic = findViewById(R.id.imageView2);
        birthday = findViewById(R.id.birthdayText);
        progress = findViewById(R.id.progressBar5);
        profileTask = new RequestTask(new RequestTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(FirebaseMessage result) {
                try {
                    birthday.setText(result.getInformation().get("birthday").toString());
                    name.setText(result.getInformation().get("name").toString());
                    AccountModel.getTargetAccount().setEmail(result.getInformation().get("e-mail").toString());
                    birthday.setVisibility(View.VISIBLE);
                    name.setVisibility(View.VISIBLE);
                }catch(NullPointerException|JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Couldn't load profile", Toast.LENGTH_LONG).show();
                }
                progress.setVisibility(View.INVISIBLE);
            }
        }, Messages.getProfile(AccountModel.getTargetAccount().getId()));
        profileTask.execute();
        progress.setVisibility(View.VISIBLE);
        doRequest(AccountModel.getTargetAccount().getId());
    }

    public void doRequest(int id){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://20180115t013035-dot-mapchat-191515.appspot.com/Image/get/"+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("ProfilePic",response);
                        byte[] byteArray = Base64.decode(response,Base64.DEFAULT);

                        InputStream in = new ByteArrayInputStream(byteArray);
                        Bitmap bmp = BitmapFactory.decodeStream(in);
                        profilePic.setImageBitmap(bmp);
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Couldn't load profile picture", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }
}



