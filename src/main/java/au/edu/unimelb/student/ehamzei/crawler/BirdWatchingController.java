package au.edu.unimelb.student.ehamzei.crawler;

import au.edu.unimelb.student.ehamzei.crawler.config.Config;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ehamzei on 12/07/2017.
 */
public class BirdWatchingController {
    private static final Logger LOG = LoggerFactory.getLogger(BirdWatchingController.class);


    static {
        PropertyConfigurator.configure("src/log4j.properties");
    }
    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = Config.CRAWL_DATA_STORAGE;
        int numberOfCrawlers = Config.NUM_OF_THREADS;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed("http://www.feathersandphotos.com.au/forum/showthread.php?38823-Norfolk-Island-Revisited-November-2015&p=345089");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(BirdWatchingCrawler.class, numberOfCrawlers);
    }
}
