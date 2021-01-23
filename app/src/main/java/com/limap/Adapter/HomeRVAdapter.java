package com.limap.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.limap.Activity.ViewAddActivity;
import com.limap.Model.SetterAllPostDetails;
import com.limap.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by Admin on 10/10/2015.
 */
public class HomeRVAdapter extends  RecyclerView.Adapter<HomeRVAdapter.ViewHolder> {
    static Context context;
    private LayoutInflater inflater;
    public static List<SetterAllPostDetails> getDataAdapter;

    public HomeRVAdapter(List<SetterAllPostDetails> getDataAdapter) {
        this.getDataAdapter = getDataAdapter;
    }

    public HomeRVAdapter(List<SetterAllPostDetails> getDataAdapter, Context context) {
        super();
        inflater = LayoutInflater.from(context);
        this.getDataAdapter = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_add_cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int pos = position;
        final SetterAllPostDetails getDataAdapter1 = getDataAdapter.get(position);
        // userConatctNo.setText(Html.fromHtml("<u>"+response.body().getUser_contact()+"</u>"));
        String vet  =   "";
        if(!getDataAdapter1.getCategory().equals("ox")) {
            vet =  " ( " + getDataAdapter1.getVariety() + " )";
        }
        holder.title.setText(getDataAdapter1.getCategory()+vet);
        float fKm=Float.parseFloat(getDataAdapter1.getDistance());
        float f=Math.round(fKm);
        holder.tv_distance.setText(String.valueOf(f)+"km ");
        holder.description.setText(getDataAdapter1.getAddress());

        holder.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), ViewAddActivity.class);
                i.putExtra("id",getDataAdapter1.getTbl_add_post_data_id());
                i.putExtra("category",getDataAdapter1.getCategory());
                i.putExtra("mobile_no",getDataAdapter1.getMobile_no());
                i.putExtra("speciality",getDataAdapter1.getSpeciality());
                i.putExtra("variety",getDataAdapter1.getVariety());
                i.putExtra("age",getDataAdapter1.getAge());
                i.putExtra("vet",getDataAdapter1.getVet());
                i.putExtra("milkhistory",getDataAdapter1.getLastmilkhistory());
                i.putExtra("description",getDataAdapter1.getDescription());
                i.putExtra("image1",getDataAdapter1.getImage1());
                i.putExtra("image2",getDataAdapter1.getImage2());
                i.putExtra("image3",getDataAdapter1.getImage3());
           //     i.putExtra("image4",getDataAdapter1.getImage4());
                i.putExtra("image5",getDataAdapter1.getImage5());
           //     i.putExtra("video1",getDataAdapter1.getVideo1());
                v.getContext().startActivity(i);
            }
        });

        holder.image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), ViewAddActivity.class);
                i.putExtra("id",getDataAdapter1.getTbl_add_post_data_id());
                i.putExtra("category",getDataAdapter1.getCategory());
                i.putExtra("mobile_no",getDataAdapter1.getMobile_no());
                i.putExtra("speciality",getDataAdapter1.getSpeciality());
                i.putExtra("variety",getDataAdapter1.getVariety());
                i.putExtra("age",getDataAdapter1.getAge());
                i.putExtra("vet",getDataAdapter1.getVet());
                i.putExtra("milkhistory",getDataAdapter1.getLastmilkhistory());
                i.putExtra("description",getDataAdapter1.getDescription());
                i.putExtra("image1",getDataAdapter1.getImage1());
                i.putExtra("image2",getDataAdapter1.getImage2());
                i.putExtra("image3",getDataAdapter1.getImage3());
            //    i.putExtra("image4",getDataAdapter1.getImage4());
                i.putExtra("image5",getDataAdapter1.getImage5());
            //    i.putExtra("video1",getDataAdapter1.getVideo1());
                v.getContext().startActivity(i);
            }
        });

        holder.image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), ViewAddActivity.class);
                i.putExtra("id",getDataAdapter1.getTbl_add_post_data_id());
                i.putExtra("category",getDataAdapter1.getCategory());
                i.putExtra("mobile_no",getDataAdapter1.getMobile_no());
                i.putExtra("speciality",getDataAdapter1.getSpeciality());
                i.putExtra("variety",getDataAdapter1.getVariety());
                i.putExtra("age",getDataAdapter1.getAge());
                i.putExtra("vet",getDataAdapter1.getVet());
                i.putExtra("milkhistory",getDataAdapter1.getLastmilkhistory());
                i.putExtra("description",getDataAdapter1.getDescription());
                i.putExtra("image1",getDataAdapter1.getImage1());
                i.putExtra("image2",getDataAdapter1.getImage2());
                i.putExtra("image3",getDataAdapter1.getImage3());
             //   i.putExtra("image4",getDataAdapter1.getImage4());
                i.putExtra("image5",getDataAdapter1.getImage5());
            //    i.putExtra("video1",getDataAdapter1.getVideo1());
                v.getContext().startActivity(i);
            }
        });

        /*holder.image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), ViewAddActivity.class);
                i.putExtra("id",getDataAdapter1.getTbl_add_post_data_id());
                i.putExtra("category",getDataAdapter1.getCategory());
                i.putExtra("mobile_no",getDataAdapter1.getMobile_no());
                i.putExtra("speciality",getDataAdapter1.getSpeciality());
                i.putExtra("variety",getDataAdapter1.getVariety());
                i.putExtra("age",getDataAdapter1.getAge());
                i.putExtra("vet",getDataAdapter1.getVet());
                i.putExtra("milkhistory",getDataAdapter1.getLastmilkhistory());
                i.putExtra("description",getDataAdapter1.getDescription());
                i.putExtra("image1",getDataAdapter1.getImage1());
                i.putExtra("image2",getDataAdapter1.getImage2());
                i.putExtra("image3",getDataAdapter1.getImage3());
             //   i.putExtra("image4",getDataAdapter1.getImage4());
                i.putExtra("image5",getDataAdapter1.getImage5());
            //    i.putExtra("video1",getDataAdapter1.getVideo1());
                v.getContext().startActivity(i);
            }
        });
*/
       holder.image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), ViewAddActivity.class);
                i.putExtra("id",getDataAdapter1.getTbl_add_post_data_id());
                i.putExtra("category",getDataAdapter1.getCategory());
                i.putExtra("mobile_no",getDataAdapter1.getMobile_no());
                i.putExtra("speciality",getDataAdapter1.getSpeciality());
                i.putExtra("variety",getDataAdapter1.getVariety());
                i.putExtra("age",getDataAdapter1.getAge());
                i.putExtra("vet",getDataAdapter1.getVet());
                i.putExtra("milkhistory",getDataAdapter1.getLastmilkhistory());
                i.putExtra("description",getDataAdapter1.getDescription());
                i.putExtra("image1",getDataAdapter1.getImage1());
                i.putExtra("image2",getDataAdapter1.getImage2());
                i.putExtra("image3",getDataAdapter1.getImage3());
             //   i.putExtra("image4",getDataAdapter1.getImage4());
                i.putExtra("image5",getDataAdapter1.getImage5());
             //   i.putExtra("video1",getDataAdapter1.getVideo1());
                v.getContext().startActivity(i);
            }
        });



       holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ViewAddActivity.class);
                i.putExtra("id",getDataAdapter1.getTbl_add_post_data_id());
                i.putExtra("category",getDataAdapter1.getCategory());
                i.putExtra("mobile_no",getDataAdapter1.getMobile_no());
                i.putExtra("speciality",getDataAdapter1.getSpeciality());
                i.putExtra("variety",getDataAdapter1.getVariety());
                i.putExtra("age",getDataAdapter1.getAge());
                i.putExtra("vet",getDataAdapter1.getVet());
                i.putExtra("milkhistory",getDataAdapter1.getLastmilkhistory());
                i.putExtra("description",getDataAdapter1.getDescription());
                i.putExtra("image1",getDataAdapter1.getImage1());
                i.putExtra("image2",getDataAdapter1.getImage2());
                i.putExtra("image3",getDataAdapter1.getImage3());
            //    i.putExtra("image4",getDataAdapter1.getImage4());
                i.putExtra("image5",getDataAdapter1.getImage5());
             //   i.putExtra("video1",getDataAdapter1.getVideo1());
                v.getContext().startActivity(i);
            }
        });
        final AtomicBoolean playAnimation = new AtomicBoolean(true);

     /*   Picasso.with(context).load(getDataAdapter1.getImage1()).fetch(new Callback(){
            @Override
            public void onSuccess() {
                holder.image1.setAlpha(0f);
                Picasso.with(context).load(getDataAdapter1.getImage1()).into(holder.image1);
                holder.image1.animate().setDuration(300).alpha(1f).start();
            }

            @Override
            public void onError() {

            }
        });
        Picasso.get()
                .load(getDataAdapter1.getImage1())
                .into(holder.image1);
                */
        Picasso picasso1 = Picasso.with(context);
        picasso1.setIndicatorsEnabled(false);
        picasso1.load(getDataAdapter1.getImage1())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.image1, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(getDataAdapter1.getImage1())
                                .error(R.drawable.ic_baseline_image_24)
                                .into(holder.image1, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }
                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });

        Picasso picasso2 = Picasso.with(context);
        picasso2.setIndicatorsEnabled(false);
        picasso2.load(getDataAdapter1.getImage2())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.image2, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(getDataAdapter1.getImage2())
                                .error(R.drawable.ic_baseline_image_24)
                                .into(holder.image2, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }
                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });

        Picasso picasso3 = Picasso.with(context);
        picasso3.setIndicatorsEnabled(false);
        picasso3.load(getDataAdapter1.getImage3())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.image3, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(getDataAdapter1.getImage3())
                                .error(R.drawable.ic_baseline_image_24)
                                .into(holder.image3, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }
                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });

        Picasso picasso5 = Picasso.with(context);
        picasso5.setIndicatorsEnabled(false);
        picasso5.load(getDataAdapter1.getImage5())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.image5, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(getDataAdapter1.getImage5())
                                .error(R.drawable.ic_baseline_image_24)
                                .into(holder.image5, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }
                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });

      /*  Picasso.with(context)
                .load(getDataAdapter1.getImage1())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.image1, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(getDataAdapter1.getImage1())
                                .error(R.drawable.ic_baseline_image_24)
                                .into(holder.image1, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });

*/
       /*  Picasso.with(context)
                .load(getDataAdapter1.getImage2())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.image2, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(getDataAdapter1.getImage2())
                                .error(R.drawable.ic_baseline_image_24)
                                .into(holder.image2, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });
        Picasso.with(context)
                .load(getDataAdapter1.getImage3())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.image3, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(getDataAdapter1.getImage3())
                                .error(R.drawable.ic_baseline_image_24)
                                .into(holder.image3, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });
        Picasso.with(context)
                .load(getDataAdapter1.getImage5())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.image5, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(getDataAdapter1.getImage5())
                                .error(R.drawable.ic_baseline_image_24)
                                .into(holder.image5, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });
   /*     Picasso.get()
                .load(getDataAdapter1.getImage2())
                .into(holder.image2);
        Picasso.get()
                .load(getDataAdapter1.getImage3())
                .into(holder.image3);
        Picasso.get()
                .load(getDataAdapter1.getImage5())
                .into(holder.image5);
*/





       // Glide.with(context).load(getDataAdapter1.getImage1()).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(holder.image1);
      //  Glide.with(context).load(getDataAdapter1.getImage2()).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(holder.image2);
       // Glide.with(context).load(getDataAdapter1.getImage3()).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(holder.image3);
      //  Glide.with(context).load(getDataAdapter1.getImage4()).into(holder.image4);
      //  Glide.with(context).load(getDataAdapter1.getImage5()).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(holder.image5);
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
        TextView title;
        TextView description,tv_distance;

        ImageView image1;
        ImageView image2;
        ImageView image3;
      //  ImageView image4;
        ImageView image5;

        CardView card;
        HorizontalScrollView hscroll;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);

            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            image3 = (ImageView) itemView.findViewById(R.id.image3);
          //  image4 = (ImageView) itemView.findViewById(R.id.image4);
            image5 = (ImageView) itemView.findViewById(R.id.image5);
            card = (CardView) itemView.findViewById(R.id.card);
        }

        @Override
        public void onClick(View view) {
            Log.d("Onclick", "onClick " + getPosition() + " ");
        }
    }

}
