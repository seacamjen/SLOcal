package com.epicodus.slofamfun.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.epicodus.slofamfun.R;
import com.epicodus.slofamfun.adapters.FirebaseActivityViewHolder;
import com.epicodus.slofamfun.models.LocalActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LocalUiActivity extends AppCompatActivity {
//    @Bind(R.id.localList) ListView mLocalList;
//    @Bind(R.id.searchedTextView) TextView mSearchedTextView;
    @Bind(R.id.localRecyclerView) RecyclerView mLocalRecyclerView;

//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mSearchedAcitivityReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter;

    protected void onCreate(Bundle savedInstanceState) {

        mSearchedAcitivityReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Avila Beach");

        mSearchedAcitivityReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot activitySnapshot : dataSnapshot.getChildren()) {
                    String activity = activitySnapshot.getValue().toString();
                    Log.d("Activity updated", "Activity: " + activity);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        ButterKnife.bind(this);

        setUpFirebaseAdapter();

//        Intent intent = getIntent();
//        String search = intent.getStringExtra("search");
//        mSearchedTextView.setText("Here are the results filtered by: " + search);

//        LocalActivitiesArrayAdapter adapter = new LocalActivitiesArrayAdapter(this, android.R.layout.simple_list_item_1, activities, cities);
//        mLocalList.setAdapter(adapter);
    }

    private void setUpFirebaseAdapter(){
        mFirebaseAdapter = new FirebaseRecyclerAdapter<LocalActivity, FirebaseActivityViewHolder>(LocalActivity.class, R.layout.local_activity_list, FirebaseActivityViewHolder.class, mSearchedAcitivityReference) {
            @Override
            protected void populateViewHolder(FirebaseActivityViewHolder viewHolder, LocalActivity model, int position) {
                viewHolder.bindActivity(model);
            }
        };
        mLocalRecyclerView.setHasFixedSize(true);
        mLocalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLocalRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(LocalUiActivity.this, AddActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}