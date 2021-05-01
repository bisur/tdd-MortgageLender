package com.cognizant;

import java.util.ArrayList;
import java.util.HashMap;

public class MortgageLender {

    private HashMap<String,Fund> funds= new HashMap<>();




    public HashMap<String,Fund> getAvailableFunds(String fundName) {
        if(funds.containsKey(fundName)) {
            return funds;
        }
        else {
            return null;
        }
    }

    public void addDeposit(long deposit_amount, String fundName ) {
        if(funds.containsKey(fundName)) {
            funds.get(fundName).addDeposit(deposit_amount);
        }
    }


    public void addAccount(String name, Fund fund) {
       funds.put(name,fund);
    }
}
