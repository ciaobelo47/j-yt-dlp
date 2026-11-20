package me.mailo.jytdlp;

import me.mailo.jytdlp.wrapper.AudioFormats;
import me.mailo.jytdlp.wrapper.Ytdlp;
import me.mailo.jytdlp.wrapper.YtdlpRequest;

import javax.swing.*;
import java.io.File;

/**
 * This class is an example on how you use the library
 */
class TestWrapper {
    public static void main(String[] args) {
        Ytdlp.enableDebug();
        YtdlpRequest req = new YtdlpRequest();

        File out = new File(System.getProperty("user.home"));

        req.audioQuality(0);
        req.audioFormat(AudioFormats.MP3);

        JFileChooser j = new JFileChooser();
        j.setCurrentDirectory(new File(System.getProperty("user.home")));
        j.setSelectedFile(new File("out"));
        if (j.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            out = j.getCurrentDirectory();
        } else {
            JOptionPane.showMessageDialog(null, "No Folder Selected! The program will exit.", "No Folder Selected!", JOptionPane.WARNING_MESSAGE);
            System.exit(-1);
        }

        req.setDownloadPath(out.getAbsolutePath());

        req.outputRegex("%(title)s.%(ext)s");
        req.withMetadata();
        req.withThumbnail();
        req.onlyAudio();
        req.setUrl(JOptionPane.showInputDialog(null, "Insert URL", "Insert Url", JOptionPane.QUESTION_MESSAGE)); // es. "https://www.youtube.com/watch?v=_v-cT1y9K44"
        System.out.println(Ytdlp.execute(req));
    }
}
