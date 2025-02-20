package com.am.design.development.utilities.impl;

import com.am.design.development.utilities.facade.RemoteRetrieverFacade;
import com.am.design.development.utilities.utils.FileManager;
import com.am.design.development.utilities.utils.VideoConverter;
import com.am.design.development.utilities.utils.VideoUrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class RemoteRetrieverImpl implements RemoteRetrieverFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteRetrieverFacade.class);

    @Value("${remote_retriever.destination_folder:/tmp}")
    private File destinationFolder;

    @Autowired
    private FileManager fileManager;


    @Override
    @Deprecated
    public File downloadVideo(String videoUrl) throws IOException {
        File downloadedVideo = null;
        File convertedVideo = null;
        try {
            downloadedVideo = VideoUrlUtils.downloadVideo(videoUrl, destinationFolder);
            convertedVideo = new File(downloadedVideo.getAbsolutePath() + ".mp4");
            VideoConverter.convertToMp4(downloadedVideo, convertedVideo);
        } catch (Throwable e) {
            LOGGER.error("An error occurred: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            fileManager.deleteFileAfterDelay(downloadedVideo, 0);
            fileManager.deleteFileAfterDelay(convertedVideo, null);
        }

        return downloadedVideo;
    }

}
