package JDBC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import Configuration.BaseConfiguration;
/**
 * Created by kapiton on 09.03.16.
 */
@Configuration
@EnableTransactionManagement
public class JDBCTestConfig {
    public static ApplicationContext applicationContext = null;

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("create-tables.sql")
                .build();
    }

    @Bean
    @Autowired
    public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager txManager = new DataSourceTransactionManager(dataSource);
        return txManager;
    }

    public static ApplicationContext createApplicationContext() {
        if(applicationContext == null)
            applicationContext = new AnnotationConfigApplicationContext(BaseConfiguration.class, JDBCTestConfig.class);
        return applicationContext;
    }
}
