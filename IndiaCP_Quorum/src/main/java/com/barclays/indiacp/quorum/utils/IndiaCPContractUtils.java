package com.barclays.indiacp.quorum.utils;

import com.barclays.indiacp.model.CPProgram;
import com.jpmorgan.cakeshop.client.model.Contract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikhil on 29/12/16.
 */
public class IndiaCPContractUtils {

    public static ArrayList<CPProgram> fetchCPPrograms() {

        final String nameFilter = "CPProgram";
        ArrayList<CPProgram> cpProgramArrayList = new ArrayList<>();
        List<Contract> contractList = CakeshopUtils.listContractsByName(nameFilter);

        for(Contract contract: contractList){
            cpProgramArrayList.add(fetchCPProgram(contract.getAddress()));
        }

        return cpProgramArrayList;
    }

    public static CPProgram fetchCPProgram(String cpProgramAddress) {

        return (CPProgram) CakeshopUtils.getContractState(cpProgramAddress);
    }
}
