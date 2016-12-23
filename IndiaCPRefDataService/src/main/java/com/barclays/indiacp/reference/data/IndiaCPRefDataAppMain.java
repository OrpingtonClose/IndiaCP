package com.barclays.indiacp.reference.data;

import com.barclays.indiacp.reference.data.entities.SettlementDetails;
import com.barclays.indiacp.reference.data.service.SettlementDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by chaos on 22/12/16.
 */

@EnableAutoConfiguration
@Component
@ComponentScan
@PropertySource(value = { "file:/home/chaos/RISE_CP/RISE_WORK/IndiaCP/IndiaCPRefDataService/src/main/resources/jdbc.properties" })
public class IndiaCPRefDataAppMain
{

    @Autowired
    SettlementDetailsService settlementDetailsService;



    public static void main(String[] arg)
    {
        ConfigurableApplicationContext ctx = SpringApplication.run(IndiaCPRefDataAppMain.class,arg);

        IndiaCPRefDataAppMain mainObj = ctx.getBean(IndiaCPRefDataAppMain.class);
        mainObj.testRun();
    }


    private void testRun()
    {
        System.out.println("This is IndiaCPRefDataAppMain test Run");
        settlementDetailsService.add(new SettlementDetails("S2","Issuer","","MM","","","","",""));
        System.out.println(" " + settlementDetailsService.listAll().size());

        System.out.println("This is IndiaCPRefDataAppMain test Run is now complete");
    }

}
