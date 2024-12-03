package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.Status;
import com.ffhs.jeetasks.service.StatusService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequestScoped
public class StatusBean {

    @Inject
    private StatusService statusService;

    public List<Status> getStatuses() {
        return statusService.findAllStatuses();
    }
}