package net.ilx.server.shell.modules.database.configuration;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import net.ilx.server.shell.modules.database.configuration.DatabaseConfiguration;

import org.hsqldb.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.jdbc.core.JdbcTemplate;


public class DatabaseConfigurationTest {

    private static Server server;


    @BeforeClass
    public static void startDb() {
        server = new Server();
        server.setLogWriter(null);
        server.setSilent(false);
        server.setDatabaseName(0, "sa");
        server.start();
    }
    
    @AfterClass
    public static void stopDb() {
        if (server!= null) {
            server.stop();
            server = null;
        }
    }
    
    
    @Test
    public void testDataSourceRegistration() throws Exception {
        DatabaseConfiguration configuration = new DatabaseConfiguration();

        StandardEnvironment env = new StandardEnvironment();
        MutablePropertySources propertySources = env.getPropertySources();
        ResourcePropertySource propertySource = new ResourcePropertySource("classpath:/database.properties");
        propertySources.addLast(propertySource);

        StaticApplicationContext appContext = new StaticApplicationContext();
        configuration.setEnvironment(env);
        configuration.postProcessBeanDefinitionRegistry(appContext);

        Map<String, DataSource> dataSources = appContext.getBeansOfType(DataSource.class);
        assertThat("DataSource beans must be loaded from property file", dataSources, notNullValue());
        DataSource dataSource = dataSources.get(".ds1");
        assertThat("At least one datasource (of name '.ds1') must be defined", dataSource, notNullValue());

        Connection connection = dataSource.getConnection();
        assertThat("Connection is created.", connection, notNullValue());
        
        JdbcTemplate template = new JdbcTemplate(dataSource);
        int res = template.queryForInt("values(1)");
        assertThat(res, equalTo(1));
    }
}
