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
package newsscraper;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.SimpleBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.HashMap;

import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.rss.RssComponent;
import org.apache.abdera.model.Entry;


public class StoryReader { //SINGLETON
	private static StoryReader reader = null;
 
    // private constructor restricted to this class itself
    private StoryReader()
    {}
 
    // Create the sole instance of StoryReader
    public static StoryReader getInstance() //SINGLETON 
    {
        if (reader == null)
            reader = new StoryReader();

        System.out.println("StoryReader created.");
 
        return reader;
    }

   public static void main(String args[]) throws Exception {
    	
        //Create CamelContext
    	CamelContext context = new DefaultCamelContext(); // Adds RouteBuilder 
    	
        //Connects to ActiveMQ JMS broker listening on localhost on port 61616
        ConnectionFactory connectionFactory = 
        	new ActiveMQConnectionFactory("tcp://localhost:61616");
        context.addComponent("jms", //FIX THIS quote 
            JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
                // Adds  route to the CamelContext
        context.addRoutes(new RouteBuilder() {
            public void configure() { 
            	from("jms:queue:READ_STORIES") //CONTENT-BASED ROUTER
                //.unmarshal   ?
                .choice()
                    .when(header().regex(".*NEWS.*"))
                        .to("jms:topic:NEWS_SOURCE_TRENDS")
                    .when(header().regex(".*SOCIAL.*"))
                        .to("jms:topic:SOCIAL_SOURCE_TRENDS")
                    .otherwise()
                        .to("jms:topic:UNKNOWN_SOURCE_TRENDS");

                }
        });
        // Start the route and loop indefinitely
    	context.start(); 
    }
}