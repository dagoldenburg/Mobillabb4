package dag.mobillabb4.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import dag.mobillabb4.Firebase.Constants;
import dag.mobillabb4.Firebase.Messages;
import dag.mobillabb4.Firebase.MyNotificationManager;
import dag.mobillabb4.Model.AccountModel;
import dag.mobillabb4.R;
import dag.mobillabb4.Tasks.LoginTask;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Button loginButton;
    private Button registerButton;
    private EditText emailInput;
    private EditText passwordInput;
    private SignInButton signInButton;
    private Context context = this;
    private TextView errorText;
    private static AccountModel accountData;
    private GoogleApiClient googleApiClient;
    private LoginTask loginTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.button);
        loginButton.setOnClickListener(LoginListener);
        registerButton = findViewById(R.id.button3);
        registerButton.setOnClickListener(RegisterListener);
        emailInput = findViewById(R.id.editText);
        passwordInput = findViewById(R.id.editText2);
        errorText = findViewById(R.id.ErrorText);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(GoogleSignInListener);

        GoogleSignInOptions gsio = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gsio)
                .build();
    }

    protected View.OnClickListener GoogleSignInListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent,9001);
        }
    };
    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 9001){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount acc = result.getSignInAccount();
                acc.getEmail();
                Intent intent = new Intent(context, MainChatActivity.class);
                startActivity(intent);
            }else
                errorText.setText("Invalid login information");
        }
    }
    protected View.OnClickListener RegisterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,RegisterActivity.class);
            startActivity(intent);
        }
    };

    protected View.OnClickListener LoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO: kolla mot backend om det är korrekt login info annars får man felmeddelande
            loginTask = new LoginTask(new LoginTask.OnTaskCompleted() {
                @Override
                public void onTaskCompleted(boolean result) {
                    if(result){
                        Intent intent = new Intent(context, MainChatActivity.class);
                        startActivity(intent);
                    }else{
                        errorText.setText("Invalid login information");
                    }
                }
            });
        }
    };






    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        errorText.setText("Connection problems, can't sign in.");
    }
}
