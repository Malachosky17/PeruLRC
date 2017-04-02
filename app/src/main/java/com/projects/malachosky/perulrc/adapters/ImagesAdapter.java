package com.projects.malachosky.perulrc.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.malachosky.perulrc.R;
import com.projects.malachosky.perulrc.holders.ImageViewHolder;
import com.projects.malachosky.perulrc.model.NoteImage;

import java.util.ArrayList;

/**
 * Created by Malac on 3/31/2017.
 *
 * @author: Malac
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private ArrayList<NoteImage> images;

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card, parent, false);
        return new ImageViewHolder(card);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
