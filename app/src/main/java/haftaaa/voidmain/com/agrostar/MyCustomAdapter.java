package haftaaa.voidmain.com.agrostar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by sanju on 03-01-2016.
 */
public class MyCustomAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ListItem> OfferWallItems;

    public MyCustomAdapter(Context context, ArrayList<ListItem> offerWallItems){
        this.context = context;
        this.OfferWallItems = offerWallItems;
    }

    @Override
    public int getCount() {
        return OfferWallItems.size();
    }

    @Override
    public Object getItem(int position) {
        return OfferWallItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView subTitle = (TextView) convertView.findViewById(R.id.price);



        imgIcon.setImageResource(OfferWallItems.get(position).getIcon());
        txtTitle.setText(OfferWallItems.get(position).getTitle());
        subTitle.setText(OfferWallItems.get(position).getSubtitle());


        return convertView;
    }
}
