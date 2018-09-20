package utils.manager;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import data.ResponseData;
import model.Trashcan;
import model.Trashcans;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Alert;
import utils.login.LoginManager;
import utils.manager.adapter.ManagerArrayAdapter;
import utils.retrofit.Constants;
import utils.retrofit.RetrofitBuilder;
import utils.retrofit.TrashcansEndpoint;
import utils.sqlite.TrashcanHandler;
import www.ragnsells.ee.ragnsells.R;

/**
 * Created by Andreas on 10.05.2017.
 */

public class ManagerManager {

    private Activity activity;
    public Context context;
    private static RetrofitBuilder retrofitBuilder = new RetrofitBuilder(Constants.URL_BASE);

    public ManagerManager(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    public void getAllTrashcansWithIssues(final ListView managerListView, final SwipeRefreshLayout swipeRefreshLayout){
        new Thread(new Runnable() {
            public void run() {
                TrashcansEndpoint apiService = RetrofitBuilder.getClient().create(TrashcansEndpoint.class);
                Call<ArrayList<Trashcan>> call = apiService.getAllTrashcansWithIssues(LoginManager.retrieveToken(context));

                retrofitBuilder.build();
                call.enqueue(new Callback<ArrayList<Trashcan>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Trashcan>> call, Response<ArrayList<Trashcan>> response) {
                        if(response.body() != null) {
                            final ArrayList<Trashcan> trashcans = response.body();
                            //Save to SQLITE
                            TrashcanHandler db = new TrashcanHandler(context);
                            db.deleteTrashcans();
                            for(int i=0; i<trashcans.size(); i++){
                                db.addTrashcan(trashcans.get(i));
                            }

                            swipeRefreshLayout.setRefreshing(false);
                            Trashcans.setTrashcans(trashcans);
                            final ListAdapter managerListAdapter = new ManagerArrayAdapter(activity, context, Trashcans.getTrashcans());
                            managerListView.setAdapter(managerListAdapter);
                            managerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Alert.trashcanIssueDetailViewAlert(trashcans.get(position).getId(), position, managerListView, ManagerManager.this, managerListAdapter);
                                }
                            });
                            if(trashcans.size()< 1){
                                activity.findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
                            }else {
                                activity.findViewById(R.id.empty_list).setVisibility(View.GONE);
                            }
                        }else{
                            swipeRefreshLayout.setRefreshing(false);
                            Alert.getCannotGetGPSAlert();

                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Trashcan>> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        //Alert.getCannotGetGPSAlert();
                        //Save to SQLITE
                        TrashcanHandler db = new TrashcanHandler(context);
                        final ArrayList<Trashcan> trashcans = db.getAllTrashcans();
                        Trashcans.setTrashcans(trashcans);
                        final ListAdapter managerListAdapter = new ManagerArrayAdapter(activity, context, Trashcans.getTrashcans());
                        managerListView.setAdapter(managerListAdapter);
                        managerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Alert.trashcanIssueDetailViewAlert(trashcans.get(position).getId(), position, managerListView, ManagerManager.this, managerListAdapter);
                            }
                        });
                        //Hides the listview and shows no results
                        if(trashcans.size()< 1){
                            activity.findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
                        }else {
                            activity.findViewById(R.id.empty_list).setVisibility(View.GONE);
                        }
                    }
                });
            }
        }).start();
    }

    public void deleteTrashcanIssue(final int id, final int position, final ListView managerListView, final ListAdapter managerListAdatper){
        new Thread(new Runnable() {
            public void run() {
                TrashcansEndpoint apiService = RetrofitBuilder.getClient().create(TrashcansEndpoint.class);
                retrofitBuilder.build();
                Call<ResponseData> call = apiService.updateTrashcan(id, "", LoginManager.retrieveToken(context));
                call.enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if(response.body() != null) {
                            Trashcans.getTrashcans().remove(position);
                            managerListView.setAdapter(managerListAdatper);
                            ((BaseAdapter)managerListAdatper).notifyDataSetChanged();

                        }else{
                            Alert.getCannotGetGPSAlert();

                        }
                        if(managerListView.getCount() < 1){
                            activity.findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
                        }else {
                            activity.findViewById(R.id.empty_list).setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        //Alert.getCannotGetGPSAlert();
                        //Save to SQLITE
                        TrashcanHandler db = new TrashcanHandler(context);
                        final ArrayList<Trashcan> trashcans = db.getAllTrashcans();
                        Trashcans.setTrashcans(trashcans);
                        final ListAdapter managerListAdapter = new ManagerArrayAdapter(activity, context, Trashcans.getTrashcans());
                        managerListView.setAdapter(managerListAdapter);
                        managerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Alert.trashcanIssueDetailViewAlert(trashcans.get(position).getId(), position, managerListView, ManagerManager.this, managerListAdapter);
                            }
                        });
                        //Hides the listview and shows no results
                        if(trashcans.size()< 1){
                            activity.findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
                        }else {
                            activity.findViewById(R.id.empty_list).setVisibility(View.GONE);
                        }

                    }
                });
            }
        }).start();
    }
}
