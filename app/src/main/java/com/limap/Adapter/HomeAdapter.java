package com.limap.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.limap.Activity.ViewAddActivity;
import com.limap.Model.SetterAllPostDetails;
import com.limap.R;
import com.limap.Utils.RecyclerTouchListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

     Context context;
    public List<SetterAllPostDetails> dataList;

    public HomeAdapter(Context context, List<SetterAllPostDetails> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_rv, parent, false);
      ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull  HomeAdapter.ViewHolder holder, int position) {
        SetterAllPostDetails details = dataList.get(position);
        String vet  =   "";
        if(!details.getCategory().equals("ox")) {
            vet =  " ( " + details.getVariety() + " )";
        }
        holder.tv_title.setText(details.getCategory()+vet);
        float fKm=Float.parseFloat(details.getDistance());
        float f=Math.round(fKm);
        holder.tv_distance.setText(String.valueOf(f)+"km ");
        holder.tv_desc.setText(details.getAddress());

        ArrayList<String>imageList = new ArrayList<>();
        imageList.add(details.getImage1());
        imageList.add(details.getImage2());
        imageList.add(details.getImage3());
        imageList.add(details.getImage5());
        holder.rv_items.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        ItemImageAdapter imageAdapter = new ItemImageAdapter(imageList,context);
        holder.rv_items.setAdapter(imageAdapter);

holder.rv_items.addOnItemTouchListener(new RecyclerTouchListener(context, holder.rv_items, new RecyclerTouchListener.OnItemClickListener() {
    @Override
    public void onItemClick(View view, int position) {
        SetterAllPostDetails detailss = dataList.get(position);
        Intent i = new Intent(context, ViewAddActivity.class);

        i.putExtra("id",detailss.getTbl_add_post_data_id());
        i.putExtra("category",detailss.getCategory());
        i.putExtra("mobile_no",detailss.getMobile_no());
        i.putExtra("speciality",detailss.getSpeciality());
        i.putExtra("variety",detailss.getVariety());
        i.putExtra("age",detailss.getAge());
        i.putExtra("vet",detailss.getVet());
        i.putExtra("milkhistory",detailss.getLastmilkhistory());
        i.putExtra("description",detailss.getDescription());
        i.putExtra("image1",detailss.getImage1());
        i.putExtra("image2",detailss.getImage2());
        i.putExtra("image3",detailss.getImage3());
        //   i.putExtra("image4",details.getImage4());
        i.putExtra("image5",detailss.getImage5());
        //   i.putExtra("video1",details.getVideo1());
        i.setFlags(FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(i);

    }

    @Override
    public void onLongItemClick(View view, int position) {

    }
}));


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rv_items;
        TextView tv_title ,tv_desc,tv_distance;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            rv_items = itemView.findViewById(R.id.rv_items);
            tv_title = itemView.findViewById(R.id.title);
            tv_desc = itemView.findViewById(R.id.description);
            tv_distance = itemView.findViewById(R.id.tv_distance);
        }
    }


   public class ItemImageAdapter extends RecyclerView.Adapter<ItemImageAdapter.ViewHolder>
   {

       ArrayList<String> imgList;
       Context context ;

       public ItemImageAdapter(ArrayList<String> imgList, Context context) {
           this.imgList = imgList;
           this.context = context;
       }

       @NonNull

       @Override
       public ItemImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(context).inflate(R.layout.row_home_item_image,null);
       return new ViewHolder(v);
       }

       @Override
       public void onBindViewHolder(@NonNull final ItemImageAdapter.ViewHolder holder, int position) {
           Picasso.with(context).load(imgList.get(position)).into(holder.imageView, new Callback() {
               @Override
               public void onSuccess() {
                   holder.progressBar.setVisibility(View.GONE);

               }

               @Override
               public void onError() {

               }
           });
           


       }

       @Override
       public int getItemCount() {
           return imgList.size();
       }

       public class ViewHolder extends RecyclerView.ViewHolder {
           ImageView imageView;
           ProgressBar progressBar;
           public ViewHolder(@NonNull View itemView) {
               super(itemView);
               imageView = itemView.findViewById(R.id.image);
              progressBar= itemView.findViewById(R.id.pbar);
           }
       }
   }
}
