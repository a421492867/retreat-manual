package org.lordy.designpattern.adapter;

public class VlcPlayer implements AdvancedMediaPlayer{

    public void playVlc(String fileName) {
        System.out.println("play vlc file " +fileName);
    }

    public void playMp4(String fileName) {

    }
}
