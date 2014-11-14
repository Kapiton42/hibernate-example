package ru.hh.hibernateexample.users;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import ru.hh.hibernateexample.HibernateTestBase;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserServiceTest extends HibernateTestBase {

  private static final UserDAO userDAO = new UserDAO(sessionFactory);
  private static final UserService userService = new UserService(sessionFactory, userDAO);

  @Test
  public void saveShouldInsertUserInDBAndReturnUserWithId() throws Exception {

    final User user1 = new User("first name 1", "last name 1");
    final User user2 = new User("first name 2", "last name 2");

    userService.save(user1);
    userService.save(user2);

    assertEquals("first name 1", user1.firstName());
    assertEquals("last name 1", user1.lastName());
    assertEquals(user1, userService.get(user1.id()).get());

    assertEquals("first name 2", user2.firstName());
    assertEquals("last name 2", user2.lastName());
    assertEquals(user2, userService.get(user2.id()).get());
  }

  @Test
  public void getShouldReturnUserById() throws Exception {

    final User user = new User("first name 1", "last name 1");
   userService.save(user);

    final Optional<User> userFromDB = userService.get(user.id());

    assertEquals(user, userFromDB.get());
  }

  @Test
  public void getShouldReturnEmptyOptionalIfNoUserWithSuchId() throws Exception {

    final int nonExistentUserId = 123;

    final Optional<User> user = userService.get(nonExistentUserId);

    assertFalse(user.isPresent());
  }

  @Test
  public void getAllShouldReturnAllUsers() throws Exception {

    final User user1 = new User("first name 1", "last name 1");
    final User user2 = new User("first name 2", "last name 2");
    userService.save(user1);
    userService.save(user2);

    final Set<User> users = userService.getAll();

    assertEquals(ImmutableSet.of(user1, user2), users);
  }

  @Test
  public void changeFirstNameShouldChangeFirstName() throws Exception {

    final User user = new User("first name", "last name");
    userService.save(user);

    userService.changeFirstName(user.id(), "new first name");

    final User userFromDB = userService.get(user.id()).get();
    assertEquals("new first name", userFromDB.firstName());
    assertEquals(user.lastName(), userFromDB.lastName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void changeFirstNameShouldThrowIllegalArgumentExceptionIfNoUserWithSuchId() throws Exception {

    final int nonExistentUserId = 123;
    assertFalse(userService.get(nonExistentUserId).isPresent());

    userService.changeFirstName(nonExistentUserId, "new first name");
  }

  @Test
  public void deleteShouldDeleteUserById() throws Exception {

    final User user1 = new User("first name 1", "last name 1");
    final User user2 = new User("first name 2", "last name 2");
    userService.save(user1);
    userService.save(user2);

    userService.delete(user1.id());

    assertFalse(userService.get(user1.id()).isPresent());
    assertTrue(userService.get(user2.id()).isPresent());
  }
}
