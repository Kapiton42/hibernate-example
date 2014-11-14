package ru.hh.hibernateexample.users;

import java.util.Date;

public class NewUser extends AbstractUser {  // new user that does not have id yet

  public NewUser(final String firstName, final String lastName) {
    super(firstName, lastName, new Date());
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + '{' + super.fieldsToString() + '}';
  }
}
