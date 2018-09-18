package utils.manager.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import model.Trashcan;
import www.ragnsells.ee.ragnsells.R;

/**
 * Created by Andreas on 11.05.2017.
 */

public class ManagerArrayAdapter extends ArrayAdapter<Trashcan>{

    private Activity activity;

    public ManagerArrayAdapter(Activity activity, Context context, List<Trashcan> trashcan) {
        super(context, R.layout.item_manager, trashcan);
        this.activity = activity;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = vi.inflate(R.layout.item_manager, parent, false);
            String clientEmail ="";
            String issue = "";
            String placeName ="";
            if(getItem(position) != null) {
                clientEmail = getItem(position).getEmail();
                issue = getItem(position).getIssue();
                placeName = getItem(position).getPlace_name();
            }
            TextView cleintEmailTxt = (TextView)convertView.findViewById(R.id.client_email);
            TextView issueTxt = (TextView)convertView.findViewById(R.id.issue);
            TextView tvPlaceAddress = (TextView)convertView.findViewById(R.id.tv_place_name);

            cleintEmailTxt.setText(clientEmail);
            issueTxt.setText(issue);
            tvPlaceAddress.setText(placeName);

            // [...] the rest of initialization part
        }
        // [...] some changes that must be done at refresh
        return convertView;
    }
}
