package pers.tpec.tpecview;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.MotionEvent;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected final TpecView tpecView;

    private final SparseArray<SceneObject> sceneObjects;
    private int lastPriority;

    public Scene(@NonNull final TpecView tpecView) {
        this.tpecView = tpecView;
        sceneObjects = new SparseArray<>();
        lastPriority = Integer.MAX_VALUE;
    }

    public abstract void load();

    public abstract void unload();

    public abstract void pause();

    public abstract void resume();

    public void draw(Canvas canvas) {
        synchronized (sceneObjects) {
            for (int i = 0; i < sceneObjects.size(); i++) {
                sceneObjects.valueAt(i).drawSelf(canvas);
            }
        }
    }

    public void logic() {
        List<Integer> nullList = new ArrayList<>();
        synchronized (sceneObjects) {
            for (int i = 0; i < sceneObjects.size(); i++) {
                sceneObjects.valueAt(i).logicSelf();
                if (sceneObjects.valueAt(i).isNull()) {
                    nullList.add(sceneObjects.keyAt(i));
                }
            }
        }
        if (!nullList.isEmpty()) {
            removeSceneObject(nullList);
        }
    }

    public boolean onTouch(MotionEvent event) {
        synchronized (sceneObjects) {
            for (int i = 0; i < sceneObjects.size(); i++) {
                if (sceneObjects.valueAt(i).onTouch(event)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected final void switchScene(@NonNull Scene scene) {
        tpecView.switchScene(scene);
    }

    protected final int addSceneObject(@NonNull SceneObject sceneObject) {
        return addSceneObject(sceneObject, getNewPriority());
    }

    private int getNewPriority() {
        boolean f;
        do {
            if (lastPriority == Integer.MAX_VALUE) {
                lastPriority = Integer.MIN_VALUE;
            } else {
                lastPriority++;
            }
            f = sceneObjects.get(lastPriority,null) != null;
        } while (f);
        return lastPriority;
    }

    protected final int addSceneObject(@NonNull SceneObject sceneObject, int priority) {
        synchronized (sceneObjects) {
            if (sceneObjects.get(priority) != null) {
                throw new InvalidParameterException("This priority already has an object binding on it.");
            }
            sceneObjects.put(priority, sceneObject);
        }
        lastPriority = priority;
        return priority;
    }

    protected final Scene removeSceneObject(@NonNull SceneObject sceneObject) {
        synchronized (sceneObjects) {
            for (int i = 0; i < sceneObjects.size(); i++) {
                if (sceneObjects.valueAt(i) == sceneObject) {
                    sceneObjects.remove(sceneObjects.keyAt(i));
                }
            }
        }
        return this;
    }

    protected final SceneObject getSceneObject(int priority) {
        return sceneObjects.get(priority, null);
    }

    protected final Scene removeSceneObject(int priority) {
        synchronized (sceneObjects) {
            if (sceneObjects.get(priority) != null) {
                sceneObjects.remove(priority);
            }
        }
        return this;
    }

    private void removeSceneObject(List<Integer> removeList) {
        synchronized (sceneObjects) {
            for (Integer i : removeList) {
                sceneObjects.delete(i);
            }
        }
    }

    protected final Scene clearSceneObject() {
        synchronized (sceneObjects) {
            sceneObjects.clear();
        }
        return this;
    }
}