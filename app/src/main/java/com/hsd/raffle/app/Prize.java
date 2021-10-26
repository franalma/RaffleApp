package com.hsd.raffle.app;

class Prize {
    private String name;
    private String img;
    private int posX;
    private int posY;

    public Prize(String name, String img, int posX, int posY) {
        this.name = name;
        this.img = img;
        this.posX = posX;
        this.posY = posY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}
