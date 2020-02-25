package pers.tpec.pet;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.logging.Logger;

import pers.tpec.pet.scenes.MainScene;
import pers.tpec.pet.utils.ViewResizer;
import pers.tpec.tpecview.TpecView;

public class MainActivity extends AppCompatActivity implements ViewResizer {
    private static final Logger LOG = Logger.getLogger(MainActivity.class.getName());

    private static final int OVERLAY_PERMISSION_ID = 213;

    private static final int WINDOW_WIDTH = 200;
    private static final int WINDOW_HEIGHT = 200;

    private Point screenSize = new Point();

    private TpecView tpecView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;

    private float scale = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tpecView = new TpecView(this, WINDOW_WIDTH, WINDOW_HEIGHT, TpecView.SCALEMOD_MANUAL_SCALE);
        tpecView.setManualScale(1f, 1f);
        tpecView.setScene(new MainScene(tpecView).setViewResizer(this));
        tpecView.setTargetInterval(33333333);

        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_ID);
        } else {
            showView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_ID) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "Not granted", Toast.LENGTH_SHORT).show();
                } else {
                    showView();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        windowManager.getDefaultDisplay().getSize(screenSize);
        LOG.info("SCREEN SIZE: (" + String.valueOf(screenSize.x) + "," + String.valueOf(screenSize.y) + ")");
        int min = screenSize.x > screenSize.y ? screenSize.y : screenSize.x;
        scale = (float) min / 1080f;
        tpecView.setManualScale(scale, scale);
        LOG.info("SCREEN SCALE: " + String.valueOf(scale));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (windowManager != null) {
            windowManager.removeView(tpecView);
        }
    }

    private void showView() {
        params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        params.flags |= WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        params.windowAnimations = 0;
        params.alpha = 1.0f;
        params.gravity = Gravity.START | Gravity.TOP;

        params.x = 0;
        params.y = 0;
        params.width = WINDOW_WIDTH;
        params.height = WINDOW_HEIGHT;

        windowManager.addView(tpecView, params);
        tpecView.setZOrderMediaOverlay(true);
        tpecView.getHolder().setFormat(PixelFormat.TRANSPARENT);

        moveTaskToBack(true);
    }

    @Override
    public void offset(int x, int y) {
        if (params != null) {
            params.x += (float) x * scale;
            params.y += (float) y * scale;
            if (params.x < 0) {
                params.x = 0;
            } else if (params.x + params.width >= screenSize.x) {
                params.x = screenSize.x - params.width;
            }
            if (params.y < 0) {
                params.y = 0;
            } else if (params.y + params.height >= screenSize.y) {
                params.y = screenSize.y - params.height;
            }
            windowManager.updateViewLayout(tpecView, params);
        }
    }

    @Override
    public void offsetTo(int x, int y) {
        if (params != null) {
            if (x < 0) {
                x = 0;
            } else if (x + params.width >= screenSize.x) {
                x = screenSize.x - params.width;
            }
            if (y < 0) {
                y = 0;
            } else if (y + params.height >= screenSize.y) {
                y = screenSize.y - params.height;
            }
            params.x = x;
            params.y = y;
            windowManager.updateViewLayout(tpecView, params);
        }
    }

    @Override
    public void resize(int width, int height) {
        if (params != null) {
            width = (int) ((float) width * scale);
            height = (int) ((float) height * scale);
            int midX = params.x + params.width / 2;
            int bottomY = params.y + params.height;
            params.width = width;
            params.height = height;
            params.x = midX - width / 2;
            params.y = bottomY - height;
            windowManager.updateViewLayout(tpecView, params);
            tpecView.resize(width, height);
        }
    }

    @Override
    public int getX() {
        return params == null ? 0 : params.x;
    }

    @Override
    public int getY() {
        return params == null ? 0 : params.y;
    }

    @Override
    public int getXRight() {
        return params == null ? 0 : screenSize.x - params.x - params.width;
    }

    @Override
    public int getYBottom() {
        return params == null ? 0 : screenSize.y - params.y - params.height;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
