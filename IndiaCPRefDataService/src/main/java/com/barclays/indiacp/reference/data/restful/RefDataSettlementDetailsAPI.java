package com.barclays.indiacp.reference.data.restful;

import com.barclays.indiacp.reference.data.entities.SettlementDetails;
import com.barclays.indiacp.reference.data.service.SettlementDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chaos on 22/12/16.
 */

@RestController
public class RefDataSettlementDetailsAPI
{

    @Autowired
    SettlementDetailsService settlementDetailsService;

    @RequestMapping("/getRefDataSettlementDetails")
    public String greeting(@RequestParam(value="settlementID", defaultValue="S2") String settlementDetailsId) {

        String test = "";
        for(SettlementDetails sd : settlementDetailsService.listAll())
        {
            test += sd.getSettlement_key() + "::";
        }
        return "" + test;
    }

}
