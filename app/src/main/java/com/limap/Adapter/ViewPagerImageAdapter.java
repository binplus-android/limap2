package com.limap.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.limap.Activity.PhotoActivity;
import com.limap.Activity.VideoActivity;
import com.limap.Activity.ViewAddActivity;
import com.limap.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;


/**
 * Created by admin on 1/24/2016.
 */
public class ViewPagerImageAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private boolean[] isdone;
    boolean btnclick=false;
    String image1,image2,image3,image4,image5,video1;
    //int imageids[] = {R.drawable.cow1, R.drawable.cow2, R.drawable.cow3, R.drawable.cow4, R.drawable.cow5, R.drawable.cow6};

   // public ViewPagerImageAdapter(ViewAddActivity context,String img1,String img2,String img3,String img4,String img5,String vid1) {
    public ViewPagerImageAdapter(Context context,String img1,String img2,String img3,String img5,boolean btnclick) {

        this.context = context;
        image1 =   img1;
        image2 =   img2;
        image3 =   img3;
        //image4 =   img4;
        image5 =   img5;
        this.btnclick=btnclick;
        //video1   =   vid1;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       // isdone = new boolean[6];
        isdone = new boolean[4];
    }

    @Override
    public int getCount() {
        // Returns the number of tabs
       // return 6;
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        View itemView;
        if(position==4) {

            itemView = layoutInflater.inflate(R.layout.adapter_video, container, false);
            final ImageView imageView = itemView.findViewById(R.id.play);
            imageView.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent target = new Intent(context, VideoActivity.class);

                    target.putExtra("video_url", video1);

                    context.startActivity(target);
                }
            });

        }else {

            itemView = layoutInflater.inflate(R.layout.adapter_photo, container, false);

        }

        try {

            final ImageView imageView = itemView.findViewById(R.id.add);
            final ProgressBar pbar = itemView.findViewById(R.id.pbar);
                 pbar.setVisibility(View.VISIBLE);
//            imageView.setImageResource(imageids[position]);
            Log.d("video_url",image1);

             if(position ==0) {

              //  Glide.with(context).load(image1).into(imageView);
                 Picasso.with(context)
                         .load(image1)
                         .into(imageView, new Callback() {
                             @Override
                             public void onSuccess() {
                                 pbar.setVisibility(View.GONE);
                             }

                             @Override
                             public void onError() {
                                 pbar.setVisibility(View.GONE);
                             }
                         });
                 imageView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {

                         if(btnclick){
                             goToPhotoPage();
                         }
//                         Intent target = new Intent(context, PhotoActivity.class);
//
//                         target.putExtra("image1", image1);
//                         target.putExtra("image2", image2);
//                         target.putExtra("image3", image3);
//                         target.putExtra("image5", image5);

//                         context.startActivity(target);
                     }
                 });

             }
            else if(position == 1) {
              //  Glide.with(context).load(image2).into(imageView);
                 Picasso.with(context)
                         .load(image2)
                         .into(imageView, new Callback() {
                             @Override
                             public void onSuccess() {
                                 pbar.setVisibility(View.GONE);
                             }

                             @Override
                             public void onError() {
                                 pbar.setVisibility(View.GONE);
                             }
                         });
                 imageView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {

                         if(btnclick){
                             goToPhotoPage();
                         }
//                         Intent target = new Intent(context, PhotoActivity.class);
//
//                         target.putExtra("image1", image1);
//                         target.putExtra("image2", image2);
//                         target.putExtra("image3", image3);
//                         target.putExtra("image5", image5);
//
//                         context.startActivity(target);
                     }
                 });
            }
            else if(position == 2) {
              //  Glide.with(context).load(image3).into(imageView);
                 Picasso.with(context)
                         .load(image3)
                         .into(imageView, new Callback() {
                             @Override
                             public void onSuccess() {
                                 pbar.setVisibility(View.GONE);
                             }

                             @Override
                             public void onError() {
                                 pbar.setVisibility(View.GONE);
                             }
                         });
                 imageView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {

                         if(btnclick){
                             goToPhotoPage();
                         }
//                         Intent target = new Intent(context, PhotoActivity.class);
//
//                         target.putExtra("image1", image1);
//                         target.putExtra("image2", image2);
//                         target.putExtra("image3", image3);
//                         target.putExtra("image5", image5);
//
//                         context.startActivity(target);
                     }
                 });
            }
        else if(position == 3) {
             //   Glide.with(context).load(image5).into(imageView);
                 Picasso.with(context)
                         .load(image5)
                         .into(imageView, new Callback() {
                             @Override
                             public void onSuccess() {
                                 pbar.setVisibility(View.GONE);
                             }

                             @Override
                             public void onError() {
                                 pbar.setVisibility(View.GONE);
                             }
                         });
                 imageView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {

                         if(btnclick){
                          goToPhotoPage();
                         }
//                         Intent target = new Intent(context, PhotoActivity.class);
//                         target.putExtra("image1", image1);
//                         target.putExtra("image2", image2);
//                         target.putExtra("image3", image3);
//                         target.putExtra("image5", image5);
//
//                         context.startActivity(target);
                     }
                 });
            }
          /*  else if(position == 3) {
                Glide.with(context).load(image4).into(imageView);
                 imageView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {

                         Intent target = new Intent(context, PhotoActivity.class);

                         target.putExtra("photo_url", image4);

                         context.startActivity(target);
                     }
                 });
            }
            else if(position == 5) {
                Glide.with(context).load(video1).into(imageView);
            }*/


//            String filepath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + "ADDS" + File.separator;
//
//            File file = new File(filepath+(position+1)+".jpg");
//
//                if(file.exists()) {
//
//                    Picasso.get().setLoggingEnabled(true);
//
//                    Picasso.get()
//                            .load(file)
//                            .into(imageView);
//
//
//
//                    isdone[position] = true;
//
//                }


        }catch(Exception e){

            Log.e("AddAdapter", ""+e);
        }


        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }

    private void goToPhotoPage(){
        Intent target = new Intent(context, PhotoActivity.class);
        target.putExtra("image1", image1);
        target.putExtra("image2", image2);
        target.putExtra("image3", image3);
        target.putExtra("image5", image5);

        context.startActivity(target);
    }


}
