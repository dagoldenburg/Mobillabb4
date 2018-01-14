package dag.mobillabb4.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dag.mobillabb4.CustomAdapters.MessageViewAdapter;
import dag.mobillabb4.Firebase.FirebaseMessage;
import dag.mobillabb4.Firebase.Messages;
import dag.mobillabb4.Firebase.MyFirebaseMessaging;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Toolbar toolbar =  findViewById(R.id.top_toolbar);
        toolbar.inflateMenu(R.menu.options_menu);
        toolbar.setOnMenuItemClickListener(new Menu(this));
        Toolbar toolbarbot = findViewById(R.id.bottom_toolbar);
        toolbarbot.bringToFront();
        toolbar.bringToFront();
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
                //TODO:gör så att backend skickar ut message till båda istället för att skriva det själv
                try {
                    String msg = "{\"userid\":\""+AccountModel.getMyAccount().getId()+"\",\"username\": \"me\",\"messageBody\":\""+message.getText().toString()+"\",\"timeStamp\":\""+ new Timestamp(new Date().getTime()) +"\"}";
                    Log.i("Send",msg);
                    Messages.getMessages().add(new JSONObject(msg));
                }catch(JSONException e){
                    Log.i("Send",e.getMessage());
                }
                listView.invalidateViews();
                //adapter = new MessageViewAdapter(context,Messages.getMessages());
                //listView.setAdapter(adapter);
                Messages.sendMessage(AccountModel.getMyAccount().getId(),AccountModel.getTargetAccount().getId(),message.getText().toString());
                Log.i("send",message.getText().toString());
                message.setText("");
            }
        });
        sendBut.bringToFront();
        backBut.bringToFront();
        Messages.resetMessages();
        context = this;
        listView = findViewById(R.id.messageList);
        progress = findViewById(R.id.progressBar4);
        adapter = new MessageViewAdapter(this,Messages.getMessages());
        listView.setAdapter(adapter);
        getMessagesTask = new RequestTask(new RequestTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(FirebaseMessage result) {
                Messages.resetMessages();
                try {
                    JSONArray messagesjson = result.getInformation().getJSONArray("messages");
                    Log.i("arg",messagesjson.toString());
                    for(int i=0;i<messagesjson.length();i++){
                        Messages.getMessages().add(((JSONObject)messagesjson.get(i)));
                    }
                    adapter = new MessageViewAdapter(context,Messages.getMessages());
                    listView.setAdapter(adapter);
                }catch(JSONException|NullPointerException e){
                    Toast.makeText(getApplicationContext(),"Couldn't retreive messages",Toast.LENGTH_LONG);
                    Log.i("ChatRoom",e.getMessage());
                }
                progress.setVisibility(View.INVISIBLE);
            }
        }, Messages.getMessages(AccountModel.getMyAccount().getId(),AccountModel.getTargetAccount().getId()));
        getMessagesTask.execute();
        progress.setVisibility(View.VISIBLE);
        progress.bringToFront();
        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {

            @Override
            public void run() {
                if(MyFirebaseMessaging.isMsgReceived()) {
                    MyFirebaseMessaging.setMsgReceived(false);
                    listView.invalidateViews();
                }
                handler.postDelayed( this,  1000 );
            }
        },  1000 );
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
