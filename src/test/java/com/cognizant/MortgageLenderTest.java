package com.cognizant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    public void checkAvailableFundsWhenAccountExists(){
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
    /**
     * Rule: To qualify for the full amount, candidates must have debt-to-income (DTI) less than 36%,
     * credit score above 620
     * and savings worth 25% of requested loan amount.
     *
     * Rule: To partially qualify, candidates must still meet the same dti and credit score thresholds.
     * The loan amount for partial qualified applications is four times the applicant's savings.
     *
     * Given a loan applicant with <dti>, <credit_score>, and <savings>
     * When they apply for a loan with <requested_amount>
     * Then their qualification is <qualification>
     * And their loan amount is <loan_amount>
     * And their loan status is <status>
     */

    @Test
    public void checkApplicantQualificationForLoan(){
        //fail();

        Loan loan= new Loan(21,700,100000);
        mortgageLender.setAvailableFundsForLoan(0);
        mortgageLender.applyForLoan(loan,250000);
        assertEquals(Qualification.Qualified,loan.getQualification());
        assertEquals(Status.Qualified,loan.getStatus());
        assertEquals(250000,loan.getLoanAmount());

    }

    /**
     * Given I have <available_funds> in available funds
     * When I process a qualified loan
     * Then the loan status is set to <status>
     *
     * Example:
     * | loan_amount | available_funds |    status  |
     * |   125,000   |    100,000      |   on hold  |
     * |   125,000   |    200,000      |  approved  |
     * |   125,000   |    125,000      |  approved  |
     *
     * When I process a not qualified loan
     * Then I should see a warning to not proceed
     */

    @Test
    public void checkLoanStatusWhenAvailableFunds(){
        //fail();

        Loan loan= new Loan(21,700,100000);

        mortgageLender.setAvailableFundsForLoan(100000);

        mortgageLender.applyForLoan(loan,125000);
        assertEquals(Status.OnHold,loan.getStatus());

        mortgageLender.setAvailableFundsForLoan(200000);
        mortgageLender.applyForLoan(loan,125000);

        assertEquals(Status.Approved,loan.getStatus(),"loan1 approved");
        mortgageLender.setAvailableFundsForLoan(125000);
        mortgageLender.applyForLoan(loan,125000);

        assertEquals(Status.Approved,loan.getStatus(),"loan2 approved");

        Loan loan2= new Loan(38,700,100000);
        mortgageLender.setAvailableFundsForLoan(0);
        String result= mortgageLender.applyForLoan(loan2,125000);

        assertEquals("warning to not proceed",result);


    }

    /**
     * Given I have approved a loan
     * Then the requested loan amount is moved from available funds to pending funds
     * And I see the available and pending funds reflect the changes accordingly
     */@Test
    public void checkAnApprovedLoanReviewsAvailableFunds(){
        //fail();

        Loan loan= new Loan(21,700,100000);
        long currentAvailableFunds= 200000;
        mortgageLender.setAvailableFundsForLoan(currentAvailableFunds);
        mortgageLender.applyForLoan(loan,125000);
        assertEquals(75000,mortgageLender.getAvailableFundsForLoan());




    }


}
