package com.hcl.tiktok;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.hcl.tiktok.json.ApiService;
import com.hcl.tiktok.json.VideoResponse;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.hcl.tiktok.LikeAnination;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beiyou.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService.getVideos().enqueue(new Callback<List<VideoResponse>>() {
            @Override
            public void onResponse(Call<List<VideoResponse>> call, Response<List<VideoResponse>> response) {
                if (response.body() != null) {
                    List<VideoResponse> videos = response.body();
                    viewPager2.setAdapter(new viewAdaper(videos));
                }
            }

            @Override
            public void onFailure(Call<List<VideoResponse>> call, Throwable t) {
                Log.d("retrofit", t.getMessage());
            }

        });

    }

    public class viewAdaper extends RecyclerView.Adapter<viewAdaper.CardViewHolder> {
        List<VideoResponse> data;
        long mLastTime = 0;
        long mCurTime = 0;

        public viewAdaper(List<VideoResponse> d) {
            data = d;
        }

        @NonNull
        @Override
        public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.play_view, parent, false));
        }

        @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {

            Log.d("HCl", data.get(position).getNickname());

            Uri a = Uri.parse(data.get(position).getFeedurl());

            holder.videoView.setVideoPath(data.get(position).getFeedurl());
            //循环播放
            holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    holder.videoView.start();
                }
            });

            GestureDetector gestureDetector = new GestureDetector(holder.itemView.getContext(), new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    if (holder.videoView.isPlaying()) {
                        holder.videoView.pause();
                        holder.PlayV.setVisibility(View.VISIBLE);
                    } else {
                        holder.videoView.start();
                        holder.PlayV.setVisibility(View.INVISIBLE);
                    }
                    return true;
                }

                @Override
                public boolean onDown(MotionEvent e) {
                   return true;
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    int likeViewSize = 300;
                    ConstraintLayout ct = holder.constraintLayoutl;
                    ImageView imageView = new ImageView(ct.getContext());
                    imageView.setImageResource(R.drawable.ic_like);
                    ct.addView(imageView);
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(likeViewSize,likeViewSize);
                    layoutParams.leftMargin = (int)e.getX() - likeViewSize/2;
                    layoutParams.topMargin = (int)e.getY() - likeViewSize/2;
                    layoutParams.leftToLeft = R.id.consLayout;
                    layoutParams.topToTop = R.id.consLayout;
                    imageView.setLayoutParams(layoutParams);
                    Animation animation = LikeAnination.playAnim();
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ct.post(new Runnable() {
                                @Override
                                public void run() {
                                    ct.removeView(imageView);
                                }
                            });
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    imageView.startAnimation(animation);
                    return true;
                }
            });

            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                     return gestureDetector.onTouchEvent(event);
                }
            });

            holder.desc.setText(data.get(position).getDescription());
            holder.like.setText(Integer.toString(data.get(position).getLikecount()));
            holder.name.setText("@" + data.get(position).getNickname());

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public void onViewAttachedToWindow(@NonNull CardViewHolder holder) {
            holder.videoView.start();
            super.onViewAttachedToWindow(holder);
        }

        class CardViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView like;
            TextView desc;
            VideoView videoView;
            ImageView PlayV;
            ConstraintLayout constraintLayoutl;

            CardViewHolder(@NonNull View itemView) {
                super(itemView);
                videoView = itemView.findViewById(R.id.videoView);
                name = itemView.findViewById(R.id.nickname);
                like = itemView.findViewById(R.id.likeCount);
                desc = itemView.findViewById(R.id.desc);
                PlayV = itemView.findViewById(R.id.playView);
                constraintLayoutl = itemView.findViewById(R.id.consLayout);
            }
        }
    }
}
