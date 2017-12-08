package pers.tpec.tpecview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.util.TypedValue;

public class ResManager {
    private final SparseArray<Bitmap> bmp;
    private int lastIndex;
    private Resources resources;

    public void init(final Resources resources) {
        this.resources = resources;
        bmp.clear();
        lastIndex = Integer.MIN_VALUE;
    }

    public int loadBmp(final int bmpId) {
        int i = getNewId();
        synchronized (bmp) {
            Bitmap bitmap = decodeResource(bmpId);
            bmp.put(i, bitmap);
        }
        return i;
    }

    public int loadBmp(final Resources resources, final int bmpId) {
        int i = getNewId();
        synchronized (bmp) {
            Bitmap bitmap = decodeResource(resources, bmpId);
            bmp.put(i, bitmap);
        }
        return i;
    }

    public Bitmap getBmp(final int id) {
        Bitmap r;
        synchronized (bmp) {
            r = bmp.get(id);
        }
        return r;
    }

    public void unloadBmp(final int id) {
        synchronized (bmp) {
            bmp.delete(id);
        }
    }

    public void unloadBmp(@NonNull final Bitmap bitmap) {
        synchronized (bmp) {
            for (int i = 0; i < bmp.size(); i++) {
                if (bitmap == bmp.valueAt(i)) {
                    bmp.delete(bmp.keyAt(i));
                    break;
                }
            }
        }
    }

    private int getNewId() {
        boolean f;
        do {
            if (lastIndex == Integer.MAX_VALUE) {
                lastIndex = Integer.MIN_VALUE;
            } else {
                lastIndex++;
            }
            f = bmp.get(lastIndex, null) != null;
        } while (f);
        return lastIndex;
    }

    public static Bitmap decodeResource(final Resources resources, final int bmpId) {
        TypedValue value = new TypedValue();
        resources.openRawResource(bmpId, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, bmpId, opts);
    }

    public Bitmap decodeResource(final int bmpId) {
        TypedValue value = new TypedValue();
        resources.openRawResource(bmpId, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, bmpId, opts);
    }

    private ResManager() {
        bmp = new SparseArray<>();
        lastIndex = Integer.MIN_VALUE;
    }

    private static ResManager ourInstance = new ResManager();

    public static ResManager getInstance() {
        return ourInstance;
    }
}
