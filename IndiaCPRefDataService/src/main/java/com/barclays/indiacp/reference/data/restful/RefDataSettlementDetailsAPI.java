package com.barclays.indiacp.reference.data.restful;

import com.barclays.indiacp.reference.data.entities.LegalEntity;
import com.barclays.indiacp.reference.data.entities.SettlementDetails;
import com.barclays.indiacp.reference.data.entities.UserDetails;
import com.barclays.indiacp.reference.data.service.LegalEntityService;
import com.barclays.indiacp.reference.data.service.SettlementDetailsService;
import com.barclays.indiacp.reference.data.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

/**
 * Created by chaos on 22/12/16.
 */

@RestController
public class RefDataSettlementDetailsAPI
{

    @Autowired
    SettlementDetailsService settlementDetailsService;

    @Autowired
    LegalEntityService legalEntityService;

    @Autowired
    UserDetailsService userDetailsService;


    /*@RequestMapping("/getRefDataSettlementDetails")
    public String fetchSettlementDetails(@RequestParam(value="settlementID") String settlementDetailsId) {

        String test = "";
        for(SettlementDetails sd : settlementDetailsService.listAll())
        {
            test += sd.getSettlement_key() + "::";
        }
        return "" + test;
    }*/

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<LegalEntity> fetchAll() {
        return legalEntityService.listAll();
    }

    @RequestMapping(value = "/getLegalEntityDetails", method = RequestMethod.GET)
    public LegalEntity fetchLegalEntityDetails(@RequestParam(value="LegalEntityID") String legalEntityID) {
        return legalEntityService.getLegalEntityByID(legalEntityID);
    }

    @RequestMapping(value = "/getUserDetails", method = RequestMethod.GET)
    public List<UserDetails> fetchUserDetails(@RequestParam(value="LegalEntityID") String legalEntityID) {
        return userDetailsService.getUserDetailsForEntity(legalEntityID);

    }

    @RequestMapping(value = "/getSettlementDetails", method = RequestMethod.GET)
    public List<SettlementDetails> fetchSettlementDetails(@RequestParam(value="LegalEntityID") String legalEntityID) {
        return settlementDetailsService.getSettlementDetailsForEntity(legalEntityID);
    }


    @RequestMapping(value = "/addLegalEntityDetails", method = RequestMethod.POST)
    public void addLegalEntityDetails(@RequestBody LegalEntity legalEntityJSON) {
        legalEntityService.add(legalEntityJSON);
    }




    /*@RequestMapping(value = "/addLegalEntityDetails", method = RequestMethod.POST, consumes = { "application/json" })
    public void addLegalEntityDetails(@RequestBody LegalEntity legalEntityJSON) {


        legalEntityService.add(legalEntityJSON);
    }*/



}
