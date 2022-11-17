package net.kiwox.manager.dst.service.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import net.kiwox.manager.dst.enums.Parameter;
import net.kiwox.manager.dst.service.interfaces.IParameterService;
import net.kiwox.manager.dst.service.interfaces.IScreenshotUploadTaskService;

@Service
public class ScreenshotUploadTaskServiceImpl implements IScreenshotUploadTaskService, FilenameFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotUploadTaskServiceImpl.class);

    @Autowired
    private IParameterService parameterService;

    @Value("${screenshot.dir}")
    private String screenshotDir;

    private DateFormat dateFormat;

    public ScreenshotUploadTaskServiceImpl() {
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
    }

    @Override
    public void run() {
        LOGGER.info("Starting screenshot upload task");
        long ts = System.currentTimeMillis();

        File dir = new File(screenshotDir);
        if (!dir.exists() || !dir.isDirectory()) {
            LOGGER.info("Path [{}] is not a directory", screenshotDir);
            return;
        }

        List<File> filesToDelete = new LinkedList<>();
        for (File file : dir.listFiles(this)) {
            HttpPost post = new HttpPost(parameterService.getString(Parameter.SCREENSHOT_UPLOAD_URL));
            MultipartEntityBuilder multipartBuilder = MultipartEntityBuilder.create();
            multipartBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartBuilder.addBinaryBody("file", file, ContentType.create(MediaType.IMAGE_PNG_VALUE), file.getName());
            post.setEntity(multipartBuilder.build());
            try (CloseableHttpClient client = HttpClients.createDefault();
                 CloseableHttpResponse response = client.execute(post)) {
                StatusLine status = response.getStatusLine();
                if (status == null) {
                    LOGGER.error("[DST-SCREENSHOT-006] Error uploading screenshot [{}]: No status line received.", file.getAbsolutePath());
                } else if (status.getStatusCode() == HttpStatus.SC_OK) {
                    LOGGER.debug("Successfully uploaded file [{}]", file.getAbsolutePath());
                    filesToDelete.add(file);
                } else {
                    LOGGER.error("[DST-SCREENSHOT-006] Error uploading screenshot [{}]: Received status [{}]", file.getAbsolutePath(), status.getStatusCode());
                }
            } catch (IOException e) {
                String err = "[DST-SCREENSHOT-007] Error uploading screenshot [" + file.getAbsolutePath() + "]";
                LOGGER.error(err, e);
            }
        }

        for (File file : filesToDelete) {
            try {
                Files.deleteIfExists(file.toPath());
            } catch (IOException e) {
                String err = "[DST-SCREENSHOT-008] Error deleting file [" + file.getAbsolutePath() + "]";
                LOGGER.error(err, e);
            }
        }

        ts = System.currentTimeMillis() - ts;
        LOGGER.info("Finished screenshot upload task in {}ms", ts);
    }

    @Override
    public boolean accept(File dir, String name) {
        String ext = ".png";
        if (!name.endsWith(ext)) {
            return false;
        }

        String[] names = name.substring(0, name.length() - ext.length()).split("\\s+");
        int n = 4;
        if (names.length != n) {
            return false;
        }

        try {
            Long.parseLong(names[0]);
        } catch (NumberFormatException e) {
            return false;
        }

        try {
            dateFormat.parse(names[n - 1]);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

}
