package dag.mobillabb4.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import dag.mobillabb4.Firebase.FirebaseMessage;
import dag.mobillabb4.Firebase.Messages;
import dag.mobillabb4.Model.AccountModel;
import dag.mobillabb4.R;
import dag.mobillabb4.Tasks.RequestTask;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText confirmPassword;
    private EditText email;
    private Spinner day;
    private Spinner month;
    private Spinner year;
    private Button registerButton;
    private TextView statusText;
    private Context context = this;
    private RequestTask registerTask;
    private ProgressBar progress;
    List<String> dayList;
    List<String> monthList;
    List<String> yearList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.editText3);
        password = findViewById(R.id.editText5);
        confirmPassword = findViewById(R.id.editText6);
        email = findViewById(R.id.editText7);
        registerButton = findViewById(R.id.button2);
        registerButton.setOnClickListener(RegisterListener);
        statusText = findViewById(R.id.textView8);
        day = findViewById(R.id.spinner);
        month = findViewById(R.id.spinner2);
        year = findViewById(R.id.spinner3);
        progress = findViewById(R.id.progressBar2);
        initSpinners();
    }
    protected View.OnClickListener RegisterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Date date = new Date(Integer.parseInt(year.getSelectedItem().toString())-1900,
                    Integer.parseInt(month.getSelectedItem().toString())-1,
                            Integer.parseInt(day.getSelectedItem().toString()));

            Log.i("Register","pressed");
            registerTask = new RequestTask(new RequestTask.OnTaskCompleted() {
                @Override
                public void onTaskCompleted(FirebaseMessage result) {
                    Log.i("Register","pressed");
                    try {
                        if (result.getInformation().get("status").toString().equals("success")) {
                            statusText.setTextColor(Color.BLACK);
                            statusText.setText("Successfully created new account");
                        } else {
                            statusText.setTextColor(Color.RED);
                            statusText.setText("Error creating new account");
                        }
                    }catch(NullPointerException|JSONException e){
                        //TODO: skicka delete account för säkerhetsskull, ta bort med email
                        statusText.setTextColor(Color.RED);
                        statusText.setText("Server timeout");
                    }
                    progress.setVisibility(View.INVISIBLE);
                }
            }, Messages.register(username.getText().toString(),
                    password.getText().toString(),
                    confirmPassword.getText().toString(),
                    email.getText().toString(),
                    date));
            registerTask.execute();
            progress.setVisibility(View.VISIBLE);

        }
    };
    private void initSpinners(){
        dayList = new ArrayList<>();
        for(int i=1;i<32;i++){
            dayList.add(""+i);
        }
        monthList = new ArrayList<>();
        for(int i=1;i<13;i++){
            monthList.add(""+i);
        }
        yearList = new ArrayList<>();
        for(int i=1900;i<2019;i++){
            yearList.add(""+i);
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, dayList);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, monthList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        day.setAdapter(dayAdapter);
        month.setAdapter(monthAdapter);
        year.setAdapter(yearAdapter);
    }
}
