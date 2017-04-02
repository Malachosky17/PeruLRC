package com.projects.malachosky.perulrc.holders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.projects.malachosky.perulrc.R;
import com.projects.malachosky.perulrc.model.NoteImage;

import java.lang.ref.WeakReference;

/**
 * Created by Malac on 3/31/2017.
 *
 * @author: Malac
 */

public class ImageViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;

    public ImageViewHolder(View itemView) {
        super(itemView);
        this.imageView = (ImageView) itemView.findViewById(R.id.img_thumb);
    }

    public void updateUI(NoteImage image) {
        this.imageView.setImageBitmap(decodeURI(image.getImgURI().getPath()));
        DecodeBitmapTask task = new DecodeBitmapTask(imageView, image);
        task.execute();
    }

    public class DecodeBitmapTask extends AsyncTask<Void, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewWeakReference;
        private NoteImage image;


        public DecodeBitmapTask(ImageView imageView, NoteImage image) {
            this.imageViewWeakReference = new WeakReference<>(imageView);
            this.image = image;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            return decodeURI(image.getImgURI().getPath());
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            final ImageView img = imageViewWeakReference.get();
            if(img != null) {
                img.setImageBitmap(bitmap);
            }
        }
    }

    public Bitmap decodeURI(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        //Only scale if needed
        Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math.abs(options.outWidth - 100);
        if(options.outHeight * options.outWidth * 2 >= 16384) {
            double sampleSize = scaleByHeight
                    ? options.outHeight / 1000
                    : options.outWidth / 1000;
            options.inSampleSize = (int)Math.pow(2d, Math.floor(
                    Math.log(sampleSize)/Math.log(2d)
            ));
        }
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[512];
        Bitmap output = BitmapFactory.decodeFile(filePath, options);
        return output;
    }
}
