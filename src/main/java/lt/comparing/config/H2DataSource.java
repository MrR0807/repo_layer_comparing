package lt.comparing.config;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;

public class H2DataSource {

    private static JdbcDataSource dataSource;

    private H2DataSource() {
    }

    public static DataSource dataSource() {
        if (dataSource != null) {
            return dataSource;
        }
        dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        return dataSource;
    }

    public static DataSource dataSourceWithTracing() {
        if (dataSource != null) {
            return dataSource;
        }
        dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;TRACE_LEVEL_FILE=3;TRACE_LEVEL_SYSTEM_OUT=3");
        return dataSource;
    }
}
