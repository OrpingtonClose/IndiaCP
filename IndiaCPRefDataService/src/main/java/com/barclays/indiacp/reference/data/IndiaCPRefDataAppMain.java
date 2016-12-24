package com.barclays.indiacp.reference.data;

import com.barclays.indiacp.reference.data.entities.SettlementDetails;
import com.barclays.indiacp.reference.data.entities.LegalEntity;
import com.barclays.indiacp.reference.data.entities.UserDetails;
import com.barclays.indiacp.reference.data.service.LegalEntityService;
import com.barclays.indiacp.reference.data.service.SettlementDetailsService;
import com.barclays.indiacp.reference.data.service.UserDetailsService;
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
import java.util.List;

/**
 * Created by chaos on 22/12/16.
 */

@EnableAutoConfiguration
@Component
@ComponentScan
@PropertySource(value = { "classpath:jdbc.properties"})
public class IndiaCPRefDataAppMain
{

    @Autowired
    LegalEntityService legalEntityService;

    @Autowired
    SettlementDetailsService settlementDetailsService;

   @Autowired
   UserDetailsService userDetailsService;



    public static void main(String[] arg)
    {
        ConfigurableApplicationContext ctx = SpringApplication.run(IndiaCPRefDataAppMain.class,arg);

        IndiaCPRefDataAppMain mainObj = ctx.getBean(IndiaCPRefDataAppMain.class);
        mainObj.testRun();
    }


    private void testRun()
    {
        System.out.println("This is IndiaCPRefDataAppMain test Run");
        //settlementDetailsService.add(new SettlementDetails("Issuer","","MM","","","","","", ""));
        //System.out.println(" " + settlementDetailsService.listAll());

        LegalEntity le = new LegalEntity(1, "Mo Bank","12312312312312","Kuala Lumpur","0612-232223232","","Kuala,Lumpur@kl.com","kualalumpur.com","","Mr. KL");
        LegalEntity le1 = new LegalEntity(2, "Bank of America","567567567","America","+1 99999999","","United States of America","unitedstates.com","","Mr. US");


        ArrayList<UserDetails> arrUser = new ArrayList<UserDetails>();

        arrUser.add(new UserDetails(1,"person DE", "Finance", "Langkawi","+613 232323232","langkawi@kl.com"));
        arrUser.add(new UserDetails(1,"person FG","Operations","Malacca", "+613 1000 000 00", "malacca@kl.com"));
        le.setUserDetails(arrUser);

        ArrayList<SettlementDetails> arrSettlement = new ArrayList<SettlementDetails>();

        arrSettlement.add(new SettlementDetails(1,"HDFC Bank", "121211212", "Barclays","50000","123123123","1231231231","45645645", "675675", "34232"));
        arrSettlement.add(new SettlementDetails(1,"BILIL","6445645","Yes Bank", "HDFC0000", "56456464", "35346346","78678687", "786856856", "79878978"));
        le.setSettlementDetails(arrSettlement);

        legalEntityService.add(le);
        legalEntityService.add(le1);

//       System.out.println(legalEntityService.listAll());

        List<LegalEntity> leArr = legalEntityService.listAll();
        for(LegalEntity l : leArr)
        {
            System.out.println(l);
        }
       System.out.println("This is IndiaCPRefDataAppMain test Run is now complete");
    }

}
