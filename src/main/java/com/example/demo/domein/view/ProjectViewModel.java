package com.example.demo.domein.view;

public class ProjectViewModel {
    private long projectId;
    private long workedDays;

    public ProjectViewModel() {
    }

    public long getProjectId() {
        return this.projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getWorkedDays() {
        return this.workedDays;
    }

    public void setWorkedDays(long workedDays) {
        this.workedDays = workedDays;
    }
}
