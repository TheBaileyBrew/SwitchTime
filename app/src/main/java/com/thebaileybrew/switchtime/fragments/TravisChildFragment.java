package com.thebaileybrew.switchtime.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thebaileybrew.switchtime.R;
import com.thebaileybrew.switchtime.adapter.Task;
import com.thebaileybrew.switchtime.adapter.TaskRecyclerAdapter;
import com.thebaileybrew.switchtime.ui.SwitchTime;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TravisChildFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = TravisChildFragment.class.getSimpleName();


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mChildDatabaseReference;
    private String currentTime;
    private String usedTime;
    private DatabaseReference mTaskDatabaseReference;
    private List<Task> listTasks = new ArrayList<>();

    private TextView childTextView;
    private TextView minuteTextView;
    private Button useMinutesButton;
    private EditText editTextMinutes;

    private RecyclerView recyclerView;
    private TaskRecyclerAdapter taskRecyclerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mChildDatabaseReference = mFirebaseDatabase.getReference("Child").child("Travis");
        mChildDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //TODO: Update Minute Count
                currentTime = String.valueOf(dataSnapshot.getValue());
                Log.e(TAG, "onDataChange: " + currentTime);
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mTaskDatabaseReference = mFirebaseDatabase.getReference("TravisTasks");
        mTaskDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Task newTask = dataSnapshot.getValue(Task.class);
                Log.e(TAG, "onDataChange: description: " + newTask.getTaskDescription());
                listTasks.add(newTask);
                taskRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateUI() {
        String timeField = currentTime + " minutes";
        minuteTextView.setText(timeField);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_child, container, false);
        TextView nameTextView = inflatedView.findViewById(R.id.child_name);
        nameTextView.setText(getString(R.string.travis_child_name));
        minuteTextView = inflatedView.findViewById(R.id.earned_time);
        childTextView = inflatedView.findViewById(R.id.child_name);
        editTextMinutes = inflatedView.findViewById(R.id.use_minutes_edittext);
        useMinutesButton = inflatedView.findViewById(R.id.use_minutes_button);
        useMinutesButton.setOnClickListener(this);
        recyclerView = inflatedView.findViewById(R.id.task_recycler);
        taskRecyclerAdapter = new TaskRecyclerAdapter(SwitchTime.getContext(), listTasks, new TaskRecyclerAdapter.TaskRecyclerClickHandler() {
            @Override
            public void onClick(View view, Task task) {
                openDialogView(task);
            }
        });
        recyclerView.setAdapter(taskRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SwitchTime.getContext(), RecyclerView.VERTICAL, false));


        return inflatedView;
    }

    private void openDialogView(final Task task) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage(task.getTaskExtendedDetails());
        dialogBuilder.setPositiveButton("COMPLETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SwitchTime.getContext(), task.taskDescription + " has been completed", Toast.LENGTH_SHORT).show();
                int newTime = Integer.parseInt(task.getTaskMinuteValue());
                newTime = newTime + Integer.parseInt(currentTime);
                mChildDatabaseReference.setValue(String.valueOf(newTime));
            }
        });
        dialogBuilder.setNegativeButton("NOT COMPLETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SwitchTime.getContext(), task.getTaskDescription() + " has not been completed", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId() ) {
            case R.id.use_minutes_button:
                if (!TextUtils.isEmpty(editTextMinutes.getText())) {
                    usedTime = String.valueOf(editTextMinutes.getText());
                    int newTime = Integer.parseInt(currentTime) - Integer.parseInt(usedTime);
                    mChildDatabaseReference.setValue(String.valueOf(newTime));
                    Toast.makeText(SwitchTime.getContext(), "Travis has used " + usedTime + " minutes", Toast.LENGTH_SHORT).show();
                }
                editTextMinutes.setText(null);
                break;
        }
    }
}
