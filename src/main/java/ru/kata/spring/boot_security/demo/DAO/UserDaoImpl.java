package ru.kata.spring.boot_security.demo.DAO;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;


@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> index() {
        return entityManager.createQuery("from User", User.class).getResultList();
    }


    @Override
    public Optional<User>  show(int id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }


    @Override
    public void update(int id, User user) {
        entityManager.merge(user);
    }

    @Override
    public void delete(int id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        List<User> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (findUserByUsername(username).isPresent()) {
            User user = findUserByUsername(username).get();
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(), user.getPassword(), user.getAuthorities());
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
