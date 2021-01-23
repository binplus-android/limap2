package com.limap.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.limap.Activity.ViewAddActivity;
import com.limap.Model.SetterAllPostDetails;
import com.limap.Model.SetterDoctorList;
import com.limap.R;

import java.util.List;

public class DoctorRVAdapter extends  RecyclerView.Adapter<DoctorRVAdapter.ViewHolder> {
    static Context context;
    private LayoutInflater inflater;
    public static List<SetterDoctorList> getDataAdapter;

    public DoctorRVAdapter(List<SetterDoctorList> getDataAdapter) {
        this.getDataAdapter = getDataAdapter;
    }

    public DoctorRVAdapter(List<SetterDoctorList> getDataAdapter, Context context) {
        super();
        inflater = LayoutInflater.from(context);
        this.getDataAdapter = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_doc_cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int pos = position;
        final SetterDoctorList getDataAdapter1 = getDataAdapter.get(position);
        holder.tvDocName.setText(getDataAdapter1.getName());
        holder.tvAddress.setText(getDataAdapter1.getAddress());
        holder.tvCity.setText(getDataAdapter1.getCity()+",");
        holder.tvServingVillage.setText(getDataAdapter1.getServing_village());
        holder.tvSpeciality.setText("Speciality : "+ getDataAdapter1.getSpeciality());

        if(!getDataAdapter1.getMobile_no().equals(""))
        {
            holder.linearLayoutbtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getDataAdapter1.getMobile_no()));
                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 143);
                    }
                    else {
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return getDataAdapter.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDocName;
        TextView tvAddress;
        TextView tvCity;
        TextView tvServingVillage;
        TextView tvSpeciality;
        LinearLayout linearLayoutbtn;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDocName = (TextView) itemView.findViewById(R.id.tvDocName);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            tvCity = (TextView) itemView.findViewById(R.id.tvCity);
            tvServingVillage = (TextView) itemView.findViewById(R.id.tvServingVillage);
            tvSpeciality = (TextView) itemView.findViewById(R.id.tvSpeciality);
            linearLayoutbtn = (LinearLayout) itemView.findViewById(R.id.linearLayoutbtn);

        }

        @Override
        public void onClick(View view) {
            Log.d("Onclick", "onClick " + getPosition() + " ");
        }
    }
}
