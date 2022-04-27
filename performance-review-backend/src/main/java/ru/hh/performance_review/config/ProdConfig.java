package ru.hh.performance_review.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.hh.nab.common.properties.FileSettings;
import ru.hh.nab.datasource.DataSourceFactory;
import ru.hh.nab.hibernate.NabHibernateProdConfig;
import ru.hh.nab.starter.NabProdConfig;

import javax.sql.DataSource;

@Configuration
@Import({NabHibernateProdConfig.class, NabProdConfig.class, CommonConfig.class})
public class ProdConfig {

  @Bean
  public DataSource dataSource(DataSourceFactory dataSourceFactory, FileSettings fileSettings) {
    return dataSourceFactory.create("db", false, fileSettings);
  }
}
