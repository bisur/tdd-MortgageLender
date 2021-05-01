package com.cognizant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class MortgageLenderTest {

private MortgageLender mortgageLender;
private Loan loan1;
    private Loan loan2;


    @BeforeEach
    public void setup(){
        mortgageLender= new MortgageLender();
         loan1= new Loan("Bisrat",21,700,100000);
         loan2= new Loan("Edgar",38,700,100000);

    }
    /**
     * When I check my available funds
     * Then I should see how much funds I currently have
     */


    @Test
    public void checkAvailableFundsWithNOAccountExist(){
        //fail();
        HashMap<String,Fund> funds= mortgageLender.getAvailableLoans("Bisrat");
        assertNull(funds,"account exist");

    }

    @Test
    public void checkAvailableFundsWhenAccountExists(){
        //fail();
        Fund fund= new Fund(100000,"Bisrat");
        mortgageLender.addAccount(fund.getName(),fund);

        mortgageLender.addAccount(fund.getName(),fund);
        HashMap<String,Fund> funds= mortgageLender.getAvailableLoans("Bisrat");
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
        HashMap<String,Fund> funds= mortgageLender.getAvailableLoans("Bisrat");
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

        mortgageLender.setAvailableFundsForLoan(0);
        mortgageLender.applyForLoan(loan1,250000);
        assertEquals(Qualification.Qualified,loan1.getQualification());
        assertEquals(Status.Qualified,loan1.getStatus());
        assertEquals(250000,loan1.getLoanAmount());

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


        mortgageLender.setAvailableFundsForLoan(100000);

        mortgageLender.applyForLoan(loan1,125000);
        assertEquals(Status.OnHold,loan1.getStatus());

        mortgageLender.setAvailableFundsForLoan(200000);
        mortgageLender.applyForLoan(loan1,125000);

        assertEquals(Status.Approved,loan1.getStatus(),"loan1 approved");
        mortgageLender.setAvailableFundsForLoan(125000);
        mortgageLender.applyForLoan(loan1,125000);

        assertEquals(Status.Approved,loan1.getStatus(),"loan2 approved");


        mortgageLender.setAvailableFundsForLoan(0);
        String result= mortgageLender.applyForLoan(loan2,125000);

        assertEquals("warning to not proceed",result);


    }

    /**
     * Given I have approved a loan
     * Then the requested loan amount is moved from available funds to pending funds
     * And I see the available and pending funds reflect the changes accordingly
     */
    @Test
    public void checkAnApprovedLoanReviewsAvailableFunds(){
        //fail();


        long currentAvailableFunds= 200000;
        mortgageLender.setAvailableFundsForLoan(currentAvailableFunds);
        mortgageLender.setPendingFundsForLoan(0);
        mortgageLender.applyForLoan(loan1,125000);
        long pendingFunds= mortgageLender.getPendingFundsForLoan();
        assertEquals(125000,pendingFunds);
        assertTrue(currentAvailableFunds>mortgageLender.getAvailableFundsForLoan());
    }

    /**
     * Given I have an approved loan
     * When the applicant accepts my loan offer
     * Then the loan amount is removed from the pending funds
     * And the loan status is marked as accepted
     *
     * Given I have an approved loan
     * When the applicant rejects my loan offer
     * Then the loan amount is moved from the pending funds back to available funds
     * And the loan status is marked as rejected
     */
    @Test
    public void checkLoanStatusAfterLoanAcceptedOrRejected(){
        //fail();
        mortgageLender.setAvailableFundsForLoan(125000);
        mortgageLender.applyForLoan(loan1,125000);
        long pendingFunds= mortgageLender.getPendingFundsForLoan();
        mortgageLender.acceptLoanOffer(loan1);

        assertEquals(Status.Accepted,loan1.getStatus(),"loan2 approved");
        long actualPendingFunds= mortgageLender.getPendingFundsForLoan();
        assertTrue((actualPendingFunds==pendingFunds-loan1.getLoanAmount()));

        long availableFunds= mortgageLender.getAvailableFundsForLoan();
        mortgageLender.rejectedLoanOffer(loan1);

        assertEquals(Status.Rejected,loan1.getStatus(),"loan2 approved");
        long actualAvailableFunds= mortgageLender.getAvailableFundsForLoan();
        assertTrue((actualAvailableFunds==availableFunds+loan1.getLoanAmount()));

    }

    /**
     * Rule: approved loans expired in 3 days
     *
     * Given there is an approved loan offered more than 3 days ago
     * When I check for expired loans
     * Then the loan amount is move from the pending funds back to available funds
     * And the loan status is marked as expired
     */
    @Test
    public void checkIfUnDecidedLoan(){
        //fail();


        mortgageLender.setAvailableFundsForLoan(125000);
        mortgageLender.applyForLoan(loan1,125000);

        long availableFunds= mortgageLender.getAvailableFundsForLoan();
        mortgageLender.CheckExpiredLoans();
//        assertEquals(Status.Expired,loan1.getStatus());
//        long actualAvailableFunds= mortgageLender.getAvailableFundsForLoan();
//        assertTrue((actualAvailableFunds>availableFunds));

    }
}
