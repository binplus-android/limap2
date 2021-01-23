package com.limap.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.limap.Adapter.CategoryAddRVAdapter;
import com.limap.BaseController;
import com.limap.Interface.APIService;
import com.limap.Model.APIUrl;
import com.limap.Model.SetterAllPostDetails;
import com.limap.Pref.Pref;
import com.limap.R;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchDetailsActivity extends AppCompatActivity
{
    private SwipeRefreshLayout swipeContainer;;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerViewlayoutManager;
    private ArrayList<SetterAllPostDetails> listing;
    private CategoryAddRVAdapter recyclerAdapter;

    //public JSONArray jsonArray;

    private boolean loadMore = true;
    private int index = 0;

    private int cnt=0;

    String category="",speciality="",variety="";
    Toolbar toolbar;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add);
        //setTitle("MY POST");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Search Result");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Your code
                finish();
            }
        });
        bundle = getIntent().getExtras();
        category    =   bundle.getString("category");
        speciality  =   bundle.getString("speciality");
        variety     =   bundle.getString("variety");

        swipeContainer = findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                index = 0;

                //     jsonArray = null;

                readAdds();

                loadMore = false;

            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //list of history
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); //if recycler size is fixed
        //RecyclerView Layout
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
            @Override
            public void onScrolledToEnd() {

                if (loadMore) {

                    index += 10;

                    readAdds();

                    loadMore = false;

                }
            }
        });

        // jsonArray = null;
        index = 0;
        readAdds();
    }

    private void readAdds() {
        cnt++;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait...");
        progressDialog.show();
        try {

            if (BaseController.isNetworkAvailable(getApplicationContext())) {
                swipeContainer.setRefreshing(true);

                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                // set your desired log level
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                // add your other interceptors …
                // add logging as last interceptor
                httpClient.addInterceptor(logging);  // <-- this is the important line

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(APIUrl.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();

                APIService service = retrofit.create(APIService.class);
                Call<List<SetterAllPostDetails>> call = service.searchDetailsView(category,speciality,variety);
                call.enqueue(new Callback<List<SetterAllPostDetails>>()
                {
                    @Override
                    public void onResponse(Call<List<SetterAllPostDetails>> call, Response<List<SetterAllPostDetails>> response)
                    {
                        progressDialog.dismiss();

                        List<SetterAllPostDetails> datumList1 = response.body();
                        if(datumList1.size()>0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerAdapter = new CategoryAddRVAdapter(datumList1, getApplicationContext());
                            RecyclerView.LayoutManager recyce = new LinearLayoutManager(getApplicationContext());

                            recyclerView.setLayoutManager(recyce);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(recyclerAdapter);
                            recyclerAdapter.notifyDataSetChanged();

                            swipeContainer.setRefreshing(false);
                        }
                        else
                        {

                            swipeContainer.setRefreshing(false);
                            progressDialog.dismiss();
                            recyclerView.setVisibility(View.GONE);


                        }
                    }
                    @Override
                    public void onFailure(Call<List<SetterAllPostDetails>> call, Throwable t)
                    {
                        progressDialog.dismiss();
                        //   Toast.makeText(getApplicationContext(),"Invalid contact number", Toast.LENGTH_LONG).show();
                    }
                });

            } else {

                swipeContainer.setRefreshing(false);
                recyclerView.setVisibility(View.GONE);
                //emptyview.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Connect to network and refresh", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            swipeContainer.setRefreshing(false);
            Log.e("ERORR", "" + e);
        }
    }


}
