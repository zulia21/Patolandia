package com.example.patolandis;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.SliderViewHolder> {
    private List<SliderItem> slideritems;
    private ViewPager2 viewPager2;

     Adapter(List<SliderItem> slideritems, ViewPager2 viewPager2) {
        this.slideritems = slideritems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(slideritems.get(position));
    }

    @Override
    public int getItemCount() {
        return slideritems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{

        private RoundedImageView imageView;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlider);
        }
        void setImage(SliderItem sliderItem) { imageView.setImageURI(sliderItem.getImagens()); }


        }
        public void addImage(Uri uri) {
        slideritems.add(new SliderItem(uri));
        notifyItemInserted(slideritems.size() - 1);
    }


}
