package me.mailo.jytdlp.wrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class defines a request to the ytdlp binary NOT a request to download a file
 */
public class YtdlpRequest {
    /**
     * Contains all the arguments to run ytdlp with
     */
    private HashMap<String, String> OPT;

    public YtdlpRequest() {
        OPT = new HashMap<>();
    }

    private String url;
    private boolean onlyAudio = false;

    /**
     * Updates ytdlp's binary
     */
    public void updateReq() {
        OPT.put("-U", null);
    }
    public boolean versionReq() {
        OPT.put("--version", null);
        return true;
    }

    public void setUrl(String s) { url = s; }
    public void setDownloadPath(String path) { OPT.put("-P", path); }

    /**
     * @param audioFormat audio formats are defined in {@link AudioFormats}
     */
    public void audioFormat(String audioFormat) { OPT.put("--audio-format", audioFormat); }

    /**
     * @param pattern <a href="https://github.com/yt-dlp/yt-dlp?tab=readme-ov-file#output-template">yt-dlp patterns explained</a>
     */
    public void outputRegex(String pattern) { OPT.put("-o", pattern); }

    /**
     * @param cookie cookie file to bypass age restricted or blocked content. Must be smth like "cookies.txt"
     */
    public void cookieFile(File cookie) { OPT.put("--cookies", cookie.getAbsolutePath()); }

    /**
     * the simple way to get cookies
     * @param browser major browser are defined in {@link AudioFormats}. You can use a custom string if you want
     */
    public  void cookieFromBrowser(String browser) { OPT.put("--cookies-from-browser", browser); }

    /**
     * @param quality number from 0 (best) to 10 (worst)
     */
    public void audioQuality(int quality) { OPT.put("--audio-quality", String.valueOf(quality)); }
    public void onlyAudio() { onlyAudio = true; }

    // Metadata on the final audio/video file
    public void withMetadata() { OPT.put("--add-metadata", null); }
    public void withThumbnail() { OPT.put("--embed-thumbnail", null); }

    /**
     * Creates the final command to be executed
     * @return the binary + the list of arguments
     */
    protected List<String> argBuilder() {
        List<String> s = new ArrayList<>();

        s.add(Ytdlp.ytdlpBin.getAbsolutePath());

        if (Ytdlp.logger.enableDebug) {
            s.add("-v");
        }

        if (onlyAudio) {
            s.add("-x");
        }

        for (Map.Entry<String, String> option : OPT.entrySet()) {
            String key = option.getKey();
            String value = option.getValue();
            if (value == null) {
                value = "";
            }

            s.add(key);
            s.add(value);
        }

        if (url != null) {
            s.add(url);
        }

        return s;
    }
}
