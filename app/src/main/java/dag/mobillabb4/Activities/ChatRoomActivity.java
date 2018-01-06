package dag.mobillabb4.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import dag.mobillabb4.CustomAdapters.MessageViewAdapter;
import dag.mobillabb4.Menu.Menu;
import dag.mobillabb4.R;

public class ChatRoomActivity extends AppCompatActivity {

    private Context context;
    private ListView listView;

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
            }
        });
        sendBut.bringToFront();

        context = this;
        listView = findViewById(R.id.messageList);
        ArrayList<String> messages = new ArrayList<>();

        for(int i =0;i<5;i++){
            messages.add("message "+i);
        }
        MessageViewAdapter adapter = new MessageViewAdapter(this,messages);
        listView.setAdapter(adapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
