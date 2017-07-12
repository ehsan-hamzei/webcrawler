package au.edu.unimelb.student.ehamzei.crawler;

import au.edu.unimelb.student.ehamzei.crawler.config.Config;
import au.edu.unimelb.student.ehamzei.db.walkingmaps.FullDetailModel;
import au.edu.unimelb.student.ehamzei.db.walkingmaps.MarkerModel;
import au.edu.unimelb.student.ehamzei.db.walkingmaps.POIModel;
import au.edu.unimelb.student.ehamzei.db.walkingmaps.PathDetailModel;
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
import java.util.List;

/**
 * Created by ehamzei on 11/07/2017.
 */
public class WalkingMapsCrawler extends WebCrawler {
    private static final Logger LOG = LoggerFactory.getLogger(WalkingMapsCrawler.class);
    private static final Gson gson = new Gson();
    private static ActiveMQHandler handler;
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return href.contains(Config.WALKING_MAPS_MAIN_URL);
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        LOG.debug("Visiting the URL : "+url);

        if (page.getParseData() instanceof HtmlParseData) {
            LOG.debug("******Starting new page*******");
            LOG.info("Processing the URL: "+url);
            HtmlParseData data = (HtmlParseData) page.getParseData();
            String htmlStr = data.getHtml();
            Document doc = Jsoup.parse(htmlStr);
            Element pathElement = doc.getElementById("PathDetail");
            LOG.debug(pathElement.val());
            List<PathDetailModel> pathDetail = gson.fromJson(pathElement.val(), List.class);
            Element markerElement = doc.getElementById("Markers");
            LOG.debug(markerElement.val());
            List<MarkerModel> markerModel = gson.fromJson(markerElement.val(), List.class);
            Element poiElement = doc.getElementById("PointsOfInterest");
            LOG.debug(poiElement.val());
            List<POIModel> poiModel = gson.fromJson(poiElement.val(), List.class);
            Elements titleElements = doc.getElementsByClass("row title-and-rating");
            Element titleElement = titleElements.first().getElementsByTag("h3").first();
            LOG.debug(titleElement.text());
            Element descriptionElement = doc.getElementsByClass("walk-extra mobile-info-view").first().getElementsByTag("p").first();
            LOG.debug(descriptionElement.text());

            //inserting into ActiveMQ
            try {
                FullDetailModel model = new FullDetailModel();
                model.setTitle(titleElement.text());
                model.setDescription(descriptionElement.text());
                model.setMarkers(markerModel);
                model.setPathDetails(pathDetail);
                model.setPois(poiModel);
                handler = ActiveMQHandler.getInstance();
                handler.insertDataIntoJMS(gson.toJson(model), "walkingmaps");
                LOG.info("Done!");
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
            LOG.debug("******Finishing new page*******");
        }
    }

    @Override
    public void onStart() {
        try {
            handler = ActiveMQHandler.getInstance();
            handler.start();
        } catch (Exception e ) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void onBeforeExit() {
        try {
            handler.close();
        } catch (JMSException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
