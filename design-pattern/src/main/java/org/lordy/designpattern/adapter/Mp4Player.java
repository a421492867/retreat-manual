package org.lordy.designpattern.adapter;

public class Mp4Player implements AdvancedMediaPlayer{
    public void playVlc(String fileName) {

    }

    public void playMp4(String fileName) {
        System.out.println("play mp4 file " + fileName);
    }
}
