package ru.hh.hibernateexample.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Integer id;  // problem: id is null for new user and not null for existing user
                       // seems that NewUser and PersistedUser are distinct classes
  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "creation_time", nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date creationDate;

  public User(final String firstName, final String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.creationDate = new Date();
  }

  /** for Hibernate only */
  @Deprecated
  User() {}  // problem: somebody can use this constructor and create inconsistent instance

  public Integer id() {
    return id;
  }

  // no setId, Hibernate uses reflection to set field

  public String firstName() {
    return firstName;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public String lastName() {
    return lastName;
  }

  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  public Date creationDate() {
    return creationDate;
  }

  // no setCreationDate

  @Override
  public boolean equals(final Object that) {
    if (this == that) return true;
    if (that == null || getClass() != that.getClass()) return false;

    final User thatUser = (User) that;
    if (!creationDate.equals(thatUser.creationDate)) return false;
    if (firstName != null ? !firstName.equals(thatUser.firstName) : thatUser.firstName != null) return false;
    if (id != null ? !id.equals(thatUser.id) : thatUser.id != null) return false;
    //noinspection RedundantIfStatement
    if (lastName != null ? !lastName.equals(thatUser.lastName) : thatUser.lastName != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + creationDate.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format("%s{id=%d, firstName='%s', lastName='%s', creationDate='%s'}",
            getClass().getSimpleName(), id, firstName, lastName, creationDate);
  }
}
