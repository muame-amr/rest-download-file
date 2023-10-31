package com.example.restdownloadfile;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/download")
public class DownloadController {

    private final String fileBasePath = "download-dir/";

    @GetMapping("/{filename:.+}")
    public ResponseEntity downloadFileFromLocal(@PathVariable String filename) {
        Path path = Paths.get(fileBasePath + filename);
        Resource resource = null;

        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        assert resource != null;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping(value = "/zip-download", produces = "application/zip")
    public void zipDownload(@RequestParam List<String> filename, HttpServletResponse response) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        for (String file : filename) {
            FileSystemResource resource = new FileSystemResource(fileBasePath + file);
            ZipEntry zipEntry = new ZipEntry(Objects.requireNonNull(resource.getFilename()));
            zipEntry.setSize(resource.contentLength());
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(resource.getInputStream(), zipOut);
            zipOut.closeEntry();
        }
        zipOut.finish();
        zipOut.close();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String zipFileName = LocalDate.now().format(formatter);

        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + zipFileName + "\"");
    }

    // Use this
    @GetMapping(value = "/zip", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity zipping(@RequestParam List<String> filename, HttpServletResponse response) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String zipFileName = LocalDate.now().format(formatter) + ".zip";

        ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);

        for (String file : filename) {
            FileSystemResource resource = new FileSystemResource(fileBasePath + file);
            byte[] fileBytes = resource.getInputStream().readAllBytes();
            ZipEntry zipEntry = new ZipEntry(Objects.requireNonNull(resource.getFilename()));
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(fileBytes, zipOut);
            zipOut.closeEntry();
        }
        zipOut.finish();
        zipOut.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipFileName + "\"")
                .body(byteArrayOutputStream.toByteArray());
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity zipping2(@RequestBody FilenameDTO filename) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String zipFileName = LocalDate.now().format(formatter) + ".zip";

        ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);

        for (String file : filename.getFiles()) {
            FileSystemResource resource = new FileSystemResource(fileBasePath + file);
            byte[] fileBytes = resource.getInputStream().readAllBytes();
            ZipEntry zipEntry = new ZipEntry(Objects.requireNonNull(resource.getFilename()));
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(fileBytes, zipOut);
            zipOut.closeEntry();
        }

        zipOut.finish();
        zipOut.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipFileName + "\"")
                .body(byteArrayOutputStream.toByteArray());
    }
}
