package dag.mobillabb4.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dag.mobillabb4.Model.AccountModel;
import dag.mobillabb4.Model.ConversationModel;
import dag.mobillabb4.R;

/**
 * Created by Dag on 1/3/2018.
 */

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ConversationModel> dataSource;

    public ListViewAdapter(Context context, ArrayList<ConversationModel> items) {
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
        View rowView = inflater.inflate(R.layout.list_item_conversation, parent, false);
        TextView textView =  rowView.findViewById(R.id.firstLine);
        TextView textView2 =  rowView.findViewById(R.id.secondLine);
        TextView textView3 =  rowView.findViewById(R.id.thirdLine);
        ImageView imageView = rowView.findViewById(R.id.icon);
        textView.setText(dataSource.get(position).getOwner().getUsername());
        textView2.setText(dataSource.get(position).getMessage());
        textView3.setText(dataSource.get(position).getDate());


        imageView.setImageResource(R.drawable.ic_launcher_background);

        return rowView;
    }


}
