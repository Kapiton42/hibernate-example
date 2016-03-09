package JDBC;


import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.*;

/**
 * Created by kapiton on 07.03.16.
 */

public class UserServiceTest {

    private static UserService userService;

    @Test
    public void testWithdrawMoneyFromUser() throws Exception {
        User user = User.create("ivan", "ivanov", 1000);
        userService.insert(user);

        userService.withdrawMoneyFromUser(user.getId(), 100);
        assertEquals(900, userService.get(user.getId()).get().getMoney());
    }

    @BeforeClass
    public static void setUpDBTestBaseClass() throws Exception {
        ApplicationContext applicationContext = JDBCTestConfig.createApplicationContext();
        userService = applicationContext.getBean(UserService.class);

    }
}