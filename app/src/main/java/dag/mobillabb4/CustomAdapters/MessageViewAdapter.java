package dag.mobillabb4.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dag.mobillabb4.R;

/**
 * Created by Dag on 1/3/2018.
 */

public class MessageViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> dataSource;

    public MessageViewAdapter(Context context, ArrayList<String> items) {
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
        View rowView;
        TextView textView;
        TextView textView2;
        if(position%2==0) {
            rowView = inflater.inflate(R.layout.message_item_conversation, parent, false);
            textView = rowView.findViewById(R.id.leftFirst);
            textView2 = rowView.findViewById(R.id.leftSecond);
        }else{
            rowView = inflater.inflate(R.layout.message_item_conversation_right, parent, false);
            textView = rowView.findViewById(R.id.rightFirst);
            textView2 = rowView.findViewById(R.id.rightSecond);
        }
        textView.setText(dataSource.get(position));
        textView2.setText(dataSource.get(position) + ":)");
        return rowView;
    }
}
