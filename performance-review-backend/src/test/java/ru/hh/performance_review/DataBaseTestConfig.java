package ru.hh.performance_review;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import ru.hh.nab.common.properties.FileSettings;
import ru.hh.nab.datasource.DataSourceFactory;
import ru.hh.nab.hibernate.NabHibernateCommonConfig;
import ru.hh.nab.testbase.NabTestConfig;
import ru.hh.nab.testbase.hibernate.NabHibernateTestBaseConfig;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Import({
  NabTestConfig.class,
  NabHibernateCommonConfig.class,
  NabHibernateTestBaseConfig.class
})
public class DataBaseTestConfig {

  private static final String DB_SETTINGS_FILE_NAME = "db-settings-test.properties";

  @Bean
  public DataSource dataSource(DataSourceFactory dataSourceFactory, Properties dbProperties) {
    return dataSourceFactory.create("test_db", false, new FileSettings(dbProperties));
  }

  @Bean
  public PropertiesFactoryBean dbProperties() {
    PropertiesFactoryBean properties = new PropertiesFactoryBean();
    properties.setSingleton(false);
    properties.setIgnoreResourceNotFound(true);
    properties.setLocations(
            new ClassPathResource(DB_SETTINGS_FILE_NAME),
            new ClassPathResource(DB_SETTINGS_FILE_NAME + ".dev"));
    return properties;
  }

  @Bean
  public SpringLiquibase liquibase(@Qualifier("dataSource") DataSource dataSource) {
    SpringLiquibase liquibase = new SpringLiquibase();

    liquibase.setDataSource(dataSource);
    liquibase.setChangeLog("service/test-root-changelog.xml");

    return liquibase;
  }
}
