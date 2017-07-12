package au.edu.unimelb.student.ehamzei.jms;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

/**
 * Created by ehamzei on 12/07/2017.
 */
public class ActiveMQHandler {
    private Connection jmsConnection;
    private String connectionURL = "tcp://localhost:61616";

    private ActiveMQHandler() throws JMSException {
        //Singleton Structure!!!
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("admin", "admin", connectionURL);
        this.jmsConnection = factory.createConnection();
    }

    public Connection getJmsConnection() {
        return jmsConnection;
    }

    public void setJmsConnection(Connection jmsConnection) {
        this.jmsConnection = jmsConnection;
    }

    public String getConnectionURL() {
        return connectionURL;
    }

    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }

    public static ActiveMQHandler getInstance() throws JMSException {
        if(instance == null)
            instance = new ActiveMQHandler();
        return instance;
    }

    public static void setInstance(ActiveMQHandler instance) {
        ActiveMQHandler.instance = instance;
    }

    private static ActiveMQHandler instance;

    public void start() throws JMSException {
        this.jmsConnection.start();
    }

    public void close() throws JMSException {
        this.jmsConnection.close();
    }

    public void insertDataIntoJMS (String jsonData, String queueName) throws JMSException {
//        this.jmsConnection.start();
        Session session = jmsConnection.createSession(true, 1);
        Destination dest = new ActiveMQQueue(queueName);
        MessageProducer producer = session.createProducer(dest);
        ActiveMQTextMessage message = new ActiveMQTextMessage();
        message.setJMSDestination(dest);
        message.setText(jsonData);
        producer.send(message);
        session.commit();
        session.close();
//        this.jmsConnection.close();
    }
}
