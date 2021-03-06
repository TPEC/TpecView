package pers.tpec.tpecview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import pers.tpec.tpecview.controller.Controller;

public class TpecView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    protected static final Logger LOG = Logger.getLogger(TpecView.class.getName());

    public static final int SCALEMOD_NULL = 0;
    public static final int SCALEMOD_STRETCH = 1;
    public static final int SCALEMOD_PRESERVE = 2;
    public static final int SCALEMOD_MANUAL_SCALE = 3;

    private SurfaceHolder holder;

    private Context context;

    protected volatile boolean threadFlag;

    private int screenWidth, screenHeight;
    private int windowWidth, windowHeight;
    private float scaleWidth, scaleHeight, scaleTranslateX, scaleTranslateY;
    private int scaleMod;

    private Paint paintBlack;

    protected Scene scene = null, sceneBack = null;
    private Controller controller = null;

    protected long targetInterval = 16666667;

    protected boolean preloadScene = false;

    private Lock controllerLock = new ReentrantLock();

    /**
     * @param context      context of activity
     * @param windowWidth  target window width
     * @param windowHeight target window height
     * @param scaleMod     @SCALEMOD
     */
    public TpecView(Context context, int windowWidth, int windowHeight, int scaleMod) {
        super(context);

        this.context = context;

        this.resize(windowWidth, windowHeight)
                .setScaleMod(scaleMod);

        holder = this.getHolder();
        holder.addCallback(this);

        paintBlack = new Paint();
    }

    /**
     * @param windowWidth  target window width
     * @param windowHeight target window height
     * @return this
     */
    public TpecView resize(int windowWidth, int windowHeight) {
        if (windowWidth <= 0 || windowHeight <= 0) {
            return this;
        }
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        LOG.info("Resize to (" + String.valueOf(windowWidth) + "," + String.valueOf(windowHeight) + ")");
        if (screenHeight > 0) {
            applyResolutionRatio();
        }
        return this;
    }

    /**
     * @param scaleMod @SCALEMOD
     * @return this
     */
    public TpecView setScaleMod(int scaleMod) {
        this.scaleMod = scaleMod;
        if (screenHeight > 0) {
            applyResolutionRatio();
        }
        return this;
    }

    public TpecView setManualScale(float scaleWidth, float scaleHeight) {
        if (scaleMod != SCALEMOD_MANUAL_SCALE) {
            setScaleMod(SCALEMOD_MANUAL_SCALE);
        }
        this.scaleWidth = scaleWidth;
        this.scaleHeight = scaleHeight;
        return this;
    }

    /**
     * Not thread safe
     *
     * @param scene new scene
     * @return this
     */
    final public TpecView setScene(@NonNull final Scene scene) {
        if (this.scene != null) {
            this.scene.unload();
        }
        this.scene = scene;
        setController(scene);
        this.scene.load();
        LOG.info("setScene:" + scene.toString());
        return this;
    }

    /**
     * Thread safe
     *
     * @param scene new scene
     * @return this
     */
    final public TpecView switchScene(@NonNull final Scene scene) {
        this.sceneBack = scene;
        return this;
    }

    /**
     * Thread safe
     * Preload scene before
     *
     * @param scene new scene
     * @return this
     */
    final public TpecView switchAndPreloadScene(@NonNull final Scene scene) {
        preloadScene = true;
        this.sceneBack = scene;
        scene.load();
        return this;
    }

    /**
     * @param controller @Controller
     * @return this
     */
    final public TpecView setController(final Controller controller) {
        controllerLock.lock();
        try {
            this.controller = controller;
        } finally {
            controllerLock.unlock();
        }
        return this;
    }

    /**
     * @param targetInterval nano second
     * @return this
     */
    final public TpecView setTargetInterval(final long targetInterval) {
        if (targetInterval > 0) {
            this.targetInterval = targetInterval;
        }
        return this;
    }

    final public TpecView setTargetFPS(final double targetFPS) {
        if (targetFPS > 0 && targetFPS <= 1000000000.0) {
            this.targetInterval = (long) (1000000000.0 / targetFPS);
        }
        return this;
    }

    private void applyResolutionRatio() {
        scaleTranslateX = 0;
        scaleTranslateY = 0;
        if (scaleMod == SCALEMOD_NULL) {
            scaleWidth = 1f;
            scaleHeight = 1f;
        } else if (scaleMod == SCALEMOD_STRETCH) {
            scaleWidth = (float) screenWidth / (float) windowWidth;
            scaleHeight = (float) screenHeight / (float) windowHeight;
        } else if (scaleMod == SCALEMOD_PRESERVE) {
            if ((float) windowWidth / (float) windowHeight >= (float) screenWidth / (float) screenHeight) {
                scaleWidth = (float) screenWidth / (float) windowWidth;
                scaleTranslateY = ((float) screenHeight - (float) windowHeight * scaleWidth) / 2;
            } else {
                scaleWidth = (float) screenHeight / (float) windowHeight;
                scaleTranslateX = ((float) screenWidth - (float) windowWidth * scaleWidth) / 2;
            }
            scaleHeight = scaleWidth;
        }
        LOG.info("scaleWidth:" + String.valueOf(scaleWidth) + ", scaleHeight:" + String.valueOf(scaleHeight));
    }

    protected void draw() {
        Canvas canvas = null;
        try {
            canvas = holder.lockCanvas();
            if (canvas != null) {
                canvas.save();
                canvas.translate(scaleTranslateX, scaleTranslateY);
                canvas.scale(scaleWidth, scaleHeight);

                if (scene != null) {
                    scene.draw(canvas);
                } else {
                    canvas.drawRect(0, 0, windowWidth, windowHeight, paintBlack);
                }

                canvas.restore();
                if (scaleTranslateY > 0) {
                    canvas.drawRect(0, 0, screenWidth, scaleTranslateY, paintBlack);
                    canvas.drawRect(0, screenHeight - scaleTranslateY, screenWidth, screenHeight, paintBlack);
                }
                if (scaleTranslateX > 0) {
                    canvas.drawRect(0, 0, scaleTranslateX, screenHeight, paintBlack);
                    canvas.drawRect(screenWidth - scaleTranslateX, 0, screenWidth, screenHeight, paintBlack);
                }
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        } finally {
            if (canvas != null)
                holder.unlockCanvasAndPost(canvas);
        }
    }

    public final Context getActivityContext() {
        return context;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Thread thread = new Thread(this);
        threadFlag = true;
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        screenWidth = width;
        screenHeight = height;
        LOG.info("screenWidth:" + String.valueOf(screenWidth) + ", screenHeight:" + String.valueOf(screenHeight));
        applyResolutionRatio();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        threadFlag = false;
    }

    @Override
    final public boolean onTouchEvent(MotionEvent event) {
        if (controller != null) {
            event.setLocation((event.getX() - scaleTranslateX) / scaleWidth, (event.getY() - scaleTranslateY) / scaleHeight);
            controllerLock.lock();
            try {
                return controller.onTouch(event);
            } finally {
                controllerLock.unlock();
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (scene != null) {
            return scene.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void run() {
        long startTime = System.nanoTime() - targetInterval;
        while (threadFlag) {
            while (System.nanoTime() - startTime >= targetInterval) {
                if (sceneBack != null) {
                    if (scene != null) {
                        scene.unload();
                    }
                    scene = sceneBack;
                    sceneBack = null;
                    setController(scene);
                    if (!preloadScene) {
                        scene.load();
                    }
                    LOG.info("switch to scene:" + scene.toString());
                }
                if (scene != null) {
                    scene.logic();
                }
                startTime += targetInterval;
            }
            draw();
            long st = (targetInterval - (System.nanoTime() - startTime)) / 1000000;
            if (st > 0) {
                try {
                    Thread.sleep(st);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onPause() {
        if (scene != null) {
            scene.pause();
        }
    }

    public void onResume() {
        if (scene != null) {
            scene.resume();
        }
    }
}
