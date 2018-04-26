package app;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class Application {

    public static void main(String args[]) throws Exception {
        final CamelContext camelContext = new DefaultCamelContext();

        RouteBuilder ftpRoute = new RouteBuilder() {
            @Override
            public void configure() {
                from("ftp://user_ftp@localhost:21?password=password&stepwise=true&streamDownload=true")
                    .process((Exchange e) -> {
                        System.out.println("Downloading started");
                    })
                    .to("file://download/")
                    .process((Exchange e) -> {
                        System.out.println("Passed to file");
                    });
                }
        };

        camelContext.addRoutes(ftpRoute);
        camelContext.start();
        Thread.sleep(1000000000);
    }
}
