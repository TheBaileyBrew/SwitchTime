package com.thebaileybrew.switchtime.objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thebaileybrew.switchtime.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SliderViewHolder extends RecyclerView.Adapter<SliderViewHolder.ViewHolder> {
    private static final String TAG = SliderViewHolder.class.getSimpleName();

    private final LayoutInflater inflater;
    private List<String> data = new ArrayList<>();

    private SliderClickHandler clickHandler;

    public interface SliderClickHandler {
        void onClick(View view, String time);
    }

    public SliderViewHolder(Context context, List<String> timeCount, SliderClickHandler clickHandler) {
        this.inflater = LayoutInflater.from(context);
        this.data = timeCount;
        this.clickHandler = clickHandler;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.time_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String currentTime = data.get(position);
        holder.timeView.setText(String.valueOf(currentTime));
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
        final TextView timeView;

        private ViewHolder(View view) {
            super(view);
            timeView = view.findViewById(R.id.time_id);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String currentTime = data.get(getAdapterPosition());
            clickHandler.onClick(v, currentTime);
        }
    }
}

