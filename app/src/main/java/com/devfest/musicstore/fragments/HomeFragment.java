package com.devfest.musicstore.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devfest.musicstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import viewadapters.HomeScreenSectionViewAdapter;
import model.Screen;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView mSectionRecyclerView;
    LinearLayoutManager mLinearLayoutManager;
    HomeScreenSectionViewAdapter mSectionViewAdapter;
    Screen mScreen;

    FirebaseAuth mFireBaseAuth;
    DatabaseReference mDatabase;
    boolean initialLoad = true;

    public HomeFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        firebase(v);

        return v;
    }

    void renderView(View v) {
        mSectionRecyclerView = (RecyclerView) v.findViewById(R.id.sectionRecyclerView);

        mSectionViewAdapter = new HomeScreenSectionViewAdapter(this.getActivity(), mScreen.getSections(), new HomeScreenSectionViewAdapter.SectionViewListener() {
            @Override
            public void onItemClickListener(String FilePath, int position, HomeScreenSectionViewAdapter.SectionViewHolder vh) {

            }

        });
        mLinearLayoutManager = new LinearLayoutManager(this.getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSectionRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSectionRecyclerView.setAdapter(mSectionViewAdapter);
    }

    void firebase(final View v) {
        mFireBaseAuth = FirebaseAuth.getInstance();
        mFireBaseAuth.signInAnonymously();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("data").getParent().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (initialLoad) {

                    // TODO initial Fetch

                    // now we get the Whole Screen and will pass the screen to Recycler Views

                    for(DataSnapshot ds : dataSnapshot.getChildren()){


                          if(ds.hasChild("title")){
                              if(ds.child("title").getValue().toString().equals("Home")){
                                  mScreen = new Screen(ds);
                                  renderView(v);
                              }
                          }


                    }

                    initialLoad = false;
                } else {
                    // TODO onChildAdded

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // TODO play with data when onChildChanged
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // TODO play with data when childElements were removed
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // TODO play with data when child element is moved
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
