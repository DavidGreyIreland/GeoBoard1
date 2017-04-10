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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by david on 29/03/2017.
 */

public class UserMessageListActivity extends AppCompatActivity
{

    private RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference databaseRef;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_message_list);

        // RecyclerView
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_message_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getInstance().getReferenceFromUrl("https://geoboard1-33349.firebaseio.com/Messages/");

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<ModelClass, MessageListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelClass, MessageListViewHolder>(
                ModelClass.class,
                R.layout.design_row,
                MessageListViewHolder.class,
                databaseRef)
        {
            @Override
            protected void populateViewHolder(MessageListViewHolder viewHolder, ModelClass model, int position)
            {
                firebaseAuth = firebaseAuth.getInstance();
                FirebaseUser userFirebase = firebaseAuth.getCurrentUser();
                String firebaseCurrentUser = userFirebase.getEmail();
                String user = model.getUser();

                if(user.equals(firebaseCurrentUser))
                {
                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setSubject(model.getSubject());
                    viewHolder.setLocation(model.getLocation());
                    //MessageListViewHolder.removeAt(position);
                    //notifyItemRemoved(position);
                }
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    // TODO update recyclerview to show only relevant user data
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

       /* public static void removeAt(int position)
        {
            myDataset.remove(position);
        }*/


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

/*        @Override
        public Filter getFilter()
        {
            return null;
        }*/
    }
}
