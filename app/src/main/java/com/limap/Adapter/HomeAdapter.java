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

import java.io.Serializable;
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
        final SetterAllPostDetails details = dataList.get(position);
        String vet  =   "";
        if(!details.getCategory().equals("ox")) {
            vet =  " ( " + details.getVariety() + " )";
        }
        holder.tv_title.setText(details.getCategory()+vet+" - "+details.getSeller_name());
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
        ItemImageAdapter imageAdapter = new ItemImageAdapter(imageList,context,details);
        holder.rv_items.setAdapter(imageAdapter);

//holder.rv_items.addOnItemTouchListener(new RecyclerTouchListener(context, holder.rv_items, new RecyclerTouchListener.OnItemClickListener() {
//    @Override
//    public void onItemClick(View view, int position) {
//
//        Intent i = new Intent(context, ViewAddActivity.class);
//
//        i.putExtra("id",details.getTbl_add_post_data_id());
//        i.putExtra("category",details.getCategory());
//        i.putExtra("mobile_no",details.getMobile_no());
//        i.putExtra("speciality",details.getSpeciality());
//        i.putExtra("variety",details.getVariety());
//        i.putExtra("age",details.getAge());
//        i.putExtra("vet",details.getVet());
//        i.putExtra("milkhistory",details.getLastmilkhistory());
//        i.putExtra("description",details.getDescription());
//        i.putExtra("image1",details.getImage1());
//        i.putExtra("image2",details.getImage2());
//        i.putExtra("image3",details.getImage3());
//        //   i.putExtra("image4",details.getImage4());
//        i.putExtra("image5",details.getImage5());
//        i.putExtra("model",(Serializable) details);
//        //   i.putExtra("video1",details.getVideo1());
//        i.setFlags(FLAG_ACTIVITY_NEW_TASK);
//      context.startActivity(i);
//
//    }
//
//    @Override
//    public void onLongItemClick(View view, int position) {
//
//    }
//}));


    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
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
       SetterAllPostDetails setterAllPostDetails;

       public ItemImageAdapter(ArrayList<String> imgList, Context context ,SetterAllPostDetails setterAllPostDetails) {
           this.imgList = imgList;
           this.context = context;
           this.setterAllPostDetails = setterAllPostDetails;
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
           holder.imageView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent i = new Intent(context, ViewAddActivity.class);
                   i.putExtra("id",setterAllPostDetails.getTbl_add_post_data_id());
        i.putExtra("category",setterAllPostDetails.getCategory());
        i.putExtra("mobile_no",setterAllPostDetails.getMobile_no());
        i.putExtra("speciality",setterAllPostDetails.getSpeciality());
        i.putExtra("variety",setterAllPostDetails.getVariety());
        i.putExtra("age",setterAllPostDetails.getAge());
        i.putExtra("vet",setterAllPostDetails.getVet());
        i.putExtra("milkhistory",setterAllPostDetails.getLastmilkhistory());
        i.putExtra("description",setterAllPostDetails.getDescription());
        i.putExtra("image1",setterAllPostDetails.getImage1());
        i.putExtra("image2",setterAllPostDetails.getImage2());
        i.putExtra("image3",setterAllPostDetails.getImage3());
        //   i.putExtra("image4",setterAllPostDetails.getImage4());
        i.putExtra("image5",setterAllPostDetails.getImage5());
        i.putExtra("model",(Serializable) setterAllPostDetails);
        //   i.putExtra("video1",details.getVideo1());
                   i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                   context.startActivity(i);
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
