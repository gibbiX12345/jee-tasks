package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.bean.LoginBean;
import com.ffhs.jeetasks.bean.TaskBean;
import com.ffhs.jeetasks.entity.Status;
import com.ffhs.jeetasks.entity.Task;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.io.Serializable;
import java.util.List;

@Stateless
public class TaskService implements Serializable {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;

    @Inject
    private LoginBean loginBean;

    public Task findTaskById(Long taskId) {
        return entityManager.createQuery("SELECT t FROM Task t WHERE t.taskId = :taskId", Task.class)
                .setParameter("taskId", taskId)
                .getSingleResult();
    }

    public List<Task> findAllTasksByListId(Long listId, String sortColumn, boolean ascending, TaskBean.TECHNICAL_LIST_TYPE type) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> query = cb.createQuery(Task.class);
        Root<Task> task = query.from(Task.class);

        Predicate listIdPredicate = cb.equal(task.get("taskList").get("listId"), listId);
        Predicate userIdPredicate = cb.equal(task.get("taskList").get("user").get("userId"), loginBean.getUser().getUserId());
        Predicate assignedUserIdPredicate = cb.equal(task.get("assignedUser").get("userId"), loginBean.getUser().getUserId());
        switch (type) {
            case CUSTOM_LIST:
                query.where(listIdPredicate);
                break;
            case ALL_TASKS:
                query.where(cb.or(userIdPredicate, assignedUserIdPredicate));
                break;
            case MY_ASSIGNED_TASKS:
                query.where(assignedUserIdPredicate);
                break;
        }
        Order order;
        if (sortColumn.contains(".")) {
            String object = sortColumn.substring(0, sortColumn.indexOf("."));
            String property = sortColumn.substring(sortColumn.indexOf(".") + 1);
            Join<Task, Object> objectJoin = task.join(object, JoinType.LEFT);
            order = ascending ? cb.asc(objectJoin.get(property)) : cb.desc(objectJoin.get(property));
        } else {
            order = ascending ? cb.asc(task.get(sortColumn)) : cb.desc(task.get(sortColumn));
        }
        query.orderBy(order);

        return entityManager.createQuery(query).getResultList();
    }

    public void insertModel(Task task) {
        entityManager.persist(task);
    }

    public void updateModel(Task task) {
        entityManager.merge(task);
    }

    public void deleteModel(Task task) {
        entityManager.remove(entityManager.merge(task));
    }
}
