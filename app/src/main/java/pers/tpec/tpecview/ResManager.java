package pers.tpec.tpecview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.util.TypedValue;

public class ResManager {
    private final SparseArray<BitmapBundle> bmp;
    private int lastIndex;
    private Resources resources;

    public void init(final Resources resources) {
        this.resources = resources;
        bmp.clear();
        lastIndex = Integer.MIN_VALUE;
    }

    public int loadBmp(final int bmpId) {
        return loadBmp(resources, bmpId);
    }

    public int loadBmp(final Resources resources, final int bmpId) {
        Integer id = getIdByBmpId(bmpId);
        if (id == null) {
            id = getNewId();
            synchronized (bmp) {
                Bitmap bitmap = decodeResource(resources, bmpId);
                bmp.put(id, new BitmapBundle(bmpId, bitmap));
            }
            return id;
        } else {
            return id;
        }
    }

    public Bitmap getBmp(final int id) {
        BitmapBundle bb;
        synchronized (bmp) {
            bb = bmp.get(id);
        }
        if (bb == null) {
            return null;
        } else {
            return bb.bmp;
        }
    }

    private Integer getIdByBmpId(final int bmpId) {
        synchronized (bmp) {
            for (int i = 0; i < bmp.size(); i++) {
                if (bmpId == bmp.valueAt(i).bmpId) {
                    return bmp.keyAt(i);
                }
            }
        }
        return null;
    }

    public void unloadBmp(final int id) {
        synchronized (bmp) {
            bmp.delete(id);
        }
    }

    public void unloadBmp(@NonNull final Bitmap bitmap) {
        synchronized (bmp) {
            for (int i = 0; i < bmp.size(); i++) {
                if (bitmap == bmp.valueAt(i).bmp) {
                    bmp.delete(bmp.keyAt(i));
                    break;
                }
            }
        }
    }

    public void unloadBmpId(final int bmpId) {
        synchronized (bmp) {
            for (int i = 0; i < bmp.size(); i++) {
                if (bmpId == bmp.valueAt(i).bmpId) {
                    bmp.delete(bmp.keyAt(i));
                    break;
                }
            }
        }
    }

    private int getNewId() {
        do {
            if (lastIndex == Integer.MAX_VALUE) {
                lastIndex = Integer.MIN_VALUE;
            } else {
                lastIndex++;
            }
        } while (bmp.get(lastIndex, null) != null);
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

    class BitmapBundle {
        int bmpId;
        Bitmap bmp;

        public BitmapBundle(int bmpId, Bitmap bmp) {
            this.bmpId = bmpId;
            this.bmp = bmp;
        }
    }
}
