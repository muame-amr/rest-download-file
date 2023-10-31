package com.example.restdownloadfile;

import java.util.List;

public class FilenameDTO {
    private List<String> files;

    public FilenameDTO() {
    }

    public FilenameDTO(List<String> files) {
        this.files = files;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
