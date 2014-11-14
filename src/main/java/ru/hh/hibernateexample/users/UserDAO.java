package ru.hh.hibernateexample.users;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class UserDAO {

  private final SessionFactory sessionFactory;

  public UserDAO(final SessionFactory sessionFactory) {
    this.sessionFactory = requireNonNull(sessionFactory);
  }

  public User save(final NewUser newUser) {

    // no more runtime exceptions, thanks to inheritance :-)

    final User userWithId = new User(newUser.firstName, newUser.lastName, newUser.creationDate);
    session().save(userWithId); // see also saveOrUpdate and persist
    return userWithId;
  }

  public Optional<User> get(final int userId) {
    final User user = (User) session().get(User.class, userId);
    // or session.load, load throws exception if entity with such id not found
    return Optional.ofNullable(user);
  }

  public Set<User> getAll() {
    final Criteria criteria = session().createCriteria(User.class); // Criteria query
     // or session().createQuery("FROM User"); // HQL query
    @SuppressWarnings("unchecked")
    final List<User> users = criteria.list();
    return new HashSet<>(users);
  }

  public void update(final User user) {
    session().update(user);
    // session.update throws exception if current session already has User with same id
    // session.merge does not throw exception
    // remember that update and merge is for detached entities only
  }

  public void delete(final int userId) {
    session().createQuery("DELETE User WHERE id = :id") // HQL
            .setInteger("id", userId)
            .executeUpdate();
    // see also session().delete(user);
    // but first you will need to get this user from DB
    // also be aware that hibernate silently ignores the fact that user may not have id, this most likely an error
}

  private Session session() {
    return sessionFactory.getCurrentSession();
    // or sessionFactory.openSession(), but do not forget to close it
    // try-with-resource won't work because Session does not implement Autocloseable
  }
}
