package ru.hh.performance_review.config;

import liquibase.integration.spring.SpringLiquibase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import ru.hh.nab.common.properties.FileSettings;
import ru.hh.nab.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class LiquibaseConfig {

    private final static String DB_USER_NAME = "db.user";
    private final static String DB_PASSWORD = "db.password";
    private final static String DB_URL = "db.jdbcUrl";

    private final static String ENV_DB_USER_NAME = "dbUser";
    private final static String ENV_DB_PASSWORD = "dbPass";
    private final static String ENV_DB_URL = "dbHost";


    @Bean
    public DataSource dataSource(DataSourceFactory dataSourceFactory, FileSettings fileSettings) {
        Properties properties = buildProperties(fileSettings.getProperties());
        return dataSourceFactory.create("db", false, new FileSettings(properties));
    }

    private Properties buildProperties(Properties properties) {

        String dbUserName = System.getProperties().getProperty(ENV_DB_USER_NAME);
        String dbPassword = System.getProperties().getProperty(ENV_DB_PASSWORD);
        String dbHost = System.getProperties().getProperty(ENV_DB_URL);

        if (StringUtils.isNotBlank(dbUserName)) {
            properties.put(DB_USER_NAME, dbUserName);
        }

        if (StringUtils.isNotBlank(dbPassword)) {
            properties.put(DB_PASSWORD, dbPassword);
        }

        if (StringUtils.isNotBlank(dbHost)) {
            String url = String.format("jdbc:postgresql://%s:5432/performance_review", dbHost);
            properties.put(DB_URL, url);
        }

        return properties;
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();

        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("liquibase/root-changelog.xml");
        liquibase.setDefaultSchema("performance_review");

        return liquibase;
    }


}
