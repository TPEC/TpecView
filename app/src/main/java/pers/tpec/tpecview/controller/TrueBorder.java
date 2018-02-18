package pers.tpec.tpecview.controller;

public class TrueBorder implements Border {
    @Override
    public boolean contains(int x, int y) {
        return true;
    }
}
