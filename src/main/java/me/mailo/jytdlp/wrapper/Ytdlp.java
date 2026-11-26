package me.mailo.jytdlp.wrapper;

import me.mailo.jytdlp.download.Downloader;
import me.mailo.willylogger.ConsoleColors;
import me.mailo.willylogger.LogLevel;
import me.mailo.willylogger.WillyLogger;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Main Class of the library. Contains the execute method from which starts all other classes. That's why is the main class :)
 */
public class Ytdlp {
    public static WillyLogger logger = new WillyLogger("j-yt-dlp");
    /**
     * The location of ytdlp's binary, usually downloaded from {@link me.mailo.jytdlp.download.Downloader} class
     */
    public File ytdlpBin;
    public String ytDlpVersion;

    public Ytdlp() {
        ytdlpBin = new File(osPath());
        ytDlpVersion = ytDlpVersion();
    }

    public int execute(YtdlpRequest req) {
        Downloader.download();
        logger.log(LogLevel.DEBUG, req.argBuilder(this).toString(), true);

        try {
            ProcessBuilder builder = new ProcessBuilder(req.argBuilder(this));
            builder.directory(new File("bin").getAbsoluteFile());
            builder.redirectErrorStream(true);
            if (logger.enableDebug) {
                builder.inheritIO();
            }
            Process p = builder.start();
            int exitCode = p.waitFor();

            if (exitCode == 0 || exitCode == 1) {
                logger.log(LogLevel.INFO, "Operation successfully completed!", true);
            } else {
                logger.log(LogLevel.ERROR, "There was an error. Check exit code to know more.", true);
            }
            logger.log(LogLevel.DEBUG, "yt-dlp exit code: " + exitCode, true);

            logger.log(LogLevel.INFO, ConsoleColors.RESET, true);
            return exitCode;

        } catch (NullPointerException e) {
            logger.log(LogLevel.ERROR, "yt-dlp binary not found", true);
            logger.log(LogLevel.ERROR, "The program will exit", true);
            return -1;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void enableDebug() {
        logger.enableDebug = true;
        logger.log(LogLevel.DEBUG, "Starting with debug log enabled!", true);
    }

    public void updateYtDlp() {
        YtdlpRequest req = new YtdlpRequest();
        req.updateReq();
        this.execute(req);
        this.ytDlpVersion = ytDlpVersion();
    }

    private String ytDlpVersion() {
        YtdlpRequest req = new YtdlpRequest();
        req.versionReq();

        try {
            ProcessBuilder builder = new ProcessBuilder(req.argBuilder(this));
            builder.directory(new File("bin").getAbsoluteFile());
            builder.redirectErrorStream(true);
            if (logger.enableDebug) {
                builder.inheritIO();
            }
            Process p = builder.start();

            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
                String riga;
                while ((riga = reader.readLine()) != null) {
                    if (!sb.isEmpty()) {
                        sb.append("\n");
                    }
                    sb.append(riga);
                }
            }

            p.waitFor();
            return sb.toString().trim();

        } catch (IOException e) {
            Downloader.download();
            return ytDlpVersion();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String osPath() {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            return "bin/ytdlp.exe";

        } else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            return "bin/ytdlp";

        } else {
            throw new RuntimeException("Unknown/Unsupported OS");
        }
    }
}
