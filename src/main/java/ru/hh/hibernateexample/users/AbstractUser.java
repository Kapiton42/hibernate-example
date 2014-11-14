package ru.hh.hibernateexample.users;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

@MappedSuperclass  // if you do not mark parent as @MappedSuperclass hibernate will not see its' fields in children
abstract class AbstractUser {  // parent for NewUser and PersistentUser (or just User)
                               // warn: inheritance of hibernate entities is not common practice
  @Column(name = "first_name")
  public final String firstName;  // warn: immutable entities for hibernate is not common practice

  @Column(name = "last_name")
  public final String lastName;

  @Column(name = "creation_time", nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  public final Date creationDate;

  // protected constructor only for children
  protected AbstractUser(final String firstName, final String lastName, final Date creationDate) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.creationDate = checkNotNull(creationDate);
  }

  /** for Hibernate only */
  @Deprecated
  AbstractUser() {
    firstName = null;  // ugly hax
    lastName = null;
    creationDate = null;
  }

  @Override
  public boolean equals(final Object that) {
    if (this == that) return true;
    if (that == null || getClass() != that.getClass()) return false;

    final AbstractUser abstractUser = (AbstractUser) that;
    if (!creationDate.equals(abstractUser.creationDate)) return false;
    if (firstName != null ? !firstName.equals(abstractUser.firstName) : abstractUser.firstName != null) return false;
    //noinspection RedundantIfStatement
    if (lastName != null ? !lastName.equals(abstractUser.lastName) : abstractUser.lastName != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = firstName != null ? firstName.hashCode() : 0;
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + creationDate.hashCode();
    return result;
  }

  protected String fieldsToString() {
    return String.format("firstName='%s', lastName='%s', creationDate='%s'", firstName, lastName, creationDate);
  }
}
