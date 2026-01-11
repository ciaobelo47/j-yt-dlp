package me.mailo.jytdlp.download;

import me.mailo.jytdlp.wrapper.Ytdlp;
import me.mailo.jytdlp.wrapper.YtdlpRequest;
import me.mailo.willylogger.LogLevel;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Parent Class that starts the download of ytdlp's binary
 */
public class Downloader {
    /**
     * Downloads the binary of your OS.
     * <br>
     * <b>P.S.</b> MacOS not supported
     */
    public static void download() {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            Ytdlp.logger.log(LogLevel.INFO, "OS: " + System.getProperty("os.name"), true);

            File winBin = windowsDownload(new File("bin/ytdlp.exe"));
            if (winBin == null) {
                Ytdlp.logger.log(LogLevel.WARN, "The binary was successfully downloaded but it doesn't work. Check the log for errors.", true);
            } else {
                YtdlpRequest.ytdlpBin = winBin;
            }

        } else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            Ytdlp.logger.log(LogLevel.INFO, "OS: " + System.getProperty("os.name"), true);

            File linBin = linuxDownload(new File("bin/ytdlp"));
            if (linBin == null) {
                Ytdlp.logger.log(LogLevel.WARN, "The binary was successfully downloaded but it doesn't work. Check the log for errors.", true);
            } else {
                YtdlpRequest.ytdlpBin = linBin;
            }

        } else {
            throw new RuntimeException("Unknown/Unsupported OS");
        }
    }

    private static File linuxDownload(File destination) {
        try {
            URL downloadUrl = new URL("https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp_linux");

            // download phase
            if (!destination.exists()) {
                Ytdlp.logger.log(LogLevel.WARN, "yt-dlp binary not found, trying to download it...", true);

                ReadableByteChannel rbc = Channels.newChannel(downloadUrl.openStream());
                FileOutputStream fos = new FileOutputStream(destination);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                Ytdlp.logger.log(LogLevel.INFO, "yt-dlp binary successfuly downloaded at: " + destination.getAbsolutePath(), true);

                fos.close();
            }

            //testing phase
            Ytdlp.logger.log(LogLevel.WARN, "Testing the correct execution of the binary...", true);

            if (destination.setExecutable(true)) {
                ProcessBuilder pb = new ProcessBuilder(destination.getAbsolutePath(), "--version");
                pb.directory(new File("bin").getAbsoluteFile());
                pb.redirectErrorStream(true);
                Process p = pb.start();
                p.waitFor();

                Ytdlp.logger.log(LogLevel.DEBUG, "The exit value of yt-dlp is: " + p.exitValue(), true);
                if (p.exitValue() != 0) {
                    switch (p.exitValue()) {
                        case 100:
                            Ytdlp.logger.log(LogLevel.WARN, "The binary works, but has to be restarted", true);
                            break;
                        case 101:
                        case 2:
                        case 1:
                        default:
                            Ytdlp.logger.log(LogLevel.ERROR, "The binary doesn't work, please delete it and restart the program.", true);
                    }

                    return null;
                } else {
                    Ytdlp.logger.log(LogLevel.INFO, "The binary works correctly", true);
                    return destination;
                }
            } else {
                Ytdlp.logger.log(LogLevel.ERROR, "Can't apply execution permissions to the binary. Retry or apply them yourself.", true);
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static File windowsDownload(File destination) {
        try {
            URL downloadUrl = new URL("https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp.exe");

            // download phase
            if (!destination.exists()) {
                Ytdlp.logger.log(LogLevel.WARN, "yt-dlp binary not found, trying to download it...", true);

                ReadableByteChannel rbc = Channels.newChannel(downloadUrl.openStream());
                FileOutputStream fos = new FileOutputStream(destination);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                Ytdlp.logger.log(LogLevel.INFO, "yt-dlp binary successfuly downloaded at: " + destination.getAbsolutePath(), true);
            }

            //testing phase
            Ytdlp.logger.log(LogLevel.WARN, "Testing the correct execution of the binary...", true);

            ProcessBuilder pb = new ProcessBuilder(destination.getAbsolutePath(), "--version");
            pb.directory(new File("bin").getAbsoluteFile());
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.waitFor();

            Ytdlp.logger.log(LogLevel.DEBUG, "The exit value of yt-dlp is: " + p.exitValue(), true);
            if (p.exitValue() != 0) {
                switch (p.exitValue()) {
                    case 100:
                        Ytdlp.logger.log(LogLevel.WARN, "The binary works, but has to be restarted", true);
                        break;
                    case 101:
                    case 2:
                    case 1:
                    default:
                        Ytdlp.logger.log(LogLevel.ERROR, "The binary doesn't work, please delete it and restart the program.", true);
                }

                return null;
            } else {
                Ytdlp.logger.log(LogLevel.INFO, "The binary works correctly", true);
                return destination;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
