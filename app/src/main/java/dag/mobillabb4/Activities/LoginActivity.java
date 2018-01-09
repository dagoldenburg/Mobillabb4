package dag.mobillabb4.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;

import dag.mobillabb4.Firebase.FirebaseMessage;
import dag.mobillabb4.Firebase.Messages;
import dag.mobillabb4.Firebase.MyFirebaseInstance;
import dag.mobillabb4.Model.AccountModel;
import dag.mobillabb4.R;
import dag.mobillabb4.Tasks.RequestTask;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Button loginButton;
    private Button registerButton;
    private EditText emailInput;
    private EditText passwordInput;
    private SignInButton signInButton;
    private ProgressBar progress;
    private Context context = this;
    private TextView errorText;
    private GoogleApiClient googleApiClient;
    private RequestTask loginTask;
    private RequestTask registerGmailTask;
    private RequestTask getIdByEmail;
    private RequestTask getIdByEmail2;
    private  GoogleSignInAccount acc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this,MyFirebaseInstance.class));
        Log.i("asd", FirebaseInstanceId.getInstance().getToken());
        if(AccountModel.getMyAccount()!=null){
            Intent intent = new Intent(context, MainChatActivity.class);
            startActivity(intent);
        }
        loginButton = findViewById(R.id.button);
        loginButton.setOnClickListener(LoginListener);
        registerButton = findViewById(R.id.button3);
        registerButton.setOnClickListener(RegisterListener);
        emailInput = findViewById(R.id.editText);
        passwordInput = findViewById(R.id.editText2);
        errorText = findViewById(R.id.ErrorText);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(GoogleSignInListener);
        progress = findViewById(R.id.progressBar);
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
                Auth.GoogleSignInApi.signOut(googleApiClient);
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
                    acc = result.getSignInAccount();
                    Log.i("GoogleLogIn",acc.getEmail());
                    getIdByEmail = new RequestTask(new RequestTask.OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted(FirebaseMessage result) {
                            try {
                                if (result.getInformation().get("status").equals("success")) {
                                    AccountModel.initMyAcc(Integer.parseInt(result.getInformation().get("id").toString()));
                                    Intent intent = new Intent(context, MainChatActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    googleRegister();
                                }
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }, Messages.getIdByEmail(acc.getEmail()));


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

        private void googleLogin2(){
            getIdByEmail2 = new RequestTask(new RequestTask.OnTaskCompleted() {
                @Override
                public void onTaskCompleted(FirebaseMessage result) {
                    try {
                        if (result.getInformation().get("status").equals("success")) {
                            AccountModel.initMyAcc(Integer.parseInt(result.getInformation().get("id").toString()));
                            Intent intent = new Intent(context, MainChatActivity.class);
                            startActivity(intent);
                        }
                        else{
                            googleRegister();
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, Messages.getIdByEmail(acc.getEmail()));
        }

        private void googleRegister(){
            registerGmailTask = new RequestTask(new RequestTask.OnTaskCompleted() {
                @Override
                public void onTaskCompleted(FirebaseMessage result) {
                    try {
                        if (result.getInformation().get("status").equals("success")) {
                            googleLogin2();
                            Intent intent = new Intent(context, MainChatActivity.class);
                            startActivity(intent);
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, Messages.registerGoogle(acc.getDisplayName(), "", acc.getEmail()));
        }

    protected View.OnClickListener LoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO: kolla mot backend om det är korrekt login info annars får man felmeddelande
            loginTask = new RequestTask(new RequestTask.OnTaskCompleted() {
                @Override
                public void onTaskCompleted(FirebaseMessage result) {
                    try {
                        if (Integer.parseInt(result.getInformation().get("id").toString())!=-1) {
                            AccountModel.initMyAcc(Integer.parseInt(result.getInformation().get("id").toString()));
                            Intent intent = new Intent(context, MainChatActivity.class);
                            startActivity(intent);
                        } else
                            errorText.setText("Invalid login information");
                    }catch(NullPointerException|JSONException e){
                        errorText.setText("Invalid login information");
                    }
                    progress.setVisibility(View.INVISIBLE);
                }
            }, Messages.login(emailInput.getText().toString(),passwordInput.getText().toString()));
            loginTask.execute();
            progress.setVisibility(View.VISIBLE);
        }
    };


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        errorText.setText("Connection problems, can't sign in.");
    }
}
