package com.example.planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.*;

public class activity_venueChooser extends AppCompatActivity  {
    FirebaseDatabase fireData;
    DatabaseReference dataRefrence;
    ImageButton previousButton;
    ImageView searchButton;
    EditText searchBar;
    RecyclerView recyclerListView;
    activity_list_layout.RecyclerViewClickListener recyclerViewClickListener;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_chooser);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        fireData = FirebaseDatabase.getInstance();
        dataRefrence = FirebaseDatabase.getInstance().getReference("venues");
        searchBar = findViewById(R.id.search_bar);
        searchButton = findViewById(R.id.search_button12);
        previousButton = findViewById(R.id.previous_button);
        recyclerListView = findViewById(R.id.recyclerListView);
        recyclerListView.setHasFixedSize(true);
        recyclerListView.setLayoutManager(new LinearLayoutManager(this));
        //cardView = findViewById(R.id.card_view_list);
        //System.out.println("1");



        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(activity_venueChooser.this, planning.class));
            }
        });
       // System.out.println("2");
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("2");
                String searchText = searchBar.getText().toString().trim();
                if(TextUtils.isEmpty(searchText))
                {
                    searchBar.setError("Enter the Name to search");
                    return;
                }
                firebaseUserSearch(searchText);
            }
        });
        System.out.println("3");



    }





    private void firebaseUserSearch(String searchText) {
        Query firebaseSearchQuery = dataRefrence.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerAdapter<venueHelper,userviewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<venueHelper, userviewholder>(
                venueHelper.class,
                R.layout.list_layout,
                userviewholder.class,
                firebaseSearchQuery

        ) {
            @Override
            protected void populateViewHolder(userviewholder userviewholder, venueHelper venueHelper, int i) {
                userviewholder.setDetails(getApplicationContext(),venueHelper.getImage(),venueHelper.getName(),venueHelper.getLocation());

                /*cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(),VenueImageSlider.class));
                    }
                });*/
            }
        };

        recyclerListView.setAdapter(firebaseRecyclerAdapter);
    }



    public static class userviewholder extends RecyclerView.ViewHolder  {
        View mView;


        public userviewholder(@NonNull View itemView ) {
            super(itemView);
            mView = itemView;


        }

        public void setDetails(Context ctx, String image, String title, String location) {
            ImageButton venueImage = mView.findViewById(R.id.display_image);
            TextView venueName = mView.findViewById(R.id.title_event);
            TextView venuePrice = mView.findViewById(R.id.price);
            venueName.setText(title);
            venuePrice.setText(location);
            Glide.with(ctx).load(image).into(venueImage);



        }


    }


}