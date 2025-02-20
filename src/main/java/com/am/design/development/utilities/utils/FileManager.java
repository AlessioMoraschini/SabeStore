package com.am.design.development.utilities.utils;

import com.am.design.development.utilities.impl.AmTaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileManager.class);


    @Value("${remote_retriever.wait_ms_before_delete:30000}")
    private Integer waitMsBeforeDelete;

    @Autowired
    private AmTaskExecutor amTaskExecutor;


    /**
     * Deletes the file after a delay with async thread.
     *
     * @param file
     * @param waitMsBeforeDelete : The time in milliseconds to wait before deleting the file. Use null to use default value.
     */
    public void deleteFileAfterDelay(File file, Integer waitMsBeforeDelete) {
        amTaskExecutor.getExecutor().execute(() -> {
            var delay = waitMsBeforeDelete == null ? this.waitMsBeforeDelete : waitMsBeforeDelete;
            try {
                LOGGER.info("Will delete {} in {} seconds: ", file.getAbsolutePath(), waitMsBeforeDelete.doubleValue() / 1000d);
                Thread.sleep(delay);
                var deleted = file.delete();
                LOGGER.info("File deleted: " + deleted);

            } catch (Exception e) {
                LOGGER.info("Exception thrown: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        });
    }
}
