package au.edu.unimelb.student.ehamzei.crawler;

import au.edu.unimelb.student.ehamzei.crawler.config.Config;
import au.edu.unimelb.student.ehamzei.jms.ActiveMQHandler;
import com.google.gson.Gson;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import java.io.IOException;

/**
 * Created by ehamzei on 12/07/2017.
 */
public class BirdWatchingCrawler extends WebCrawler {
    private static final Logger LOG = LoggerFactory.getLogger(BirdWatchingCrawler.class);
    private static final Gson gson = new Gson();
    private static ActiveMQHandler handler;

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        LOG.debug("Visiting the URL : " + url);
        return (href.contains(Config.BIRD_WATCHING_MAIN_URL));
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
//        if (page.getParseData() instanceof HtmlParseData) {
//            HtmlParseData data = (HtmlParseData) page.getParseData();
//            String htmlStr = data.getHtml();
        Document doc = null;
        try {
            doc = Jsoup.connect(page.getWebURL().getURL()).timeout(100 * 1000).get();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        Elements elements = doc.getElementsByClass("postcontent restore");
        if (elements != null && elements.size() != 0) {
            LOG.info("******Starting new page*******");
            LOG.info("Processing the URL: " + url);
            String message = "";
            for (Element e : elements) {
                message += e.text()+"\n";

            }
            LOG.info("Message is : " + message);
            //inserting into ActiveMQ
            try {
                handler = ActiveMQHandler.getInstance();
                handler.insertDataIntoJMS(message, "birdwatching");
                Thread.sleep(75L);
            } catch (InterruptedException e) {
                LOG.error(e.getMessage(), e);
            } catch (JMSException e) {
                LOG.error(e.getMessage(), e);
            }
            LOG.info("******Finishing new page*******");
        }
    }
}
