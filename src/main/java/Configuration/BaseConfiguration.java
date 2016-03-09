package Configuration;
/**
 * Created by kapiton on 09.03.16.
 */
import java.util.Properties;

import javax.inject.Inject;
import javax.sql.DataSource;

import JDBC.User;
import JDBC.UserDAO;
import JDBC.UserService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "Hibernate" })
public class BaseConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    @Autowired
    UserDAO userDAO(DataSource dataSource) {
        return new UserDAO(dataSource);
    }

    @Bean
    @Autowired
    UserService userService(UserDAO userDAO) {
        return new UserService(userDAO);
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan(new String[] { "Hibernate" });
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Inject
    private DataSource dataSource;

    @Inject
    private PlatformTransactionManager platformTransactionManager;

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.format_sql", true);
        return properties;
    }

    static EmbeddedDatabase createEmbeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("create-tables.sql")
                .build();
    }
}


