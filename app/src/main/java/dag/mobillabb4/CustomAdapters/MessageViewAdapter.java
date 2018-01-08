package dag.mobillabb4.CustomAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dag.mobillabb4.Model.AccountModel;
import dag.mobillabb4.R;

/**
 * Created by Dag on 1/3/2018.
 */

public class MessageViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<JSONObject> dataSource;

    public MessageViewAdapter(Context context, ArrayList<JSONObject> items) {
        this.context = context;
        dataSource = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;
        TextView textView = null;
        TextView textView2 = null;

        try {
        if(dataSource.get(position).get("id")== AccountModel.getMyAccount().getId()) {
            rowView = inflater.inflate(R.layout.message_item_conversation, parent, false);
            textView = rowView.findViewById(R.id.leftFirst);
            textView2 = rowView.findViewById(R.id.leftSecond);
        } else {
            rowView = inflater.inflate(R.layout.message_item_conversation_right, parent, false);
            textView = rowView.findViewById(R.id.rightFirst);
            textView2 = rowView.findViewById(R.id.rightSecond);
        }
        textView.setText(dataSource.get(position).get("username").toString());
        textView2.setText(dataSource.get(position).get("content").toString());
        }catch(JSONException e){
            Log.i("MessageViewAdapter",e.getMessage());
        }
        return rowView;
    }
}
