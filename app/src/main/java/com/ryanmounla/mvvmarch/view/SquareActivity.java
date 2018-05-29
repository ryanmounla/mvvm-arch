package com.ryanmounla.mvvmarch.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ryanmounla.mvvmarch.R;
import com.ryanmounla.mvvmarch.util.SystemUtils;
import com.ryanmounla.mvvmarch.viewmodel.ImageViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ryanmounla on 2018-05-28.
 */

public class SquareActivity extends AppCompatActivity implements View.OnTouchListener {

    @BindView(R.id.fl_square) FrameLayout flSquare;
    @BindView(R.id.fl_drop_zone) FrameLayout flDropZone;
    @BindView(R.id.fl_rotator) FrameLayout flRotator;
    @BindView(R.id.iv_image) ImageView ivImage;
    private float dX, dY;
    private ImageViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);
        ButterKnife.bind(this);
        flSquare.setOnTouchListener(this);
        viewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
    }


    @Override
    protected void onStart() {
        super.onStart();
        rotateSquare();
        displayRandomDog();
    }

    private void rotateSquare() {
        Animation startRotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        flRotator.startAnimation(startRotateAnimation);

    }

    private void displayRandomDog() {
        viewModel.registerDogImageObservable().observe(this, image -> {
            if(image != null) {
                Glide.with(this)
                        .load(image.url)
                        .into(ivImage);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handlePress(v, event);
                break;
            case MotionEvent.ACTION_MOVE:
                handleDrag(v, event);
                break;
            case MotionEvent.ACTION_UP:
                handleRelease(v);
                break;
            default:
                break;
        }
        return true;
    }

    private void handlePress(View v, MotionEvent event) {
        dX = v.getX() - event.getRawX();
        dY = v.getY() - event.getRawY();
        displayRandomDog();
    }

    private void handleDrag(View v, MotionEvent event) {
        showBottomDrawer();
        v.animate()
                .x(event.getRawX() + dX)
                .y(event.getRawY() + dY)
                .setDuration(0)
                .start();
    }

    private void handleRelease(View v) {
        if(!SystemUtils.isViewOverlapping(v, flDropZone) && flDropZone.getVisibility() == View.VISIBLE) {
            hideBottomDrawer();
            snapSquareToOrigin();
        } else {
            snapSquareToDropZone();
        }
    }

    private void snapSquareToOrigin() {
        snapToPosition(flSquare.getY(), 0,
                flSquare.getX(), 0);
    }

    private void snapSquareToDropZone() {
        snapToPosition(flSquare.getY(), flDropZone.getY() + flDropZone.getHeight()/2 - flSquare.getHeight()/2,
                flSquare.getX(), flDropZone.getX() +flDropZone.getWidth()/2 - flSquare.getWidth()/2);
    }

    private void snapToPosition(float fromY, float toY, float fromX, float toX){
        AnimatorSet animSetXY = new AnimatorSet();
        ObjectAnimator y = ObjectAnimator.ofFloat(flSquare, "translationY",fromY, toY);
        ObjectAnimator x = ObjectAnimator.ofFloat(flSquare, "translationX", fromX, toX);
        animSetXY.playTogether(x, y);
        animSetXY.setInterpolator(new LinearInterpolator());
        animSetXY.setDuration(300);
        animSetXY.start();
    }

    private void showBottomDrawer() {
        if(flDropZone.getAlpha() == 0.9f) {
            flDropZone.setAlpha(1f);
            flDropZone.animate().translationYBy(-SystemUtils.dpToPx(200)).start();
        }
    }

    private void hideBottomDrawer() {
        flDropZone.animate().translationY(SystemUtils.dpToPx(200)).withEndAction(() -> flDropZone.setAlpha(0.9f));
    }


}
