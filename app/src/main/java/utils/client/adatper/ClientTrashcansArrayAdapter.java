package utils.client.adatper;

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
import utils.dataconverter.DateConverter;
import www.ragnsells.ee.ragnsells.R;

/**
 * Created by Andreas on 11.05.2017.
 */

public class ClientTrashcansArrayAdapter extends ArrayAdapter<Trashcan>{

    private Activity activity;
    private List<Trashcan> trashcans;

    public ClientTrashcansArrayAdapter(Activity activity, Context context, List<Trashcan> trashcans) {
        super(context, R.layout.item_manager, trashcans);
        this.activity = activity;
        this.trashcans = trashcans;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert vi != null;
            convertView = vi.inflate(R.layout.item_client_log, parent, false);
            String issue = "";
            String added = "";
            String place ="";

            if(getItem(position) != null) {
                issue = getItem(position).getIssue();
                added = DateConverter.convertDateToReadableFormatNoTime(getItem(position).getCreated_at());
                place = getItem(position).getPlace_name();
            }
            TextView issueTxt = (TextView)convertView.findViewById(R.id.issue);
            TextView addedText = (TextView)convertView.findViewById(R.id.tv_trashcan_order_added);
            TextView placeText = (TextView)convertView.findViewById(R.id.tv_place_name);

            issueTxt.setText(issue);
            addedText.setText(added);
            placeText.setText(place);
            // [...] the rest of initialization part
        }
        // [...] some changes that must be done at refresh
        return convertView;
    }

    public void removeItem(int position){
        if(trashcans != null){
            trashcans.remove(position);
        }
    }
}
