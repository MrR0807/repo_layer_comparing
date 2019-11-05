package lt.comparing.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

import static java.util.Objects.nonNull;

public class HikariConfiguration {

    private static HikariDataSource dataSource;

    private HikariConfiguration() {
    }

    public static DataSource dataSource() {
        if (nonNull(dataSource)) {
            return dataSource;
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
//        config.setUsername("sa");
//        config.setPassword("");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(config);
    }
}
