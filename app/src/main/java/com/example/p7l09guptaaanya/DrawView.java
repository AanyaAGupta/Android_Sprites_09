package com.example.p7l09guptaaanya;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DrawView extends View {
    Paint paint=new Paint();
    Sprite sprite = new Sprite();
    Sprite foodSprite, badSprite;
    private static final int MAX_STREAMS=100;
    private int soundIdBackground;
    private boolean soundPoolLoaded;
    private SoundPool soundPool;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        foodSprite = generateSprite();
        badSprite = generateSprite();
        badSprite.setColor(Color.GREEN);
        sprite.grow(100);
        sprite.setBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.dogsprite4));
        initSoundPool();
        //sprite = new Sprite(); //now u can instantiate with info of the screen
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        //never instantiate a sprite object here!!
        super.onDraw(canvas);
        paint.setColor(Color.GRAY);//set paint to gray
        canvas.drawRect(getLeft(),0,getRight(),getBottom(),paint);//paint background gray
        paint.setColor(Color.RED);//set paint to red
        //sprite updates itself
        //sprite.update();
        sprite.update(canvas);
        foodSprite.update(canvas);
        badSprite.update(canvas);
        if(RectF.intersects(sprite, foodSprite)){
            foodSprite=generateSprite();
            sprite.grow(10);

        }
        if(RectF.intersects(sprite, badSprite)){
            badSprite=generateSprite();
            badSprite.setColor(Color.GREEN);
            sprite.grow(-5);
        }
        if(RectF.intersects(foodSprite, badSprite)){
            foodSprite.grow((int)(-foodSprite.width()*.1));//shrink food
            badSprite=generateSprite();//recreate badSprite
            badSprite.setColor(Color.GREEN);
        }
        //sprite draws itself
        sprite.draw(canvas);
        foodSprite.draw(canvas);
        badSprite.draw(canvas);
        invalidate();  //redraws screen, invokes onDraw()
    }

    private Sprite generateSprite(){
        float x = (float)(Math.random()*(getWidth()-.1*getWidth()));
        float y = (float)(Math.random()*(getHeight()-.1*getHeight()));
        int dX = (int)(Math.random()*11-5);
        int dY = (int)(Math.random()*11-5);
        return new Sprite(x,y,x+.1f*getWidth(),y+.1f*getWidth(),dX,dY,Color.MAGENTA);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(badSprite.contains(event.getX(),event.getY())){
                badSprite=generateSprite();
                badSprite.setColor(Color.GREEN);
            }
        }
        return true;
    }

    private void initSoundPool()  {
        // With Android API >= 21.
        if (Build.VERSION.SDK_INT >= 21 ) {
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            SoundPool.Builder builder= new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);
            this.soundPool = builder.build();
        }
        // With Android API < 21
        else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }
        // When SoundPool load complete.
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPoolLoaded = true;
                // Playing background sound.
                playSoundBackground();
            }
        });
        // Load the sound background.mp3 into SoundPool
        soundIdBackground= soundPool.load(this.getContext(), R.raw.dogbark,1);
        // Load the sound explosion.wav into SoundPool
    }

    public void playSoundBackground()  {
        if(soundPoolLoaded) {
            float leftVolumn = 0.8f;
            float rightVolumn =  0.8f;
            // Play sound background.mp3
            int streamId = this.soundPool.play(this.soundIdBackground,leftVolumn, rightVolumn, 1, -1, 1f);
        }
    }

}

