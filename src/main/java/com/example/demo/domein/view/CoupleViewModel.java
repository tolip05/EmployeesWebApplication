package com.example.demo.domein.view;

import java.util.ArrayList;
import java.util.List;

public class CoupleViewModel {
    private long firstEmplId;
    private long secondEmplId;
    private long totalDuration;
    private List<ProjectViewModel> projects;

    public CoupleViewModel() {
        this.projects = new ArrayList<>();
    }

    public long getFirstEmplId() {
        return this.firstEmplId;
    }

    public void setFirstEmplId(long firstEmplId) {
        this.firstEmplId = firstEmplId;
    }

    public long getSecondEmplId() {
        return this.secondEmplId;
    }

    public void setSecondEmplId(long secondEmplId) {
        this.secondEmplId = secondEmplId;
    }

    public long getTotalDuration() {
        return this.totalDuration;
    }

    public void setTotalDuration(long totalDuration) {
        this.totalDuration = totalDuration;
    }

    public List<ProjectViewModel> getProjects() {
        return this.projects;
    }

    public void setProjects(List<ProjectViewModel> projects) {
        this.projects = projects;
    }
}
