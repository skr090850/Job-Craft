package com.example.jobcraft.splash_and_onboarding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobcraft.R;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private final List<SliderItem> sliderItems;

    public SliderAdapter(List<SliderItem> sliderItems){
        this.sliderItems = sliderItems;
    }

    @NonNull
    @Override
    public SliderAdapter.SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.splash_and_onboarding_item_slider,parent,false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.SliderViewHolder holder, int position) {
        int realPosition = position % sliderItems.size();
        SliderItem currentItem = sliderItems.get(realPosition);
        holder.imageView.setImageResource(currentItem.getImage());
        holder.title.setText(currentItem.getTitle());
        holder.subTitle.setText(currentItem.getSubTitle());
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
    static class SliderViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView title;
        TextView subTitle;
        SliderViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.onBoardingImage);
            title = itemView.findViewById(R.id.onBoardingTitle);
            subTitle = itemView.findViewById(R.id.onBoardingSubTitle);
        }
    }
}
