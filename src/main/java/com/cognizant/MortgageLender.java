package com.cognizant;

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

    public void applyForLoan(Loan loan, long requestedLoanAmount) {
        if (qualify(loan, requestedLoanAmount)){
            if (loan.getQualification()==Qualification.Qualified || loan.getQualification()==Qualification.PartialQualified){
                loan.setLoanStatus(Status.Qualified);
                loan.setLoanAmount(requestedLoanAmount);
            }
            else {
                loan.setLoanStatus(Status.Denied);
                loan.setLoanAmount(0);
            }
        }


    }

    private boolean qualify(Loan loan, long requestedLoanAmount) {
        if( loan.getDti()<36 && loan.getCredit_score()>620 && loan.checkSavings(requestedLoanAmount)){

            loan.setQualification(Qualification.Qualified);
            return true;

        }
        else if (loan.getDti()<36 && loan.getCredit_score()>620 && (loan.getSavings()*4)== requestedLoanAmount) {
            loan.setQualification(Qualification.PartialQualified);
            return true;
        }
        else {
            loan.setQualification(Qualification.notQualified);
            return false;
        }
    }
}
