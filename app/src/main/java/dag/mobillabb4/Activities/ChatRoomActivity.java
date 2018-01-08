package dag.mobillabb4.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

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
    private static long msgId = 0;
    private int targetId;
    private RequestTask getMessagesTask;


    public ChatRoomActivity(int targetId){
        this.targetId = targetId;
    }

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
        sendBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: skicka meddelande till backend
                FirebaseMessaging fm = FirebaseMessaging.getInstance();
                fm.send(new RemoteMessage.Builder("838320272447" + "@gcm.googleapis.com")
                        .setMessageId(Long.toString(msgId++))
                        .addData("my_message", "Hello World")
                        .addData("my_action","SAY_HELLO")
                        .build());
            }
        });
        sendBut.bringToFront();

        context = this;
        listView = findViewById(R.id.messageList);
        ArrayList<String> messages = new ArrayList<>();

        MessageViewAdapter adapter = new MessageViewAdapter(this,messages);
        listView.setAdapter(adapter);
        getMessagesTask = new RequestTask(new RequestTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(FirebaseMessage result) {

            }
        }, Messages.getMessages(AccountModel.getMyAccount().getId(),targetId));
        getMessagesTask.execute();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
