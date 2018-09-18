package utils.client;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import data.ResponseData;
import model.Trashcan;
import model.Trashcans;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Alert;
import utils.client.adatper.ClientTrashcansArrayAdapter;
import utils.loading.LoadingManager;
import utils.login.LoginManager;
import utils.retrofit.Constants;
import utils.retrofit.RetrofitBuilder;
import utils.retrofit.TrashcansEndpoint;
import utils.sqlite.TrashcanHandler;
import www.ragnsells.ee.ragnsells.R;

/**
 * Created by Andreas on 18.05.2017.
 */

public class ClientManager {

    public Activity activity;
    public Context context;
    private static RetrofitBuilder retrofitBuilder = new RetrofitBuilder(Constants.URL_BASE);

    public ClientManager(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }


    public void getAllClientTrashcans(final ListView clientLogListView, final SwipeRefreshLayout swipeRefreshLayout) {
        TrashcansEndpoint apiService = RetrofitBuilder.getClient().create(TrashcansEndpoint.class);
        retrofitBuilder.build();
        Call<ArrayList<Trashcan>> call = apiService.getAllUserTrashcans(LoadingManager.retrieveUserId(context), LoginManager.retrieveToken(context));
        call.enqueue(new Callback<ArrayList<Trashcan>>() {
            @Override
            public void onResponse(Call<ArrayList<Trashcan>> call, Response<ArrayList<Trashcan>> response) {
                if(response.body() != null) {

                    final ArrayList<Trashcan> trashcans = response.body();

                    TrashcanHandler db = new TrashcanHandler(context);
                    db.deleteTrashcans();
                    for(int i=0; i<trashcans.size(); i++){
                        db.addTrashcan(trashcans.get(i));
                    }



                    Collections.reverse(trashcans);
                    Trashcans.setTrashcans(trashcans);
                    final ArrayList<Trashcan> trashcansList = Trashcans.getTrashcans();
                    final ListAdapter clientTrashcansAdatper = new ClientTrashcansArrayAdapter(activity, context, Trashcans.getTrashcans());
                    swipeRefreshLayout.setRefreshing(false);
                    clientLogListView.setAdapter(clientTrashcansAdatper);
                    clientLogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Alert.deleteTrashcanLogAlert(trashcans.get(position).getId(), position, clientLogListView, ClientManager.this, clientTrashcansAdatper);
                        }
                    });
                    if(trashcans.size() < 1){
                        clientLogListView.setVisibility(View.GONE);
                        activity.findViewById(R.id.empty_list_client_log).setVisibility(View.VISIBLE);
                    }else {
                        clientLogListView.setVisibility(View.VISIBLE);
                        activity.findViewById(R.id.empty_list_client_log).setVisibility(View.GONE);
                    }
                }else{
                    swipeRefreshLayout.setRefreshing(false);
                    Alert.info(context, "Viga", "Serveri viga");

                }

            }

            @Override
            public void onFailure(Call<ArrayList<Trashcan>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                //TODO: make this
                TrashcanHandler db = new TrashcanHandler(context);
                final ArrayList<Trashcan> trashcans = db.getAllTrashcans();
                Trashcans.setTrashcans(trashcans);
                final ListAdapter clientTrashcansAdatper = new ClientTrashcansArrayAdapter(activity, context, Trashcans.getTrashcans());
                swipeRefreshLayout.setRefreshing(false);
                clientLogListView.setAdapter(clientTrashcansAdatper);
                clientLogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Alert.deleteTrashcanLogAlert(trashcans.get(position).getId(), position, clientLogListView, ClientManager.this, clientTrashcansAdatper);
                    }
                });
                if(trashcans.size()< 1){
                    activity.findViewById(R.id.empty_list_client_log).setVisibility(View.VISIBLE);
                }else {
                    activity.findViewById(R.id.empty_list_client_log).setVisibility(View.GONE);
                }

            }
        });
    }

    public void deleteClientTrashcan(final int trashcanId, final int position, final ListAdapter clientTrashcansAdatper, final ListView clientLogListView) {
        new Thread(new Runnable() {
            public void run() {
                TrashcansEndpoint apiService = RetrofitBuilder.getClient().create(TrashcansEndpoint.class);
                retrofitBuilder.build();
                Call<ResponseData> call = apiService.deleteTrashcan(trashcanId, LoginManager.retrieveToken(context));
                call.enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if(response.body() != null) {
                            Trashcans.getTrashcans().remove(position);
                            clientLogListView.setAdapter(clientTrashcansAdatper);
                            ((BaseAdapter)clientTrashcansAdatper).notifyDataSetChanged();
                            if(clientLogListView.getCount() < 1){
                                activity.findViewById(R.id.empty_list_client_log).setVisibility(View.VISIBLE);
                            }else {
                                activity.findViewById(R.id.empty_list_client_log).setVisibility(View.GONE);
                            }

                        }else{
                            Alert.info(context, "Viga", "Serveri viga");

                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        Alert.info(context, "Viga", "Palun ühendage end võrguga");
                    }
                });
            }
        }).start();
    }
}
