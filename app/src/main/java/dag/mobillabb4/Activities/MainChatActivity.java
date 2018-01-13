package dag.mobillabb4.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import dag.mobillabb4.Model.ConversationModel;
import dag.mobillabb4.R;
import dag.mobillabb4.Tasks.RequestTask;

public class MainChatActivity extends AppCompatActivity{

    private ListView listView;
    private Context context;
    private ImageButton mapButton;
    private RequestTask getConversationTask;
    private ProgressBar progress;
    private EditText search;
    private ListViewAdapter adapter;
    private RequestTask deleteTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyFirebaseMessaging.cleanUpMessageHeap();
        setContentView(R.layout.activity_chat);

        Log.i("asd", FirebaseInstanceId.getInstance().getToken());
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
                ConversationModel.filterConversations(s.toString());
                adapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        getConversationTask = new RequestTask(new RequestTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(FirebaseMessage result) {
                //Log.i("Conversations",result.getInformation());
                try{
                    //TODO:returnera hela users istället så sparar man dom som accountModel
                    JSONArray conversations = result.getInformation().getJSONArray("conversation");
                    Log.i("hejhej2",conversations.toString());
                    ArrayList<ConversationModel> temp = new ArrayList<>();
                    for(int i = 0;i<conversations.length();i++){
                        temp.add(new ConversationModel(
                                ((JSONObject) conversations.get(i)).get("latestMessage").toString(),
                                ((JSONObject) conversations.get(i)).get("date").toString(),
                                new AccountModel(Integer.parseInt(((JSONObject) conversations.get(i)).get("id").toString()),
                                ((JSONObject) conversations.get(i)).get("username").toString())));
                    }
                    ConversationModel.setConversations(temp);
                    ConversationModel.setFilteredConversations(temp);

                    Log.i("ListView",ConversationModel.getConversations().toString());
                    adapter = new ListViewAdapter(getApplicationContext(),ConversationModel.getFilteredConversations());
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            AccountModel.setTargetAccount(ConversationModel.getFilteredConversations().get(position).getOwner());
                            Intent intent = new Intent(context, ChatRoomActivity.class);
                            startActivity(intent);
                        }
                    });
                    registerForContextMenu(listView);

                }catch(NullPointerException|JSONException e){
                    e.printStackTrace();
                    Toast.makeText(context, "No conversations to show", Toast.LENGTH_LONG).show();
                }

                progress.setVisibility(View.INVISIBLE);
            }
        }, Messages.getChatContacts(AccountModel.getMyAccount().getId()));
        getConversationTask.execute();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.conversation_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int temp = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
        final int targetId = ConversationModel.getConversations().get(temp).getOwner().getId();
        final String targetUser = ConversationModel.getConversations().get(temp).getOwner().getUsername();
        switch (item.getItemId()) {
            case R.id.goToProf:

                AccountModel.setTargetAccount(new AccountModel(targetId,targetUser));
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.deleteConversation:
                Log.i("ListViewMenu","deleteconversation");
                Log.i("ListViewmenu",ConversationModel.getConversations().toString());
                Log.i("ListViewmenu",ConversationModel.getConversations().get(temp).toString());

                Log.i("ListViewMenu",""+targetId);
                deleteTask = new RequestTask(new RequestTask.OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(FirebaseMessage result) {
                        try {
                            if(result.getInformation().get("status").equals("success")){
                                ConversationModel.removeConversation(targetId);
                                adapter = new ListViewAdapter(getApplicationContext(),ConversationModel.getFilteredConversations());
                                listView.setAdapter(adapter);
                            }
                        } catch (JSONException|NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }, Messages.deleteContact(AccountModel.getMyAccount().getId(),targetId));
                deleteTask.execute();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
