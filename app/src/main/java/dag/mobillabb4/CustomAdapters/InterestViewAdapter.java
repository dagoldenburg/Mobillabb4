package dag.mobillabb4.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dag.mobillabb4.Model.ConversationModel;
import dag.mobillabb4.Model.InterestModel;
import dag.mobillabb4.R;

/**
 * Created by Dag on 1/14/2018.
 */

public class InterestViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<InterestModel> dataSource;
    private ArrayList<InterestModel> dataSourceUser;

    public InterestViewAdapter(Context context, ArrayList<InterestModel> items,ArrayList<InterestModel> userItems) {
        this.context = context;
        dataSource = items;
        dataSourceUser = userItems;
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

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.interest_item, parent, false);
        ImageView img = rowView.findViewById(R.id.interestIcon);
        TextView text = rowView.findViewById(R.id.interestItem);
        text.setText(dataSource.get(position).getName());
        for(InterestModel im : dataSourceUser) {
            if (im.getId() == dataSource.get(position).getId()) {
                img.setImageResource(android.R.drawable.checkbox_on_background);
            }
        }

        return rowView;
    }
}
