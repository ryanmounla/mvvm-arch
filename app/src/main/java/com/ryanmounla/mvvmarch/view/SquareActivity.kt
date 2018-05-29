package com.ryanmounla.mvvmarch.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.ryanmounla.mvvmarch.R
import com.ryanmounla.mvvmarch.model.Image
import com.ryanmounla.mvvmarch.util.SystemUtils
import com.ryanmounla.mvvmarch.viewmodel.ImageViewModel

class SquareActivity : AppCompatActivity(), View.OnTouchListener {

    @BindView(R.id.fl_square) lateinit var flSquare: FrameLayout
    @BindView(R.id.fl_drop_zone) lateinit var flDropZone: FrameLayout
    @BindView(R.id.fl_rotator) lateinit var flRotator: FrameLayout
    @BindView(R.id.iv_image) lateinit var ivImage: ImageView
    private var dx: Float? = null
    private var dy: Float? = null
    private var viewModel: ImageViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_square)
        ButterKnife.bind(this)
        flSquare.setOnTouchListener(this)
        viewModel = ViewModelProviders.of(this).get(ImageViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        rotateSquare()
        displayRandomDog()
    }

    private fun rotateSquare(){
        val startRotateAnimation: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate)
        flRotator.startAnimation(startRotateAnimation)
    }

    private fun displayRandomDog(){
        viewModel?.registerDogImageObservable()?.observe(this, Observer { image: Image? ->
            Glide.with(this)
                    .load(image?.url)
                    .into(ivImage)
        })
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> handlePress(v, event)
            MotionEvent.ACTION_UP -> handleRelease(v)
            MotionEvent.ACTION_MOVE -> handleDrag(v, event)
        }
        return true
    }

    private fun handlePress(v: View, event: MotionEvent){
        dx = v.x - event.rawX
        dy = v.y - event.rawY
        displayRandomDog()
    }

    private fun handleRelease(v: View){
        if(!SystemUtils.isViewOverlapping(v, flDropZone) && flDropZone.visibility == View.VISIBLE) {
            hideBottomDrawer()
            snapSquareToOrigin()
        } else {
            snapSquareToDropZone()
        }
    }

    private fun handleDrag(v: View, event: MotionEvent){
        showBottomDrawer()
        v.animate()
                .x(event.rawX + dx!!)
                .y(event.rawY + dy!!)
                .setDuration(0)
                .start()

    }

    private fun snapSquareToOrigin(){
        snapToPosition(flSquare.y, 0f,
                flSquare.x, 0f)
    }

    private fun snapSquareToDropZone(){
        snapToPosition(flSquare.y, flDropZone.y + flDropZone.height/2 -flSquare.height/2,
                flSquare.x, flDropZone.x + flDropZone.width/2 - flSquare.width/2)
    }

    private fun snapToPosition(fromY: Float, toY: Float, fromX: Float, toX: Float){
        val animSetXY = AnimatorSet()
        val x: ObjectAnimator = ObjectAnimator.ofFloat(flSquare, "translationX", fromX, toX)
        val y: ObjectAnimator = ObjectAnimator.ofFloat(flSquare, "translationY", fromY, toY)
        animSetXY.playTogether(x, y)
        animSetXY.interpolator = LinearInterpolator()
        animSetXY.duration = 300
        animSetXY.start()
    }

    private fun showBottomDrawer(){
        if(flDropZone.alpha == 0.9f){
            flDropZone.alpha = 1f
            flDropZone.animate().translationYBy(-SystemUtils.dpToPx(200).toFloat()).start()
        }
    }

    private fun hideBottomDrawer(){
        flDropZone.animate().translationY(SystemUtils.dpToPx(200).toFloat()).withEndAction({ flDropZone.alpha = 0.9f })
    }

}