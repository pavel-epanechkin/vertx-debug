package dao;

import org.h2.jdbcx.JdbcConnectionPool;
import org.jooq.*;

import static org.jooq.impl.DSL.using;

public class H2DB {

    private JdbcConnectionPool connectionPool;

    public H2DB(String dbPath, String generationScriptPath) {
        connectionPool =
                JdbcConnectionPool.create(createConnectionString(dbPath, generationScriptPath), "admin", "admin");
    }

    private String createConnectionString(String dbPath, String generationScriptPath) {
        return "jdbc:h2:file:" + dbPath + ";INIT=RUNSCRIPT FROM '" + generationScriptPath + "'";
    }

    public DSLContext query() {
        return using(connectionPool, SQLDialect.H2);
    }

    public void close() {
        connectionPool.dispose();
    }
}
