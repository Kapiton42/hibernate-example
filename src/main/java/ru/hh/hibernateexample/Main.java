package ru.hh.hibernateexample;

import org.hibernate.SessionFactory;
import ru.hh.hibernateexample.users.NewUser;
import ru.hh.hibernateexample.users.User;
import ru.hh.hibernateexample.users.UserDAO;
import ru.hh.hibernateexample.users.UserService;

class Main {

  public static void main(final String[] args) {

    final SessionFactory sessionFactory = getSessionFactory();
    try {

      final UserService userService = getUserService(sessionFactory);

      final NewUser newUser = new NewUser("Head", "Hunterz");
      System.out.println("persisting " + newUser);
      final User headHunterz = userService.save(newUser);
      System.out.println("users in db: " + userService.getAll());
      System.out.println();

      System.out.println("changing first name to 'Tail' via userService.update");
      final User tailHunterz = headHunterz.withFirstName("Tail");
      userService.update(tailHunterz);
      System.out.println("users in db: " + userService.getAll());
      System.out.println();

      System.out.println("changing first name back to 'Head' via userService.changeFirstName");
      userService.changeFirstName(tailHunterz.id, "Head");
      System.out.println("users in db: " + userService.getAll());
      System.out.println();

      System.out.println("deleting " + headHunterz);
      userService.delete(headHunterz.id);
      System.out.println("users in db: " + userService.getAll());

    } finally {
      sessionFactory.close();
    }
  }

  private static SessionFactory getSessionFactory() {
    return HibernateConfig.prod().buildSessionFactory();
  }

  private static UserService getUserService(final SessionFactory sessionFactory) {

    final UserDAO userDAO = new UserDAO(sessionFactory);
    return new UserService(sessionFactory, userDAO);
  }

  private Main() {
  }
}
