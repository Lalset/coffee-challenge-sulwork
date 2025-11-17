package com.sulwork.backend.dialect;

import org.hibernate.dialect.H2Dialect;

public class H2PostgresNoReturnDialect extends H2Dialect {

    public H2PostgresNoReturnDialect() {
        super();
        this.getDefaultProperties().setProperty("hibernate.jdbc.use_get_generated_keys", "true");
    }
}
