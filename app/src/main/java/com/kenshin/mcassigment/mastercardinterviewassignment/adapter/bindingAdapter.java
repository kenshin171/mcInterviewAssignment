package com.kenshin.mcassigment.mastercardinterviewassignment.adapter;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.kenshin.mcassigment.mastercardinterviewassignment.R;
import com.kenshin.mcassigment.mastercardinterviewassignment.di.module.GlideApp;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by kennethleong on 22/9/17.
 */

public class bindingAdapter {

    @BindingAdapter({"app:setGlideDrawableCircle"})
    public static void setDrawableCircle(ImageView view, String path) {

        RequestOptions options;

        view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        options = new RequestOptions()
                .placeholder(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_image))
                .error(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_error))
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        GlideApp.with(view.getContext())
                .load(view.getContext().getString(R.string.endpoint)+path)
                .transition(withCrossFade())
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        view.setImageResource(android.R.color.transparent);
                        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return false;
                    }
                })
                .into(view);

    }

}
