package dag.mobillabb4.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private ProgressBar progress;
    private EditText search;
    private ListViewAdapter adapter;

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
        progress = findViewById(R.id.progressBar3);
        getConversationTask = new RequestTask(new RequestTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(FirebaseMessage result) {
                //Log.i("Conversations",result.getInformation());
                try{
                JSONArray conversations = result.getInformation().getJSONArray("conversation");
                Log.i("hejhej2",conversations.toString());
                ArrayList<AccountModel> temp = new ArrayList<>();
                for(int i = 0;i<conversations.length();i++){
                    temp.add(new AccountModel(Integer.parseInt(((JSONObject) conversations.get(i)).get("id").toString()),
                            ((JSONObject) conversations.get(i)).get("username").toString()));
                }
                AccountModel.setConversations(temp);

                    AccountModel.setFilteredConversations(temp);
                adapter = new ListViewAdapter(getApplicationContext(),AccountModel.getFilteredConversations());
                listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Log.i("lol",""+position);
                            AccountModel.setTargetAccount(AccountModel.getFilteredConversations().get(position));
                            Intent intent = new Intent(context, ChatRoomActivity.class);
                            startActivity(intent);
                        }
                    });
                }catch(NullPointerException|JSONException e){
                    e.printStackTrace();
                    Toast.makeText(context, "No conversations to show", Toast.LENGTH_LONG).show();
                }
                progress.setVisibility(View.INVISIBLE);
                AccountModel.setConversations(null);
            }
        }, Messages.getChatContacts(AccountModel.getMyAccount().getId()));
        getConversationTask.execute();
        progress.setVisibility(View.VISIBLE);
        progress.bringToFront();

        listView = findViewById(R.id.listView);



        search = findViewById(R.id.mySearchText);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("text","change");
                AccountModel.filterConversations(s.toString());
                adapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

}
