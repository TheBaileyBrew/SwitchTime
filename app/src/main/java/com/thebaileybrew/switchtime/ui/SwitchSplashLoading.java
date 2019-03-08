package com.thebaileybrew.switchtime.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.thebaileybrew.switchtime.MainActivity;
import com.thebaileybrew.switchtime.R;
import com.thebaileybrew.switchtime.adapter.ImageRefs;
import com.thebaileybrew.switchtime.objects.ChildSliderLayoutManager;
import com.thebaileybrew.switchtime.objects.ChildSliderViewHolder;
import com.thebaileybrew.switchtime.objects.SliderLayoutManager;
import com.thebaileybrew.switchtime.objects.SliderViewHolder;
import com.thebaileybrew.switchtime.utils.DisplayMetricUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.thebaileybrew.switchtime.utils.ConstantTribulations.NUMBER_OF_CHILDREN;

public class SwitchSplashLoading extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = SwitchSplashLoading.class.getSimpleName();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mChildDatabaseReference;
    private DatabaseReference mImageDatabaseReference;

    private RecyclerView recyclerView;
    private TextView childTextView;
    private ChildSliderLayoutManager childSliderManager;
    private ChildSliderViewHolder childSliderViewHolder;

    private Button addChild, removeChildren;
    private Button startTime;

    private int currentLoadedChildren;

    private CircleImageView childOneImage, childTwoImage, childThreeImage, childFourImage;
    private TextView childOneName, childTwoName, childThreeName, childFourName;
    private LinearLayout childOneLayout, childTwoLayout, childThreeLayout, childFourLayout;
    private Button submitNameButton;
    private EditText editTextName;
    private LinearLayout layoutForChildName;

    private File localStorage;
    private String selectedChildImage;

    private List<String> allImages = new ArrayList<>();

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SwitchTime.getContext());
        currentLoadedChildren = sharedPreferences.getInt(NUMBER_OF_CHILDREN,0);
        initViews();
        setupFirebase();
        loadAllImages();

        setupRecycler();

    }

    private void loadAllImages() {
        allImages = ImageRefs.getReferences();
    }

    private void setupFirebase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mImageDatabaseReference = mFirebaseDatabase.getReference("Images");
        //TODO:Setup check for current Children & update GridLayout
        //Update UI to display added children
        if (currentLoadedChildren != 0) {
            setVisibility(currentLoadedChildren);
            //Update UI

        }

    }

    private void setVisibility(int currentLoadedChildren) {
        switch (currentLoadedChildren) {
            case 1:
                childOneLayout.setVisibility(View.VISIBLE);
                break;
            case 2:
                childTwoLayout.setVisibility(View.VISIBLE);
                break;
            case 3:
                childThreeLayout.setVisibility(View.VISIBLE);
                break;
            case 4:
                childFourLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void updateChildAvatar(int currentChildren) {
        CircleImageView currentViewToUpdate;
        switch (currentChildren) {
            case 1:
            default:
                currentViewToUpdate = childOneImage;
                break;
            case 2:
                currentViewToUpdate = childTwoImage;
                break;
            case 3:
                currentViewToUpdate = childThreeImage;
                break;
            case 4:
                currentViewToUpdate = childFourImage;
                break;
        }
        Picasso.get()
                .load(selectedChildImage)
                .into(currentViewToUpdate);

    }


    private void initViews() {
        recyclerView = findViewById(R.id.child_list_recycler);
        childTextView = findViewById(R.id.child_name);
        addChild = findViewById(R.id.add_new_child);
        addChild.setOnClickListener(this);
        startTime = findViewById(R.id.start_app);
        startTime.setOnClickListener(this);
        removeChildren = findViewById(R.id.remove_all_children);
        removeChildren.setOnClickListener(this);

        childOneImage = findViewById(R.id.child_image_one);
        childTwoImage = findViewById(R.id.child_image_two);
        childThreeImage = findViewById(R.id.child_image_three);
        childFourImage = findViewById(R.id.child_image_four);

        childOneName = findViewById(R.id.child_name_one);
        childTwoName = findViewById(R.id.child_name_two);
        childThreeName = findViewById(R.id.child_name_three);
        childFourName = findViewById(R.id.child_name_four);

        childOneLayout = findViewById(R.id.child_avatar_picker_holder_one);
        childTwoLayout = findViewById(R.id.child_avatar_picker_holder_two);
        childThreeLayout = findViewById(R.id.child_avatar_picker_holder_three);
        childFourLayout = findViewById(R.id.child_avatar_picker_holder_four);

        submitNameButton = findViewById(R.id.submit_name_for_new_child);
        submitNameButton.setOnClickListener(this);
        editTextName = findViewById(R.id.edit_text_for_new_child);
        layoutForChildName = findViewById(R.id.layout_for_name_of_new_child);

    }

    private void setupRecycler() {
        childSliderManager = new ChildSliderLayoutManager(this, new ChildSliderLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {

            }
        });
        childSliderViewHolder = new ChildSliderViewHolder(this, allImages, new ChildSliderViewHolder.ChildSliderClickHandler() {
            @Override
            public void onClick(View view, String child) {
                int position = recyclerView.getChildLayoutPosition(view);
                recyclerView.smoothScrollToPosition(position);
                selectedChildImage = child;
                updateChildAvatar(currentLoadedChildren);

            }
        });

        int paddingRecycler = DisplayMetricUtils.getScreenWidth(SwitchTime.getContext())/2 - DisplayMetricUtils.displayToPixel(SwitchTime.getContext(),100);
        recyclerView.setPadding(paddingRecycler,0, paddingRecycler,0);
        recyclerView.setLayoutManager(childSliderManager);
        recyclerView.setAdapter(childSliderViewHolder);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_new_child:
                Toast.makeText(this, "Children Loaded: " + currentLoadedChildren, Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.VISIBLE);
                //TODO: Set Visibility on next item in Grid Layout
                currentLoadedChildren = currentLoadedChildren + 1;
                setVisibility(currentLoadedChildren);
                sharedPreferences.edit().putInt(NUMBER_OF_CHILDREN, currentLoadedChildren).apply();
                layoutForChildName.setVisibility(View.VISIBLE);
                break;
            case R.id.remove_all_children:
                currentLoadedChildren = 0;
                layoutForChildName.setVisibility(View.INVISIBLE);
                sharedPreferences.edit().putInt(NUMBER_OF_CHILDREN, currentLoadedChildren).apply();
                Toast.makeText(this, "Children Loaded: " + currentLoadedChildren, Toast.LENGTH_SHORT).show();
                clearAllImageViews();
                break;
            case R.id.start_app:
                Intent startApp = new Intent(this, MainActivity.class);
                startActivity(startApp);
                break;
            case R.id.submit_name_for_new_child:
                if (!TextUtils.isEmpty(editTextName.getText())) {
                    Toast.makeText(this, "current name is: " + editTextName.getText().toString(), Toast.LENGTH_SHORT).show();
                    switch (currentLoadedChildren) {
                        case 0:
                            break;
                        case 1:
                            childOneName.setText(editTextName.getText().toString());
                            break;
                        case 2:
                            childTwoName.setText(editTextName.getText().toString());
                            break;
                        case 3:
                            childThreeName.setText(editTextName.getText().toString());
                            break;
                        case 4:
                            childFourName.setText(editTextName.getText().toString());
                            break;
                    }
                    layoutForChildName.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(this, "Please enter a name...", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void clearAllImageViews() {
        Picasso.get()
                .load(R.drawable.ic_add_black_24dp)
                .into(childOneImage);
        Picasso.get()
                .load(R.drawable.ic_add_black_24dp)
                .into(childTwoImage);
        Picasso.get()
                .load(R.drawable.ic_add_black_24dp)
                .into(childThreeImage);
        Picasso.get()
                .load(R.drawable.ic_add_black_24dp)
                .into(childFourImage);
    }
}
