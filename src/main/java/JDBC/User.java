package JDBC;
/**
 * Created by kapiton on 06.03.16.
 */
import java.util.Objects;

public final class User {

    private Integer id;
    private String firstName;
    private String lastName;
    private int money;

    public static User create(final String firstName, final String lastName, final int money) {
        return new User(null, firstName, lastName, money);
    }

    public static User create(final String firstName, final String lastName) {
        return new User(null, firstName, lastName, 0);
    }

    static User existing(final int id, final String firstName, final String lastName, final int money) {
        return new User(id, firstName, lastName, money);
    }

    private User(final Integer id, final String firstName, final String lastName, final int money) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.money = money;
    }

    public Integer getId() {
        return id;
    }

    void setId(final int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;

        final User thatUser = (User) that;
        return Objects.equals(id, thatUser.id)
                && Objects.equals(firstName, thatUser.firstName)
                && Objects.equals(lastName, thatUser.lastName)
                && money == thatUser.getMoney();
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format(
                "%s{id=%d, firstName='%s', lastName='%s', money='%d'}",
                getClass().getSimpleName(), id, firstName, lastName, money
        );
    }
}
