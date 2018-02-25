package pers.tpec.pet.utils;

public interface ViewResizer {
    void offset(int x, int y);

    void offsetTo(int x, int y);

    void resize(int width, int height);

    int getX();

    int getY();

    int getXRight();

    int getYBottom();
}
