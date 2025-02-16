package ru.kata.spring.boot_security.demo.DAO;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(Role role) {
        entityManager.persist(role);
    }

    @Override
    @Transactional(readOnly = true)
    public Role findByName(String name) {
        return entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                .setParameter("name", name).getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return entityManager.createQuery("from Role", Role.class).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findByIds(List<Long> roleIds) {
        return entityManager.createQuery("SELECT r FROM Role r WHERE r.id IN :ids", Role.class)
                .setParameter("ids", roleIds)
                .getResultList();
    }
}
