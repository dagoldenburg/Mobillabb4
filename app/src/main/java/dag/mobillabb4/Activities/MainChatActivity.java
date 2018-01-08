package dag.mobillabb4.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import dag.mobillabb4.CustomAdapters.ListViewAdapter;
import dag.mobillabb4.Firebase.FirebaseMessage;
import dag.mobillabb4.Firebase.Messages;
import dag.mobillabb4.Firebase.MyFirebaseInstance;
import dag.mobillabb4.Firebase.MyFirebaseMessaging;
import dag.mobillabb4.Menu.Menu;
import dag.mobillabb4.Model.AccountModel;
import dag.mobillabb4.R;
import dag.mobillabb4.Tasks.RequestTask;

public class MainChatActivity extends AppCompatActivity {

    private ListView listView;
    private Context context;
    private ImageButton mapButton;
    private RequestTask getConversationTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyFirebaseMessaging.cleanUpMessageHeap();
        setContentView(R.layout.activity_chat);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.options_menu);
        toolbar.setOnMenuItemClickListener(new Menu(this));
        context = this;
        mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<AccountModel> conversations = new ArrayList<>();
        ListViewAdapter adapter = new ListViewAdapter(this,conversations);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(context, ChatRoomActivity.class);
                startActivity(intent);
            }
        });
            getConversationTask = new RequestTask(new RequestTask.OnTaskCompleted() {
                @Override
                public void onTaskCompleted(FirebaseMessage result) {
                    Log.i("Conversations",result.getInformation());
                }
            }, Messages.getChatContacts(AccountModel.getMyAccount().getId()));
    getConversationTask.execute();
    }
}
