package Hibernate;

import Configuration.BaseConfiguration;
import JDBC.User;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by kapiton on 09.03.16.
 */
public class RealEstateServiceTest {
    public static RealEstateService realEstateService;
    public static SessionFactory sessionFactory;
    private static Set<RealEstate> realEstates;

    @BeforeClass
    public static void setUp() {
        final ApplicationContext applicationContext = createApplicationContext();
        realEstateService = applicationContext.getBean(RealEstateService.class);
        realEstateService.createTables("create-tables.sql");
        realEstateService.createTables("create-2tables.sql");
        sessionFactory = applicationContext.getBean(SessionFactory.class);
    }

    @Before
    public void setUpTest() {
        realEstates = new HashSet<>();

        RealEstate realEstate = new RealEstate("Moscow", 0, 100);
        realEstates.add(realEstate);
        realEstate = new RealEstate("New-York", 0, 300);
        realEstates.add(realEstate);
        realEstates.forEach(realEstateService::save);
    }

    @After
    public void tearDownTest() {
        realEstateService.cleanTables();
    }

    @Test
    public void testSave() throws Exception {
        RealEstate realEstate = realEstates.iterator().next();
        System.out.println(realEstate.toString());
        System.out.println(realEstateService.get(realEstate.getId()).get());
        assertTrue(realEstate.equals(realEstateService.get(realEstate.getId()).get()));
    }

    @Test
    public void testGetAll() throws Exception {
        Set<RealEstate> setGet;
        setGet = realEstateService.getAll();
        assertTrue(setGet.equals(realEstates));
    }

    @Test
    public void testUpdate() throws Exception {
        RealEstate realEstate = realEstates.iterator().next();
        realEstateService.changeCost(realEstate.getId(), 500);
        realEstateService.update(realEstate);
        System.out.println(realEstateService.get(realEstate.getId()).get());
        assertTrue(realEstate.equals(realEstateService.get(realEstate.getId()).get()));
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testDelete() throws Exception {
        RealEstate realEstate = realEstates.iterator().next();
        realEstateService.delete(realEstate.getId());
        realEstateService.get(realEstate.getId()).get();
    }

    @Test
    public void testBuy() {
        User user = User.create("ivan", "ivanov", 1000);
        realEstateService.getUserService().insert(user);

        RealEstate realEstate = realEstates.iterator().next();

        try {
            realEstateService.buyEstate(user.getId(), realEstate.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        user.setMoney(900);
        realEstate.setOwner(user.getId());
        System.out.println(realEstateService.get(realEstate.getId()).get());
        System.out.println(realEstateService.getUserService().get(user.getId()).get());

        assertTrue(realEstate.equals(realEstateService.get(realEstate.getId()).get()));
        assertTrue(user.equals(realEstateService.getUserService().get(user.getId()).get()));
    }

    public static ApplicationContext createApplicationContext() {
        return new AnnotationConfigApplicationContext(BaseConfiguration.class, HibernateTestConfig.class);
    }
}