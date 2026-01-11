package me.mailo.jytdlp;

import me.mailo.jytdlp.wrapper.AudioFormats;
import me.mailo.jytdlp.wrapper.Ytdlp;
import me.mailo.jytdlp.wrapper.YtdlpRequest;
import me.mailo.jytdlp.download.Downloader;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * This class is only used for testing the library
 * <br>
 * <b>Do NOT import it anywhere</b>
 */
public class TestWrapper {
    public static void main(String[] args) {
        Ytdlp.enableDebug();

        File out = new File(System.getProperty("user.home"));

        YtdlpRequest.audioQuality(0);
        YtdlpRequest.audioFormat(AudioFormats.MP3);

        JFileChooser j = new JFileChooser();
        j.setCurrentDirectory(new File(System.getProperty("user.home")));
        j.setSelectedFile(new File("out"));
        if (j.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            out = j.getCurrentDirectory();
        } else {
            JOptionPane.showMessageDialog(null, "No Folder Selected! The program will exit.", "No Folder Selected!", JOptionPane.WARNING_MESSAGE);
            System.exit(-1);
        }

        YtdlpRequest.setDownloadPath(out.getAbsolutePath());

        YtdlpRequest.outputRegex("%(title)s.%(ext)s");
        YtdlpRequest.withMetadata();
        YtdlpRequest.withThumbnail();
        YtdlpRequest.onlyAudio();
        YtdlpRequest.setUrl("https://music.youtube.com/watch?v=lYBUbBu4W08");
        Ytdlp.execute();
    }
}
