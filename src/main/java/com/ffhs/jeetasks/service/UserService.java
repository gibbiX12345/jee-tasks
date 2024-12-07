package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing user-related operations.
 * Provides methods to handle user data retrieval and registration.
 */
@Stateless
public class UserService implements Serializable {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;

    /**
     * Retrieves all users from the database.
     *
     * @return A list of {@link User} entities.
     */
    public List<User> findAllUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    /**
     * Finds a user by their email address.
     *
     * @param email The email address to search for.
     * @return An {@link Optional} containing the found user, or empty if no user is found.
     */
    public Optional<User> findUserByEmail(String email) {
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    /**
     * Finds a user by their unique ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The {@link User} entity with the specified ID.
     */
    public User findUserById(Long userId) {
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.userId = :userId", User.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    /**
     * Registers a new user by persisting their data to the database.
     *
     * @param user The {@link User} entity to be registered.
     */
    public void registerUser(User user) {
        entityManager.persist(user);
    }
}
