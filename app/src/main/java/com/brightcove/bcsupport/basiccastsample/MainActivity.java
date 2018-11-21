package com.brightcove.bcsupport.basiccastsample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;

import com.brightcove.cast.GoogleCastComponent;
import com.brightcove.player.edge.Catalog;
import com.brightcove.player.edge.VideoListener;
import com.brightcove.player.event.EventEmitter;
import com.brightcove.player.event.EventEmitterImpl;
import com.brightcove.player.model.Video;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements VideoListAdapter.ItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView videoListView = findViewById(R.id.video_list_view);
        final VideoListAdapter videoListAdapter = new VideoListAdapter(this);
        videoListView.setAdapter(videoListAdapter);
        EventEmitter eventEmitter = new EventEmitterImpl();
        Catalog catalog = new Catalog(eventEmitter, getString(R.string.account), getString(R.string.policy));

        catalog.findVideoByID(getString(R.string.videoId), new VideoListener() {
            @Override
            public void onVideo(Video video) {
                video.getProperties().remove(Video.Fields.CAPTION_SOURCES);
                List<Video> videoList = new ArrayList<Video>();
                videoList.add(video);
                videoListAdapter.setVideoList(videoList);
            }
        });
    }

    @Override
    public void itemClicked(View view, Video video, int position) {
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra(VideoPlayerActivity.INTENT_EXTRA_VIDEO, (Parcelable) video);

        Pair<View, String> imagePair = Pair
                .create(view, getString(R.string.transition_image));
        //noinspection unchecked
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, imagePair);
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        GoogleCastComponent.setUpMediaRouteButton(this, menu);
        return true;
    }
}

