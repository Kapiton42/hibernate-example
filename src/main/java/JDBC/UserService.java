package JDBC;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by kapiton on 06.03.16.
 */
@Service("UserService")
public class UserService {
    private final UserDAO userDAO;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public void insert(User user) {
        userDAO.insert(user);
        logger.info("Insert user with id {}", user.getId());
    }
    @Transactional
    public Optional<User> get(int userId) {
        logger.info("Get user with id {}", userId);
        return userDAO.get(userId);
    }
    @Transactional
    public Set<User> getAll() {
        logger.info("Get all users");
        return userDAO.getAll();
    }
    @Transactional
    public void update(User user) {
        userDAO.update(user);
        logger.info("Update user with id {}", user.getId());
    }
    @Transactional
    public void delete(int userId) {
        userDAO.delete(userId);
        logger.info("Delete user with id {}", userId);
    }

    public UserService(final UserDAO userDAO) {
        this.userDAO = requireNonNull(userDAO);
    }
    @Transactional
    public void withdrawMoneyFromUser(int userId, int money) {
        withdrawMoneyFromUserWithoutTrans(userId, money);
    }

    public void withdrawMoneyFromUserWithoutTrans(int userId, int money) {
        User user = userDAO.get(userId).get();
        user.setMoney(user.getMoney() - money);
        userDAO.update(user);
        logger.info("Withdraw {} money from user with id {}", money, userId);
    }
}
