package me.mailo.jytdlp.wrapper;

import me.mailo.jytdlp.download.Downloader;
import me.mailo.willylogger.ConsoleColors;
import me.mailo.willylogger.LogLevel;
import me.mailo.willylogger.WillyLogger;

import java.io.*;

/**
 * Main Class of the library. Contains the execute method from which starts all other classes. That's why is the main class :)
 */
public class Ytdlp {
    public static WillyLogger logger = new WillyLogger("j-yt-dlp");

    /**
     * The location of ytdlp's binary, usually downloaded from {@link me.mailo.jytdlp.download.Downloader} class
     */
    public static File ytdlpBin;

    public static int execute(YtdlpRequest req) {
        Downloader.download();
        logger.log(LogLevel.DEBUG, req.argBuilder().toString(), true);

        try {
            ProcessBuilder builder = new ProcessBuilder(req.argBuilder());
            builder.directory(new File("bin").getAbsoluteFile());
            builder.redirectErrorStream(true);
            if (logger.enableDebug) {
                builder.inheritIO();
            }
            Process p = builder.start();
            p.waitFor();

            if (p.exitValue() == 0 || p.exitValue() == 1) {
                logger.log(LogLevel.INFO, "The file has been successfully downloaded!", true);
            } else {
                logger.log(LogLevel.ERROR, "There was an error while downloading the file. Check exit code to know more.", true);
            }
            logger.log(LogLevel.DEBUG, "yt-dlp exit code: " + p.exitValue(), true);

            logger.log(LogLevel.INFO, ConsoleColors.RESET, true);
            return p.exitValue();

        } catch (NullPointerException e) {
            logger.log(LogLevel.ERROR, "yt-dlp binary not found", true);
            logger.log(LogLevel.ERROR, "The program will exit", true);
            return -1;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void enableDebug() {
        logger.enableDebug = true;
        logger.log(LogLevel.DEBUG, "Starting with debug log enabled!", true);
    }
}
