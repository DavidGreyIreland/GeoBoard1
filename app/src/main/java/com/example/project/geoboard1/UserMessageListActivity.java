package com.example.project.geoboard1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by david on 29/03/2017.
 */

public class UserMessageListActivity extends AppCompatActivity
{

    private RecyclerView mBlogList;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_message_list);

        // RecyclerView
        mBlogList = (RecyclerView)findViewById(R.id.recyclerview_message_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));
        //database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-cards-c9925.firebaseio.com/");
        database = FirebaseDatabase.getInstance();
        myRef = database.getInstance().getReferenceFromUrl("https://geoboard1-33349.firebaseio.com/Messages/");

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<ModelClass, MessageListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelClass, MessageListViewHolder>(
                ModelClass.class,
                R.layout.design_row,
                MessageListViewHolder.class,
                myRef)
        {
            @Override
            protected void populateViewHolder(MessageListViewHolder viewHolder, ModelClass model, int position)
            {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setSubject(model.getSubject());
                viewHolder.setLocation(model.getLocation());
            }
        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }

    // viewholder for recyclerView
    public static class MessageListViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public MessageListViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
            itemView.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.androidsquad.space/"));
                    Intent browserChooserIntent = Intent.createChooser(browserIntent, "Choose browser of your choice");
                    v.getContext().startActivity(browserChooserIntent);
                }
            });
        }

        public void setTitle(String title)
        {
            TextView post_title = (TextView)mView.findViewById(R.id.title);
            post_title.setText("Title: " + title);
        }

        public void setSubject(String subject)
        {
            TextView post_title = (TextView)mView.findViewById(R.id.subject);
            post_title.setText("Subject: " + subject);
        }

        public void setLocation(String location)
        {
            TextView post_title = (TextView)mView.findViewById(R.id.location);
            post_title.setText("Location:         " + location);
        }
    }
}
