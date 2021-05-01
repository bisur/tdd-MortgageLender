package com.cognizant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class MortgageLenderTest {

private MortgageLender mortgageLender;


    @BeforeEach
    public void setup(){
         mortgageLender= new MortgageLender();
    }
    /**
     * When I check my available funds
     * Then I should see how much funds I currently have
     */


    @Test
    public void checkAvailableFundsWithNOAccountExist(){
        //fail();
        HashMap<String,Fund> funds= mortgageLender.getAvailableFunds("Bisrat");
        assertNull(funds,"account exist");

    }

    @Test
    public void checkAvailableFundsWith(){
        //fail();
        Fund fund= new Fund(100000,"Bisrat");
        mortgageLender.addAccount(fund.getName(),fund);
        HashMap<String,Fund> funds= mortgageLender.getAvailableFunds("Bisrat");
        assertEquals(100000,funds.get("Bisrat").getTotal());

    }


    /**
     * Given I have <current_amount> available funds
     * When I add <deposit_amount>
     * Then my available funds should be <total>
     *
     * Examples:
     * | current_amount | deposit_amount |   total  |
     * |     100,000    |      50,000    | 150,000  |
     * |     200,000    |      30,000    | 230,000  |
     */

    @Test
    public void checkAvailableFundDetails(){
        //fail();
        Fund fund= new Fund(100000,"Bisrat");
        mortgageLender.addAccount(fund.getName(),fund);
        mortgageLender.addDeposit(50000, "Bisrat");
      HashMap<String,Fund> funds= mortgageLender.getAvailableFunds("Bisrat");
        assertEquals(150000,funds.get("Bisrat").getTotal());

    }
}
