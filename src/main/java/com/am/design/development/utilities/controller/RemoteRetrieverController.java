package com.am.design.development.utilities.controller;

import com.am.design.development.utilities.facade.RemoteRetrieverFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("remoteRetriever")
public class RemoteRetrieverController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteRetrieverController.class);

    @Autowired
    private RemoteRetrieverFacade remoteRetrieverFacade;

    // TODO replace the method below with something more useful, youtube api changed and libs not working anymore.
    // E.g. you can add a method to upload and convert a video, returning back the converted one.
    @GetMapping("downloadVideo")
    @Deprecated
    public ResponseEntity<FileSystemResource> downloadVideo(@RequestParam(name = "videoUrl") String videoUrl) {
        try {
            LOGGER.info("Downloading video from: " + videoUrl);
            var result = remoteRetrieverFacade.downloadVideo(videoUrl);
            LOGGER.info("Video downloaded into: " + result.getAbsolutePath());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + result.getName());
            return new ResponseEntity<FileSystemResource>(new FileSystemResource(result), headers, HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("An error occurred: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

}
