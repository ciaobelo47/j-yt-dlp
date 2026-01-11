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

    public static void execute() {
        Downloader.download();
        logger.log(LogLevel.DEBUG, YtdlpRequest.argBuilder().toString(), true);

        try {
            ProcessBuilder builder = new ProcessBuilder(YtdlpRequest.argBuilder());
            builder.directory(new File("bin").getAbsoluteFile());
            builder.redirectErrorStream(true);
            if (logger.enableDebug) {
                builder.inheritIO();
            }
            Process p = builder.start();
            p.waitFor();

            logger.log(LogLevel.INFO, "The file has been successfully downloaded!", true);
            logger.log(LogLevel.DEBUG, "yt-dlp exit code: " + p.exitValue(), true);
            System.exit(0);

            logger.log(LogLevel.INFO, ConsoleColors.RESET, true);
        } catch (NullPointerException e) {
            logger.log(LogLevel.ERROR, "yt-dlp binary not found", true);
            logger.log(LogLevel.ERROR, "The program will exit", true);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void enableDebug() {
        logger.enableDebug = true;
        logger.log(LogLevel.DEBUG, "Starting with debug log enabled!", true);
    }
 }
