package com.thebaileybrew.switchtime;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thebaileybrew.switchtime.adapter.Task;
import com.thebaileybrew.switchtime.objects.SliderLayoutManager;
import com.thebaileybrew.switchtime.objects.SliderViewHolder;
import com.thebaileybrew.switchtime.ui.SwitchTime;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.RecyclerView;

import static com.thebaileybrew.switchtime.utils.ConstantTribulations.BRYCEN;
import static com.thebaileybrew.switchtime.utils.ConstantTribulations.SELECTED_CHILD_PREF;
import static com.thebaileybrew.switchtime.utils.ConstantTribulations.TASK_ITEM_REFERENCE;
import static com.thebaileybrew.switchtime.utils.ConstantTribulations.TRAVIS;

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = AddTaskActivity.class.getSimpleName();

    private RecyclerView tensScroller;
    private SliderLayoutManager tensScrollerManager;
    private SliderViewHolder tensAdapter;
    private String tensValue = "";
    private List<String> tensList = new ArrayList<>();


    private RecyclerView onesScroller;
    private SliderLayoutManager onesScrollerManager;
    private SliderViewHolder onesAdapter;
    private String onesValue = "";
    private List<String> onesList = new ArrayList<>();


    private TextInputEditText shortDescriptionEditText;
    private TextInputEditText longDescriptionEditText;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mTaskDatabaseReference;
    private DatabaseReference mTravisTaskDatabaseReference;

    private Button submitTaskButton;

    private CheckBox brycenCheckBox;
    private CheckBox travisCheckBox;

    private Task currentTask;
    private String selectedChild;
    private Boolean loadedData = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        setupFirebaseReferences();
        createLists();

        initViews();
        setupTimeScrollRecyclers();

        Intent intent = getIntent();
        if (intent != null) {
            loadedData = true;
            currentTask = intent.getParcelableExtra(TASK_ITEM_REFERENCE);
            selectedChild = intent.getStringExtra(SELECTED_CHILD_PREF);
            shortDescriptionEditText.setText(currentTask.getTaskDescription());
            longDescriptionEditText.setText(currentTask.getTaskExtendedDetails());
            if (selectedChild.equals(BRYCEN)) {
                brycenCheckBox.setChecked(true);
            }
            if (selectedChild.equals(TRAVIS)) {
                travisCheckBox.setChecked(true);
            }
            String time = currentTask.getTaskMinuteValue();
            tensValue = time.substring(0,1);
            Log.e(TAG, "onCreate: tens= " + tensValue );
            onesValue = time.substring(1,2);
            Log.e(TAG, "onCreate: singles= " + onesValue );
            onesScroller.scrollToPosition(Integer.parseInt(onesValue));
            tensScroller.scrollToPosition(Integer.parseInt(tensValue));
        }

    }

    private void createLists() {
        tensList.add("0");
        tensList.add("1");
        tensList.add("2");
        tensList.add("3");
        tensList.add("4");
        tensList.add("5");
        tensList.add("6");
        onesList.add("0");
        onesList.add("1");
        onesList.add("2");
        onesList.add("3");
        onesList.add("4");
        onesList.add("5");
        onesList.add("6");
        onesList.add("7");
        onesList.add("8");
        onesList.add("9");
    }

    private void setupFirebaseReferences() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mTaskDatabaseReference = mFirebaseDatabase.getReference("Tasks");
        mTravisTaskDatabaseReference = mFirebaseDatabase.getReference("TravisTasks");
    }


    private void initViews() {
        //Recyclers
        onesScroller = findViewById(R.id.minutes_digit_time_scroller);
        tensScroller = findViewById(R.id.tens_digit_time_scroller);
        //EditTexts
        shortDescriptionEditText = findViewById(R.id.short_description_entry);
        longDescriptionEditText = findViewById(R.id.long_description_entry);
        //Button
        submitTaskButton = findViewById(R.id.submit_task_button);
        submitTaskButton.setOnClickListener(this);
        //Checkboxes
        brycenCheckBox = findViewById(R.id.brycen_checkbox);
        travisCheckBox = findViewById(R.id.travis_checkbox);
    }

    private void setupTimeScrollRecyclers() {
        tensScrollerManager = new SliderLayoutManager(SwitchTime.getContext(), new SliderLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                tensValue = String.valueOf(position);
                if (tensValue.equals("6")) {
                    onesScroller.scrollToPosition(0);
                }

            }
        });
        tensAdapter = new SliderViewHolder(SwitchTime.getContext(), tensList, new SliderViewHolder.SliderClickHandler() {
            @Override
            public void onClick(View view, String time) {
                tensValue = time;
                if (tensValue.equals("6")) {
                    onesScroller.scrollToPosition(0);
                }
            }
        });
        tensScroller.setLayoutManager(tensScrollerManager);
        tensScroller.setAdapter(tensAdapter);

        onesScrollerManager = new SliderLayoutManager(SwitchTime.getContext(), new SliderLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                onesValue = String.valueOf(position);
            }
        });
        onesAdapter = new SliderViewHolder(SwitchTime.getContext(), onesList, new SliderViewHolder.SliderClickHandler() {
            @Override
            public void onClick(View view, String time) {
                onesValue = time;
            }
        });
        onesScroller.setLayoutManager(onesScrollerManager);
        onesScroller.setAdapter(onesAdapter);


    }

    @Override
    public void onClick(View v) {
        if (tensValue == null) {
            tensValue = "0";
        }
        if (onesValue == null) {
            onesValue = "0";
        }
        String totalTime = tensValue + onesValue;


        switch (v.getId()) {
            case R.id.submit_task_button:
                if(TextUtils.isEmpty(shortDescriptionEditText.getText())
                        || shortDescriptionEditText.getText().toString().equals("")) {
                    Toast.makeText(this, "Must have a short description",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(longDescriptionEditText.getText())
                        || longDescriptionEditText.getText().toString().equals("")) {
                    Toast.makeText(this, "Must have additional details",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //Create new task and add to Firebase
                Task addTask = new Task(totalTime, shortDescriptionEditText.getText().toString(),
                        longDescriptionEditText.getText().toString());
                if (brycenCheckBox.isChecked()) {
                    mTaskDatabaseReference.child(addTask.getTaskDescription()).setValue(addTask);
                    brycenCheckBox.setChecked(false);
                }
                if (travisCheckBox.isChecked()) {
                    mTravisTaskDatabaseReference.child(addTask.getTaskDescription()).setValue(addTask);
                    travisCheckBox.setChecked(false);
                }

                if (loadedData) {
                    NavUtils.navigateUpFromSameTask(this);
                } else {
                    onesScroller.scrollToPosition(0);
                    tensScroller.scrollToPosition(0);
                    longDescriptionEditText.setText("");
                    longDescriptionEditText.clearFocus();
                    shortDescriptionEditText.setText("");
                    shortDescriptionEditText.clearFocus();
                }

                break;

        }
    }
}
