package com.ffhs.jeetasks.bean.data;

import com.ffhs.jeetasks.entity.Priority;
import com.ffhs.jeetasks.service.PriorityService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

/**
 * Bean responsible for managing priority-related operations in the application.
 */
@Named
@RequestScoped
public class PriorityBean {

    @Inject
    private PriorityService priorityService;

    /**
     * Retrieves all priorities available in the system.
     *
     * @return A list of all {@link Priority} entities.
     */
    public List<Priority> getPriorities() {
        return priorityService.findAllPriorities();
    }
}
