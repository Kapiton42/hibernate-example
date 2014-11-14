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

    final NewUser newUser1 = new NewUser("first name 1", "last name 1");
    final NewUser newUser2 = new NewUser("first name 2", "last name 2");

    final User savedUser1 = userService.save(newUser1);
    final User savedUser2 = userService.save(newUser2);

    assertEquals(newUser1.firstName, savedUser1.firstName);
    assertEquals(newUser1.lastName, savedUser1.lastName);
    assertEquals(newUser1.creationDate, savedUser1.creationDate);
    assertEquals(savedUser1, userService.get(savedUser1.id).get());

    assertEquals(newUser2.firstName, savedUser2.firstName);
    assertEquals(newUser2.lastName, savedUser2.lastName);
    assertEquals(newUser2.creationDate, savedUser2.creationDate);
    assertEquals(savedUser2, userService.get(savedUser2.id).get());
  }

  @Test
  public void getShouldReturnUserById() throws Exception {

    final User savedUser = userService.save(new NewUser("first name 1", "last name 1"));

    final Optional<User> userFromDB = userService.get(savedUser.id);

    assertEquals(savedUser, userFromDB.get());
  }

  @Test
  public void getShouldReturnEmptyOptionalIfNoUserWithSuchId() throws Exception {

    final int nonExistentUserId = 123;

    final Optional<User> user = userService.get(nonExistentUserId);

    assertFalse(user.isPresent());
  }

  @Test
  public void getAllShouldReturnAllUsers() throws Exception {

    final User savedUser1 = userService.save(new NewUser("first name 1", "last name 1"));
    final User savedUser2 = userService.save(new NewUser("first name 2", "last name 2"));

    final Set<User> users = userService.getAll();

    assertEquals(ImmutableSet.of(savedUser1, savedUser2), users);
  }

  @Test
  public void changeFirstNameShouldChangeFirstName() throws Exception {

    final NewUser newUser = new NewUser("first name", "last name");
    final User user = userService.save(newUser);

    userService.changeFirstName(user.id, "new first name");

    final User userFromDB = userService.get(user.id).get();
    assertEquals("new first name", userFromDB.firstName);
    assertEquals(newUser.lastName, userFromDB.lastName);
  }

  @Test(expected = IllegalArgumentException.class)
  public void changeFirstNameShouldThrowIllegalArgumentExceptionIfNoUserWithSuchId() throws Exception {

    final int nonExistentUserId = 123;
    assertFalse(userService.get(nonExistentUserId).isPresent());

    userService.changeFirstName(nonExistentUserId, "new first name");
  }

  @Test
  public void deleteShouldDeleteUserById() throws Exception {

    final User user1 = userService.save(new NewUser("first name 1", "last name 1"));
    final User user2 = userService.save(new NewUser("first name 2", "last name 2"));

    userService.delete(user1.id);

    assertFalse(userService.get(user1.id).isPresent());
    assertTrue(userService.get(user2.id).isPresent());
  }
}
