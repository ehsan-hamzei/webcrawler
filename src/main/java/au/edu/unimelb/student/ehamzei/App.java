package au.edu.unimelb.student.ehamzei;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    static {
        PropertyConfigurator.configure("src/log4j.properties");
    }

    public static void main( String[] args ) {
        LOG.info("Hi there :)");
    }
}
