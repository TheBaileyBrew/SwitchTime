package com.thebaileybrew.switchtime.objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.thebaileybrew.switchtime.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ChildSliderViewHolder extends RecyclerView.Adapter<ChildSliderViewHolder.ViewHolder> {
    private static final String TAG = ChildSliderViewHolder.class.getSimpleName();

    private Context mContext;
    private final LayoutInflater inflater;
    private List<String> data = new ArrayList<>();

    private ChildSliderClickHandler clickHandler;

    public interface ChildSliderClickHandler {
        void onClick(View view, String child);
    }

    public ChildSliderViewHolder(Context context, List<String> allImages, ChildSliderClickHandler clickHandler) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.data = allImages;
        this.clickHandler = clickHandler;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_child_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String currentImage = data.get(position);
        Picasso.get()
                .load(currentImage)
                .into(holder.circleImageView);
    }

    public void setTime(List<String> time) {
        this.data = time;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        } else {
            return data.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView circleImageView;

        private ViewHolder(View view) {
            super(view);
            circleImageView = view.findViewById(R.id.circular_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String currentChild = data.get(getAdapterPosition());
            clickHandler.onClick(v, currentChild);
        }
    }
}

