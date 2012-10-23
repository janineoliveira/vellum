/*
 * Copyright Evan Summers
 * 
 */
package venigmon.app;

import bizstat.server.BizstatServer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import org.h2.tools.Server;
import vellum.config.ConfigMap;
import vellum.config.ConfigParser;
import vellum.config.PropertiesMap;
import vellum.datatype.SimpleEntityCache;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.logr.LogrLevel;
import vellum.storage.DataSourceConfig;
import vellum.storage.SimpleConnectionPool;
import vellum.util.Streams;
import venigmon.httpserver.HttpServerConfig;
import venigmon.httpserver.VenigmonHttpServer;
import venigmon.storage.VenigmonSchemaStorage;
import venigmon.storage.VenigmonStorage;

/**
 *
 * @author evan
 */
public class VenigmonStarter {

    Logr logger = LogrFactory.getLogger(VenigmonStarter.class);
    VenigmonStorage storage;
    VenigmonHttpServer httpServer;
    DataSourceConfig dataSourceConfig;
    PropertiesMap configProperties;
    BizstatServer server;
    Thread serverThread;
    String confFileName;
    ConfigMap configMap;
    Server h2Server;

    public void init() throws Exception {
        initConfig();        
        if (configProperties.getBoolean("h2TcpServer")) {
            h2Server = Server.createTcpServer().start();            
        }            
        dataSourceConfig = new DataSourceConfig(configMap.get("DataSource", 
                configProperties.getString("dataSource")).getProperties());
        storage = new VenigmonStorage(new SimpleEntityCache(), new SimpleConnectionPool(dataSourceConfig));
        new VenigmonSchemaStorage(storage).verifySchema();
        httpServer = new VenigmonHttpServer(storage, new HttpServerConfig(configMap.get("HttpServer", 
                configProperties.getString("httpServer")).getProperties()));
    }

    public void start() throws Exception {
        httpServer.start();
        logger.info("HTTP server started");
        testPost();
        //stop();
    }
    
    private void testPost() throws IOException {
        URL url = new URL("http://localhost:8080/post/aide/evans");
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        PrintWriter out = new PrintWriter(connection.getOutputStream());
        out.println("hello");
        out.close();
        String response = Streams.readString(connection.getInputStream());
        logger.info(response);
    }

    public void stop() throws Exception {
        httpServer.stop();
        if (h2Server != null) {
            h2Server.stop();
        }
    }
    
    private void initConfig() throws Exception {
        confFileName = getString("conf");
        configMap = ConfigParser.newInstance(new FileInputStream(new File(confFileName)));
        configProperties = configMap.find("Config", "default").getProperties();
        String logLevelName = configProperties.get("logLevel");
        if (logLevelName != null) {
            LogrFactory.setDefaultLevel(LogrLevel.valueOf(logLevelName));
        }
    }

    private String getString(String name) {
        String string = System.getProperty(name);
        if (string == null) {
            throw new RuntimeException(name);
        }
        return string;
    }
    
    public static void main(String[] args) throws Exception {
        try {
            VenigmonStarter starter = new VenigmonStarter();
            starter.init();
            starter.start();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
