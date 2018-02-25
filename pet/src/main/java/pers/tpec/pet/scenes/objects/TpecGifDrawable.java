package pers.tpec.pet.scenes.objects;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.logging.Logger;

import pl.droidsonroids.gif.GifDrawable;

public class TpecGifDrawable extends GifDrawable {
    private static final Logger LOG = Logger.getLogger(TpecGifDrawable.class.getName());

    private int frameIndex;
    private Rect rectSrc;
    private int width;
    private int height;

    public TpecGifDrawable(@NonNull AssetManager assets, @NonNull String assetName) throws IOException {
        super(assets, assetName);
        this.reset();
        String s0 = super.toString();
        int i0 = s0.indexOf("size:");
        int i1 = s0.indexOf("x", i0);
        int i2 = s0.indexOf(",", i1);
        String s1 = s0.substring(i0 + 6, i1);
        String s2 = s0.substring(i1 + 1, i2);
        width = Integer.parseInt(s1);
        height = Integer.parseInt(s2);
        rectSrc = new Rect(0, 0, width, height);
        LOG.info("gif size: (" + s1 + "," + s2 + ")");
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rect getRectSrc() {
        return rectSrc;
    }

    private void nextFrame() {
        frameIndex++;
        if (frameIndex >= super.getNumberOfFrames()) {
            frameIndex = 0;
        }
    }

    public Bitmap getNextFrame() {
        nextFrame();
        return super.seekToFrameAndGet(frameIndex);
    }

    public Bitmap getCurrentFrame() {
        return super.getCurrentFrame();
    }

    @Override
    public void reset() {
        frameIndex = 0;
    }
}
