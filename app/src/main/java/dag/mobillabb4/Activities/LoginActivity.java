package dag.mobillabb4.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import dag.mobillabb4.Model.AccountModel;
import dag.mobillabb4.R;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;
    private EditText usernameInput;
    private EditText passwordInput;
    private Context context = this;
    private TextView errorText;
    private static AccountModel accountData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.button);
        loginButton.setOnClickListener(LoginListener);
        registerButton = findViewById(R.id.button3);
        registerButton.setOnClickListener(RegisterListener);
        usernameInput = findViewById(R.id.editText);
        passwordInput = findViewById(R.id.editText2);
        errorText = findViewById(R.id.ErrorText);

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
            if(AccountModel.login(usernameInput.getText().toString(),passwordInput.getText().toString())) {
                Intent intent = new Intent(context, MainChatActivity.class);
                startActivity(intent);
            }else{
                errorText.setText("Invalid login information");
            }
        }
    };


}
