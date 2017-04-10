package com.example.project.geoboard1;

import android.support.v7.app.AppCompatActivity;

public class ViewGeoBoard extends AppCompatActivity
{
/*    // non recycler code DONT DELETE
    private static final String TAG = "";
    TextView tv;
    private DatabaseReference database;
    MessageDetails m;
    FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;
    private Adapter adapter;
    List<ListItem> data;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_geo_board);

        recyclerView = (RecyclerView)findViewById(R.id.recycleList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        // gets the list data for the recyclerView
        adapter = new Adapter(Data.getListData(), this);
        recyclerView.setAdapter(adapter);




        // non recycler code DONT DELETE
        //tv = (TextView)findViewById(R.id.textView);
        //m = (MessageDetails)getApplicationContext();

        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://geoboard1-33349.firebaseio.com/Messages");


        database.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                firebaseAuth = firebaseAuth.getInstance();
                FirebaseUser userFirebase = firebaseAuth.getCurrentUser();
                String output = "";

                data = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String firebaseCurrentUser = userFirebase.getEmail();
                    String user = snapshot.child("User").getValue(String.class);
                    int counter = 0;
                    // allows for only currently logged in users messages to be shown
                    if(user.equals(firebaseCurrentUser))
                    {
                        String title = snapshot.child("Title").getValue(String.class);
                        String location = snapshot.child("Location").getValue(String.class);

                        ListItem item = new ListItem();
                        item.setMessage(title);
                        data.add(item);
                        output += "Title: " + title + "\nLocation: " + location + "\n\n";
                    }
                }
                //tv.setText(output);
            }

            public List<ListItem> messageData()
            {
                return data;
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        });
    }*/
}
