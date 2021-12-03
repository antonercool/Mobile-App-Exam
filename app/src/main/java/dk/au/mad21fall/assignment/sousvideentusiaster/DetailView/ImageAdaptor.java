package dk.au.mad21fall.assignment.sousvideentusiaster.DetailView;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class ImageAdaptor extends PagerAdapter {

    private Context mContext;
    private ArrayList<Bitmap> mImages;

    public ImageAdaptor(Context mContext) {
        this.mContext = mContext;
    }

    public void initImages(ArrayList<Bitmap> images){
        this.mImages = images;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(mImages.get(position));
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
