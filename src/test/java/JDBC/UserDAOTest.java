package JDBC;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import Configuration.BaseConfiguration;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by kapiton on 06.03.16.
 */
public class UserDAOTest {

    private static UserDAO userDAO;
    private static Set<User> users;

    @BeforeClass
    public static void setUpDBTestBaseClass() throws Exception {
        ApplicationContext applicationContext = JDBCTestConfig.createApplicationContext();
        userDAO = applicationContext.getBean(UserDAO.class);
    }

    @Before
    public void setUpTest() {
        users = new HashSet<>();

        User user = User.create("ivan", "ivanov", 100);
        users.add(user);
        user = User.create("petya", "petrov", 300);
        users.add(user);
        users.forEach(userDAO::insert);
    }

    @After
    public void tearDownTest() {
        userDAO.clearDatabase();
    }

    @Test
    public void testInsertGet() throws Exception {
        User user = users.iterator().next();
        assertTrue(user.equals(userDAO.get(user.getId()).get()));
    }

    @Test
    public void testGetAll() throws Exception {
        Set<User> usersGet = userDAO.getAll();
        for(User user: usersGet) {
            System.out.println(user.toString());
        }
        assertTrue(usersGet.equals(users));
    }

    @Test
    public void testUpdate() throws Exception {
        User user = users.iterator().next();
        user.setMoney(500);
        userDAO.update(user);
        assertTrue(user.equals(userDAO.get(user.getId()).get()));
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testDelete() throws Exception {
        User user = users.iterator().next();
        userDAO.delete(user.getId());
        assertTrue(user.equals(userDAO.get(user.getId()).get()));
    }
}