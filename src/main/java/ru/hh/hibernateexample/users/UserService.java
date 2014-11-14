package ru.hh.hibernateexample.users;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class UserService {

  private final SessionFactory sessionFactory;
  private final UserDAO userDAO;

  public UserService(final SessionFactory sessionFactory, final UserDAO userDAO) {
    this.sessionFactory = requireNonNull(sessionFactory);
    this.userDAO = requireNonNull(userDAO);
  }

  public User save(final NewUser newUser) {
    return inTransaction(() -> userDAO.save(newUser));
  }

  public Optional<User> get(final int userId) {
    return inTransaction(() -> userDAO.get(userId));
  }

  public Set<User> getAll() {
    return inTransaction(userDAO::getAll);
  }

  public void update(final User user) {
    inTransaction(() -> userDAO.update(user));
  }

  public void changeFirstName(final int userId, final String firstName) {
    inTransaction(() -> {
      final Optional<User> optionalUser = userDAO.get(userId);
      if (!optionalUser.isPresent()) {
        throw new IllegalArgumentException("there is no user with id " + userId);
      }
      final User user = optionalUser.get().withFirstName(firstName);
      sessionFactory.getCurrentSession().merge(user);
      // need to merge because hibernate does not know anything about new user
      // there is possibility of deadlock if two transactions get one user and then try to update it
      // to avoid it we can 'select for update' in userDAO.get above
      // also we can implement UserDAO.setFirstName(int userId, String firstName) that does 1 query instead of 2 (get, update on commit)
    });
  }

  public void delete(final int userId) {
    inTransaction(() -> userDAO.delete(userId));
  }

  private <T> T inTransaction(final Supplier<T> supplier) {
    final Optional<Transaction> transaction = beginTransaction();
    try {
      final T result = supplier.get();
      transaction.ifPresent(Transaction::commit);
      return result;
    } catch (RuntimeException e) {
      transaction.ifPresent(Transaction::rollback);
      throw e;
    }
  }

  private void inTransaction(final Runnable runnable) {
    inTransaction(() -> {
      runnable.run();
      return null;
    });
  }

  private Optional<Transaction> beginTransaction() {
    final Transaction transaction = sessionFactory.getCurrentSession().getTransaction();
    if (!transaction.isActive()) {
      transaction.begin();
      return Optional.of(transaction);
    }
    return Optional.empty();
  }
}
