package com.am.design.development.utilities.utils;

import io.github.gaeqs.javayoutubedownloader.JavaYoutubeDownloader;
import io.github.gaeqs.javayoutubedownloader.decoder.MultipleDecoderMethod;
import io.github.gaeqs.javayoutubedownloader.stream.StreamOption;
import io.github.gaeqs.javayoutubedownloader.stream.YoutubeVideo;
import io.github.gaeqs.javayoutubedownloader.stream.download.StreamDownloader;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import static com.am.design.development.utilities.constants.StringConstants.HEX_CHARS;

public class VideoUrlUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoUrlUtils.class);

    @Deprecated
    public static File downloadVideo(String videoUrl, File destinationFolder) throws IOException {
        destinationFolder.mkdirs();

        String destinationFile = destinationFolder.getAbsolutePath() + File.separator + RandomStringUtils.random(40, HEX_CHARS);

        try {
            destinationFile = downloadYoutubeVideo(videoUrl, new File(destinationFile)).getAbsolutePath();
            LOGGER.info("Download completed successfully!");
        } catch (IOException e) {
            LOGGER.error("An error occurred: " + e.getMessage());
            throw e;
        }

        return new File(destinationFile);
    }

    @Deprecated
    public static File downloadYoutubeVideo(String url, File destinationFile) throws IOException {
        //Extracts and decodes all streams.
        YoutubeVideo video = JavaYoutubeDownloader.decodeOrNull(url, MultipleDecoderMethod.AND, "html", "embedded");
        //Gets the option with the greatest quality that has video and audio.
        StreamOption option = video.getStreamOptions().stream()
                .filter(target -> target.getType().hasVideo() && target.getType().hasAudio())
                .min(Comparator.comparingInt(o -> o.getType().getVideoQuality().ordinal())).orElse(null);
        //If there is no option, returns false.
        if (option == null)
            throw new RuntimeException("No video and audio stream found.");
        //Prints the option type.
        System.out.println(option.getType());
        //Creates the file. folder/title.extension
        File file = new File(destinationFile.getParentFile().getCanonicalPath(), destinationFile.getName() + "." + option.getType().getContainer().toString().toLowerCase());
        //Creates the downloader.
        StreamDownloader downloader = new StreamDownloader(option, file, null);
        downloader.run();
        return file;
    }
}