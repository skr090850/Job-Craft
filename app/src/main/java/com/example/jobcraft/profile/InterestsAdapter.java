package com.example.jobcraft.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.jobcraft.databinding.ProfileListItemInterestBinding;

import java.util.List;

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.InterestsViewHolder> {
    private final List<InterestItem> interestItems;

    public InterestsAdapter(List<InterestItem> interestItems) {
        this.interestItems = interestItems;
    }

    @NonNull
    @Override
    public InterestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProfileListItemInterestBinding binding = ProfileListItemInterestBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new InterestsViewHolder(binding);
    }

    public void onBindViewHolder(@NonNull InterestsViewHolder holder, int position){
        InterestItem item = interestItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return interestItems.size();
    }

    class InterestsViewHolder extends RecyclerView.ViewHolder {
        private final ProfileListItemInterestBinding binding;

        public InterestsViewHolder(ProfileListItemInterestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(InterestItem item) {
            binding.itemIcon.setImageResource(item.getIconResId());
            binding.itemName.setText(item.getItemName());
            binding.itemCheckBox.setChecked(item.isSelected());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setSelected(!item.isSelected());
                    binding.itemCheckBox.setChecked(item.isSelected());
                }
            });
        }

    }
}
