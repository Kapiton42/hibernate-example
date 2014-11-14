package ru.hh.hibernateexample;

import org.hibernate.SessionFactory;
import ru.hh.hibernateexample.users.User;
import ru.hh.hibernateexample.users.UserDAO;
import ru.hh.hibernateexample.users.UserService;

class Main {

  public static void main(final String[] args) {

    final SessionFactory sessionFactory = createSessionFactory();
    try {

      final UserService userService = createUserService(sessionFactory);

      final User headHunterz = new User("Head", "Hunterz");
      System.out.println("persisting " + headHunterz);
      userService.save(headHunterz);
      System.out.println("users in db: " + userService.getAll());
      System.out.println();

      System.out.println("changing first name to 'Tail' via userService.update");
      headHunterz.setFirstName("Tail");
      userService.update(headHunterz);
      System.out.println("users in db: " + userService.getAll());
      System.out.println();

      System.out.println("changing first name back to 'Head' via userService.changeFirstName");
      userService.changeFirstName(headHunterz.id(), "Head");
      System.out.println("users in db: " + userService.getAll());
      System.out.println();

      System.out.println("deleting " + headHunterz);
      userService.delete(headHunterz.id());
      System.out.println("users in db: " + userService.getAll());

    } finally {
      sessionFactory.close();
    }
  }

  private static SessionFactory createSessionFactory() {
    return HibernateConfigFactory.prod().buildSessionFactory();
  }

  private static UserService createUserService(final SessionFactory sessionFactory) {
    final UserDAO userDAO = new UserDAO(sessionFactory);
    return new UserService(sessionFactory, userDAO);
  }

  private Main() {
  }
}
