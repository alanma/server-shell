package net.ilx.server.shell.core.server.commands;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

import net.ilx.server.shell.core.server.spring.components.commands.hint.HintCapturingSimpleParser;
import net.ilx.server.shell.core.utils.alf.server.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.SimpleParser;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;

public class ServerCommands implements CommandMarker {

    private static final org.jboss.logging.Logger LOG = org.jboss.logging.Logger.getLogger(ServerCommands.class);

    @Autowired
    private SimpleParser parser;

    @Autowired
    private Environment env;

    @Autowired
    private ApplicationContext appCtxt;

    @CliCommand(value="commands", help="list available commands")
    public String commands() {
        StringBuilder sb = new StringBuilder();

        sb.append("Available commands are:").append(StringUtils.LINE_SEPARATOR);
        Set<String> availableCommands = parser.getEveryCommand();
        for (String command : availableCommands) {
            sb.append("\t").append(command).append(StringUtils.LINE_SEPARATOR);
        }

        return sb.toString();
    }

    @CliCommand(value = "help", help = "list all commands usage")
    public String obtainHelp(@CliOption(key = { "", "command" }, help = "Command name to provide help for") String buffer) {
        String help;
        if (parser instanceof HintCapturingSimpleParser) {
            HintCapturingSimpleParser myParser = (HintCapturingSimpleParser) parser;
            help = myParser.getHelp(buffer);
        }
        else {
            help = commands();
        }
        return help;
    }


    @CliCommand(value="env list")
    public String envList() {
        StringBuilder sb = new StringBuilder("property sources:").append(StringUtils.LINE_SEPARATOR);
        if (env instanceof ConfigurableEnvironment) {
            ConfigurableEnvironment confEnv = (ConfigurableEnvironment) env;
            MutablePropertySources propertySources = confEnv.getPropertySources();
            for (PropertySource<?> propertySource : propertySources) {
                sb.append(propertySource.getName()).append(StringUtils.LINE_SEPARATOR);
            }
        }
        return sb.toString();
    }

//	@CliCommand(value="eval", help="eval SpEL in current runtime")
    public String evalSpel(
            @CliOption(key = "exp", mandatory=true, help="SpEL expression to parse in currenct application context")
            String expressionString) {
        StringWriter sw = new StringWriter();
        try {
//			Scope scope = null;
//
            BeanExpressionResolver beanExpressionResolver = new StandardBeanExpressionResolver();
            if (appCtxt instanceof ConfigurableApplicationContext) {
                ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) appCtxt;
                ConfigurableListableBeanFactory beanFactory = configurableApplicationContext.getBeanFactory();
                beanExpressionResolver = beanFactory.getBeanExpressionResolver();
                Object value = beanExpressionResolver.evaluate(expressionString, new BeanExpressionContext(beanFactory, null));
                sw.append(value.toString());
            }

//			StandardEvaluationContext context = new StandardEvaluationContext();
//
//			BeanResolver beanResolver = new BeanFactoryResolver(appCtxt);
//			context.setBeanResolver(beanResolver);
//
//			context.addPropertyAccessor(new EnvironmentAccessor());
//
//			ExpressionParser parser = new SpelExpressionParser();
//			Expression expr = parser.parseExpression(expressionString);
//
//
//			Object value = expr.getValue(context);

//			sw.append(value.toString());
        }
        catch (Throwable t) {

            PrintWriter pw = new PrintWriter(sw);

            t.printStackTrace(pw);
        }

        return sw.toString();
    }




}
