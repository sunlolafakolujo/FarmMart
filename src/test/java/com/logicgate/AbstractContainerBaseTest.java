package com.logicgate;

import org.testcontainers.containers.MySQLContainer;

public abstract class AbstractContainerBaseTest {
    final static MySQLContainer MY_SQL_CONTAINER;
    static{
        MY_SQL_CONTAINER =new MySQLContainer("MySql:latest");
        MY_SQL_CONTAINER.start();
    }
}
