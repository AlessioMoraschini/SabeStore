package com.am.design.development.dto;

public class AppProfiles {

    /**
     *  Default to use h2 in memory db and deactivate flyway
     */
    public final static String TEST_JUNIT = "test";
    /**
     *  Default to use h2 in memory db.
     */
    public final static String DEFAULT = "default";
    /**
     *  testenvlocal to test on local Docker or from IDE to connect to real local DBs running on Local Docker instance
     */
    public final static String TEST_ENV_LOCAL = "testenvlocal";
    /**
     *  testenv to test on remote Docker or from IDE to connect to real remote DBs running on remote Docker instance
     */
    public final static String TEST_ENV = "testenv";
    /**
     *  prod to use only in production environment
     */
    public final static String PRODUCTION = "prod";
}
