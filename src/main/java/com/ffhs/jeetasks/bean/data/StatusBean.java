package com.ffhs.jeetasks.bean.data;

import com.ffhs.jeetasks.entity.Status;
import com.ffhs.jeetasks.service.StatusService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

/**
 * Bean responsible for managing status-related operations in the application.
 */
@Named
@RequestScoped
public class StatusBean {

    @Inject
    private StatusService statusService;

    /**
     * Retrieves all statuses available in the system.
     *
     * @return A list of all {@link Status} entities.
     */
    public List<Status> getStatuses() {
        return statusService.findAllStatuses();
    }
}
