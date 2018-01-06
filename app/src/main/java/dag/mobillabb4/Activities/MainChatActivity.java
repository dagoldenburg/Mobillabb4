package dag.mobillabb4.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import dag.mobillabb4.CustomAdapters.ListViewAdapter;
import dag.mobillabb4.Menu.Menu;
import dag.mobillabb4.R;

public class MainChatActivity extends AppCompatActivity {

    private ListView listView;
    private Context context;
    private ImageButton mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.options_menu);
        toolbar.setOnMenuItemClickListener(new Menu(this));
        context = this;
        listView = findViewById(R.id.listView);
        mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsActivity.class);
                startActivity(intent);
            }
        });
        ArrayList<String> messages = new ArrayList<>();

        for(int i =0;i<5;i++){
            messages.add("message "+i);
        }
        ListViewAdapter adapter = new ListViewAdapter(this,messages);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(context, ChatRoomActivity.class);
                startActivity(intent);
            }
        });
    }
}
