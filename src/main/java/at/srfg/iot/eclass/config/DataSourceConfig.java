package at.srfg.iot.eclass.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;

public class DataSourceConfig {
     
    public DataSource getDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url("jdbc:postgresql://localhost:5432/eclass");
        dataSourceBuilder.username("postgres");
        dataSourceBuilder.password("postgres");
        return dataSourceBuilder.build();
    }
}