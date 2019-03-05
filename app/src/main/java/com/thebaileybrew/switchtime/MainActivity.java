package com.thebaileybrew.switchtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.transition.Slide;
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thebaileybrew.switchtime.adapter.Task;
import com.thebaileybrew.switchtime.adapter.TaskList;
import com.thebaileybrew.switchtime.fragments.BrycenChildFragment;
import com.thebaileybrew.switchtime.fragments.TravisChildFragment;

import java.util.ArrayList;
import java.util.List;

import static com.thebaileybrew.switchtime.utils.ConstantTribulations.BRYCEN;
import static com.thebaileybrew.switchtime.utils.ConstantTribulations.SELECTED_CHILD_PREF;
import static com.thebaileybrew.switchtime.utils.ConstantTribulations.TRAVIS;

public class MainActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mChildDatabaseReference;
    private DatabaseReference mTaskDatabaseReference;
    private DatabaseReference mTravisTaskDatabaseReference;
    private TabLayout.Tab tabBrycen;
    private TabLayout.Tab tabTravis;

    private SharedPreferences sharedPrefs;
    private String selectedChild;
    private List<Task> taskList = new ArrayList<>();
    private List<Task> travisList = new ArrayList<>();
    private Fragment loadedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabBrycen = tabLayout.getTabAt(0);
        tabTravis = tabLayout.getTabAt(1);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        selectedChild = sharedPrefs.getString(SELECTED_CHILD_PREF,"Brycen");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mChildDatabaseReference = mFirebaseDatabase.getReference("Child");
        mChildDatabaseReference.child("Brycen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String brycenTime = dataSnapshot.getValue(String.class);
                String totalString = "Brycen " + "- " + brycenTime;
                tabBrycen.setText(totalString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mChildDatabaseReference.child("Travis").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String travisTime = dataSnapshot.getValue(String.class);
                String totalString = "Travis " + "- " + travisTime;
                tabTravis.setText(totalString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mTaskDatabaseReference = mFirebaseDatabase.getReference("Tasks");
        mTravisTaskDatabaseReference = mFirebaseDatabase.getReference("TravisTasks");
        getFirebaseDetails();
        tabLayout.addOnTabSelectedListener(this);

        loadFragment(selectedChild);


    }

    private void loadFragment(String selectedChild) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch(selectedChild) {
            case BRYCEN:
                if (loadedFragment == null) {
                    loadedFragment = new BrycenChildFragment();
                    loadedFragment.setExitTransition(new Slide(Gravity.TOP));
                    fragmentTransaction.add(R.id.frame_for_fragments, loadedFragment).commit();
                } else {
                    if (!(fragmentManager.findFragmentById(R.id.frame_for_fragments) instanceof BrycenChildFragment)) {
                        loadedFragment = new BrycenChildFragment();
                        loadedFragment.setEnterTransition(new Slide(Gravity.TOP));
                        fragmentTransaction.replace(R.id.frame_for_fragments, loadedFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
                break;
            case TRAVIS:
                if (loadedFragment == null) {
                    loadedFragment = new TravisChildFragment();
                    loadedFragment.setExitTransition(new Slide(Gravity.TOP));
                    fragmentTransaction.add(R.id.frame_for_fragments, loadedFragment).commit();
                } else {
                    if (!(fragmentManager.findFragmentById(R.id.frame_for_fragments) instanceof TravisChildFragment)) {
                        loadedFragment = new TravisChildFragment();
                        loadedFragment.setEnterTransition(new Slide(Gravity.TOP));
                        fragmentTransaction.replace(R.id.frame_for_fragments, loadedFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
                break;


        }
    }

    //Reads the Database for changes/modifications
    private void getFirebaseDetails() {
        //TODO: Check if empty - YES: Load default values, NO: Load DB values
        //mChildDatabaseReference.child("Brycen").setValue("0");
        //mChildDatabaseReference.child("Travis").setValue("0");
        taskList = TaskList.getTasks();
        for(int t = 0; t < taskList.size(); t++) {
            final Task addTask = taskList.get(t);
            mTaskDatabaseReference.child(addTask.getTaskDescription()).setValue(addTask);
        }
        travisList = TaskList.getTravisTasks();
        for (int l = 0; l < travisList.size(); l++) {
            final Task newTask = travisList.get(l);
            mTravisTaskDatabaseReference.child(newTask.getTaskDescription()).setValue(newTask);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String tabSelected = String.valueOf(tab.getText());
        String child = tabSelected.substring(0,6);
        loadFragment(child);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Toast.makeText(this, String.valueOf(tab.getText()) + " already loaded", Toast.LENGTH_SHORT).show();

    }
}
