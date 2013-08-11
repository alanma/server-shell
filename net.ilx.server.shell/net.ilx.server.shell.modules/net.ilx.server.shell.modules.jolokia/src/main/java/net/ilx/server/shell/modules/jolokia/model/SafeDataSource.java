package net.ilx.server.shell.modules.jolokia.model;

public interface SafeDataSource {

	public String getDummyStringValue();

    public int getSize();

    public int getIdle();

    public int getMaxActive();

    public int getActive();

    public int getNumIdle();

    public int getNumActive();

    public int getWaitCount();

    //=================================================================
    //       POOL OPERATIONS
    //=================================================================
    public void checkIdle();

    public void checkAbandoned();

    public void testIdle();

    // Settings

    public String getDriverClassName();

    public String getPoolName();


    public String getValidationQuery();

    public String getUrl();
}
