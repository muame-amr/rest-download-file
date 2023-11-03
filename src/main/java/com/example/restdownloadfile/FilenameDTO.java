package com.example.restdownloadfile;

import java.time.LocalDate;
import java.util.List;

public class FilenameDTO {

    private String court;
    private LocalDate filterDate;
    private int page;
    private List<String> files;

    public FilenameDTO() {
    }

    public FilenameDTO(String court, LocalDate filterDate, int page, List<String> files) {
        this.court = court;
        this.filterDate = filterDate;
        this.page = page;
        this.files = files;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getCourt() {
        return court;
    }

    public int getPage() {
        return page;
    }

    public LocalDate getFilterDate() {
        return filterDate;
    }

    public void setFilterDate(LocalDate filterDate) {
        this.filterDate = filterDate;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
