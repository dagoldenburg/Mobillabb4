package dag.mobillabb4.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dag.mobillabb4.CustomAdapters.MessageViewAdapter;
import dag.mobillabb4.Firebase.FirebaseMessage;
import dag.mobillabb4.Firebase.Messages;
import dag.mobillabb4.Menu.Menu;
import dag.mobillabb4.Model.AccountModel;
import dag.mobillabb4.R;
import dag.mobillabb4.Tasks.RequestTask;

public class ChatRoomActivity extends AppCompatActivity {

    private Context context;
    private ListView listView;
    private EditText message;
    private static long msgId = 0;
    private RequestTask getMessagesTask;
    private RequestTask sendMessageTask;
    private ProgressBar progress;
    private MessageViewAdapter adapter;
    ArrayList<JSONObject> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Toolbar toolbar =  findViewById(R.id.top_toolbar);
        toolbar.inflateMenu(R.menu.options_menu);
        toolbar.setOnMenuItemClickListener(new Menu(this));
        Toolbar toolbarbot = findViewById(R.id.bottom_toolbar);
        toolbarbot.bringToFront();
        ImageButton backBut =findViewById(R.id.imageButton);
        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageButton sendBut =findViewById(R.id.imageButton2);
        message = findViewById(R.id.myEditText);
        sendBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: skicka meddelande till backend
                sendMessageTask = new RequestTask(new RequestTask.OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(FirebaseMessage result) {
                        try {
                            messages.add(new JSONObject("{\"id\":\""+AccountModel.getMyAccount().getId()+"\"," +
                                                            "\"username\": \"me\"," +
                                                            "\"content\":\""+message.getText().toString()+"\"}"));
                        }catch(JSONException e){
                            Log.i("Send",e.getMessage());
                        }
                        updateView();
                    }
                }, Messages.sendMessage(AccountModel.getMyAccount().getId(),AccountModel.getTargetAccount().getId(),message.getText().toString()));
                sendMessageTask.execute();
            }
        });
        sendBut.bringToFront();
        messages = new ArrayList<>();
        context = this;
        listView = findViewById(R.id.messageList);
        progress = findViewById(R.id.progressBar4);
        adapter = new MessageViewAdapter(this,messages);
        listView.setAdapter(adapter);
        getMessagesTask = new RequestTask(new RequestTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(FirebaseMessage result) {
                messages = new ArrayList<>();
                try {
                    JSONArray messagesjson = result.getInformation().getJSONArray("messages");
                    for(int i=0;i<messagesjson.length();i++){
                        messages.add(((JSONObject)messagesjson.get(i)));
                    }
                }catch(JSONException e){
                    Log.i("ChatRoom",e.getMessage());
                }
                progress.setVisibility(View.INVISIBLE);
            }
        }, Messages.getMessages(AccountModel.getMyAccount().getId(),AccountModel.getTargetAccount().getId()));
        getMessagesTask.execute();
        progress.setVisibility(View.VISIBLE);
        progress.bringToFront();
    }

    public void updateView(){
        adapter = new MessageViewAdapter(this,messages);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
