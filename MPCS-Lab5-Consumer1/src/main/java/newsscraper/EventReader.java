/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.SimpleBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;


public class TrendItem {
    private String trendName = "Default"; //Set default names that we can replace later on.
    private String url = "Default.com";
    private String siteName = "Default";
    private String trendType = "Default"; //WORK ON THIS
    
    public TrendItem(String trendName, String url, String siteName, String trendType) {
        super();
        this.title = title;
        this.url = url;
        this.siteName = siteName;
        this.trendType = trendType;
    }

    //Getters and setters
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getSiteName() {
        return siteName;
    }
    public int setSiteName() {
        this.siteName = siteName; //Necessary?  We are parsing existing feeds
    }
    //Set Type - create a map of sites and classify them as SOCIAL MEDIA or NEWS.  
    
}


public class EventReader {

    public static void main(String args[]) throws Exception {
    	
        // create CamelContext
    	CamelContext context = new DefaultCamelContext(); // Adds RouteBuilder 
    	
        HashMap<String, String> siteType = new HashMap<String, Boolean>(); //Create hash map - either "News" or "Social Media"

        //Connects to ActiveMQ JMS broker listening on localhost on port 61616
        ConnectionFactory connectionFactory = 
        	new ActiveMQConnectionFactory("tcp://localhost:61616");
        context.addComponent("jms",
            JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        
        // Adds  route to the CamelContext
        context.addRoutes(new RouteBuilder() {
            public void configure(String source) {
                .from("jms:queue:" + source + "FILTER");
                .log("RECEIVED: jms queue: ${body} from file: ${header.CamelFileNameOnly}")
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        String title = exchange.getIn().getBody(String.class);
                        TrendItem inTrend = new TrendItem();
                        //Set SiteName, TrendType, etc. 
                        //Arbitrarily set TRENDS according to keywords (i.e. Trump, Korea, Economy, etc.)
                     //   String THISURL = exchange.getIn().getURL(String.class);   SYNTAX
                     //   if (siteType.get(THISURL) == "NEWS") { //Already in hash map
                          // trend1.setType("NEWS"); //Filter out duplicate stories
                       // }
                      //  else{ SOCIAL MEDIA
                      //      exchange.getIn().setBody(title);
                      //      trend1.setType("SOCIAL");   
                    //    }

                        //CREATE NEWS-SOCIAL MEDIA MAP
                        //Case-based router to either of the two filters
                    }
                   }
                .to("file:data/outbox?noop=true&fileName=Thread-${threadName}-${header.CamelFileNameOnly}.out");
            }
        });

        // start the route and let it do its work
        context.start();
    }
}

//Why is the data not ending up in the outbox?
