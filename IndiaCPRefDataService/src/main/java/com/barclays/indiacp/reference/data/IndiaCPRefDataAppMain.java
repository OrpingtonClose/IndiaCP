package com.barclays.indiacp.reference.data;

import com.barclays.indiacp.reference.data.entities.SettlementDetails;
import com.barclays.indiacp.reference.data.entities.LegalEntity;
import com.barclays.indiacp.reference.data.entities.UserDetails;
import com.barclays.indiacp.reference.data.service.LegalEntityService;
import com.barclays.indiacp.reference.data.service.SettlementDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;

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

    @Autowired
    LegalEntityService legalEntityService;

    public static void main(String[] arg)
    {
        ConfigurableApplicationContext ctx = SpringApplication.run(IndiaCPRefDataAppMain.class,arg);

        IndiaCPRefDataAppMain mainObj = ctx.getBean(IndiaCPRefDataAppMain.class);
        mainObj.testRun();
    }


    private void testRun()
    {
        System.out.println("This is IndiaCPRefDataAppMain test Run");
        settlementDetailsService.add(new SettlementDetails("Issuer","","MM","","","","","", ""));
        System.out.println(" " + settlementDetailsService.listAll());

        LegalEntity le = new LegalEntity("ABC Bank","123123123123","Chennai","020-20202020","abc@abc.com","www.abc.com","","","ABC DEF");

        ArrayList<UserDetails> arr = new ArrayList<UserDetails>();

        arr.add(new UserDetails("person A", "AI", "Wall street","100","obama@mm.com"));

        le.setUserDetails(arr);

        legalEntityService.add(le);
        System.out.println(" " + legalEntityService.listAll());

        System.out.println("This is IndiaCPRefDataAppMain test Run is now complete");
    }

}
