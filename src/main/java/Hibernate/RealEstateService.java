package Hibernate;

/**
 * Created by kapiton on 06.03.16.
 */

import JDBC.UserService;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Transactional
@Service("RealEstateService")
public class RealEstateService {
    @Autowired
    private RealEstateDAO realEstateDAO;
    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(RealEstateService.class);

    public UserService getUserService() {
        return userService;
    }

    public RealEstateService(final SessionFactory sessionFactory,
                             final RealEstateDAO realEstateDAO, final UserService userService) {
        this.userService = requireNonNull(userService);
        this.realEstateDAO = requireNonNull(realEstateDAO);
    }

    @Deprecated
    RealEstateService() {}

    public void createTables(String pathname) {
        try {
            realEstateDAO.createTables(pathname);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void save(final RealEstate realEstate) {
        realEstateDAO.insert(realEstate);
        logger.info("Insert real estate with id {}", realEstate.getId());
    }

    public Optional<RealEstate> get(final int realEstateId) {
        logger.info("get real estate with id {}", realEstateId);
        return realEstateDAO.get(realEstateId);
    }

    public Set<RealEstate> getAll() {
        logger.info("Get all real estate");
        return realEstateDAO.getAll();
    }

    public void update(final RealEstate realEstate) {
        realEstateDAO.update(realEstate);
        logger.info("Update real estate with id {}", realEstate.getId());
    }

    public void changeAddress(final int realEstateId, final String address) {
            final Optional<RealEstate> optionalRealEstate = realEstateDAO.get(realEstateId);
            if (!optionalRealEstate.isPresent()) {
                IllegalArgumentException illegalArgumentException =
                        new IllegalArgumentException("there is no real estate with id " + realEstateId);
                logger.warn("Try Change address of no existing real estate with id {}",
                        realEstateId, illegalArgumentException);
                throw illegalArgumentException;
            }
            optionalRealEstate.get().setAddress(address);
        logger.info("Change address of real estate with id {}", realEstateId);
    }

    public void changeCost(final int realEstateId, final int cost) {
            final Optional<RealEstate> optionalRealEstate = realEstateDAO.get(realEstateId);
            if (!optionalRealEstate.isPresent()) {
                IllegalArgumentException illegalArgumentException =
                        new IllegalArgumentException("there is no real estate with id " + realEstateId);
                logger.warn("Try Change cost of no existing real estate with id {}",
                        realEstateId, illegalArgumentException);
                throw illegalArgumentException;
            }
            optionalRealEstate.get().setCost(cost);
        logger.info("Change cost of real estate with id {}", realEstateId);
    }

    public void changeOwner(final int realEstateId, final int ownerId) {
            final Optional<RealEstate> optionalRealEstate = realEstateDAO.get(realEstateId);
            if (!optionalRealEstate.isPresent()) {
                IllegalArgumentException illegalArgumentException =
                        new IllegalArgumentException("there is no real estate with id " + realEstateId);
                logger.warn("Try Change owner of no existing real estate with id {}",
                        realEstateId, illegalArgumentException);
                throw illegalArgumentException;
            }
            optionalRealEstate.get().setOwner(ownerId);
        logger.info("Change owner of real estate with id {}", realEstateId);
    }

    public void buyEstate(int userId, int realEstateId) {
        changeOwner(realEstateId, userId);
        userService.withdrawMoneyFromUserWithoutTrans(userId, get(realEstateId).get().getCost());
        logger.info("Buy real estate with id {} by user with id {}", realEstateId, userId);
       // int i = 1 / 0;
    }

    public void delete(final int realEstateId) {
        realEstateDAO.delete(realEstateId);
        logger.info("Delete real estate with id {}", realEstateId);
    }

    public void cleanTables() {
        realEstateDAO.cleanTables();
    }
}
