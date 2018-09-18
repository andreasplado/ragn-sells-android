package utils.login;

import android.widget.ListAdapter;

import presenter.LoginActivityPersenter;
import www.ragnsells.ee.ragnsells.R;

/**
 * RagnSells
 * Created by Andreas on 25.05.2017.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;

/**
 * Created by daimajia on 14-5-29.
 */
public class BannerAdapter extends BaseAdapter{
    private Context mContext;
    public BannerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return SliderLayout.Transformer.values().length;
    }

    @Override
    public Object getItem(int position) {
        return SliderLayout.Transformer.values()[position].toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView t = (TextView)LayoutInflater.from(mContext).inflate(R.layout.banner_item,null);
        t.setText(getItem(position).toString());
        return t;
    }
}
