package net.ilx.server.shell.modules.database.commands;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import net.ilx.server.shell.core.utils.alf.server.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;

public class DatabaseCommands implements CommandMarker {

    @Autowired
    ApplicationContext appCtxt;

    @CliCommand(value="db list", help="list available data sources")
    public String list() {
        StringBuilder sb = new StringBuilder();
        Map<String, DataSource> dataSourceMap = appCtxt.getBeansOfType(javax.sql.DataSource.class);
        Set<String> dataSourceNames = dataSourceMap.keySet();
        for (String dataSourceName : dataSourceNames) {
            sb.append(dataSourceName).append(StringUtils.LINE_SEPARATOR);
        }

        return sb.toString();
    }

    @CliCommand(value="db test", help="test given data source")
    public String test(@CliOption(key="name", help="data source name", mandatory=true) String name,
            @CliOption(key="query", help="test query to use", mandatory=false) String query,
            @CliOption(key="v", help="should we be verbose", mandatory=false, unspecifiedDefaultValue="FALSE", specifiedDefaultValue="TRUE") Boolean verbose
            ) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            DataSource dataSource = appCtxt.getBean(name, DataSource.class);
            Connection connection = dataSource.getConnection();
            pw.append("test succeeded for data source: ").append(name);
        }
        catch (Throwable t) {
            t.printStackTrace(pw);
        }

        return sw.toString();
    }
}
