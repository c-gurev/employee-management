package com.employee.management.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

@ApplicationScoped
public class DataSourceProducer {

    private static final String URL = "jdbc:oracle:thin:@//oracle-db:1521/XEPDB1";

    private static final String USER = "emp_mngr";

    private static final String PASSWORD = "mypwd";

    private HikariDataSource dataSource;

    @Produces
    @ApplicationScoped
    public DataSource dataSource() {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("oracle.jdbc.OracleDriver");
            config.setJdbcUrl(URL);
            config.setUsername(USER);
            config.setPassword(PASSWORD);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setPoolName("EmpMngrHikariCP");
            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }

    public void close(@Disposes DataSource dataSource) {
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        }
    }
}
