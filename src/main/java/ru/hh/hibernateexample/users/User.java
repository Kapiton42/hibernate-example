package ru.hh.hibernateexample.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "users")
public class User extends AbstractUser {  // persisted user with id

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  public final int id;
  // TODO: UserId

  public User withFirstName(final String firstName) {
    return new User(id, firstName, lastName, creationDate);
  }

  // this constructor is only used to create user from NewUser before passing to session.save
  User(final String firstName, final String lastName, final Date creationDate) {
    this(-1, firstName, lastName, creationDate);  // another ugly hax with id=-1
  }

  private User(final int id, final String firstName, final String lastName, final Date creationDate) {
    super(firstName, lastName, creationDate);
    this.id = id;
  }

  /** for Hibernate only */
  @Deprecated
  User() {  // problem: somebody can use this constructor and create inconsistent instance
    id = -1;  // yet another ugly hax
  }

  @Override
  public boolean equals(final Object that) {
    if (this == that) return true;
    if (that == null || getClass() != that.getClass()) return false;
    if (!super.equals(that)) return false;

    final User user = (User) that;
    return id == user.id;
  }

  @Override
  public int hashCode() {
    return id;  // not perfect but super fast and close enough to be perfect :-)
  }

  @Override
  public String toString() {
    return String.format("%s{id=%d, %s}", getClass().getSimpleName(), id, super.fieldsToString());
  }
}
