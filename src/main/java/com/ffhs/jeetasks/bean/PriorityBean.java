package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.Priority;
import com.ffhs.jeetasks.service.PriorityService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequestScoped
public class PriorityBean {

    @Inject
    private PriorityService priorityService;

    public List<Priority> getPriorities() {
        return priorityService.findAllPriorities();
    }
}