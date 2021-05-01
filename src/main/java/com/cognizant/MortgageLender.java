package com.cognizant;

import java.util.HashMap;

public class MortgageLender {

    private HashMap<String,Fund> funds= new HashMap<>();
    private long availableFundsForLoan;

    public long getAvailableFundsForLoan() {
        return availableFundsForLoan;
    }

    public void setAvailableFundsForLoan(long availableFundsForLoan) {
        this.availableFundsForLoan = availableFundsForLoan;
    }

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

    public String applyForLoan(Loan loan, long requestedLoanAmount) {
        if (qualify(loan, requestedLoanAmount)){
            if(getAvailableFundsForLoan()==0){
                if (loan.getQualification()==Qualification.Qualified || loan.getQualification()==Qualification.PartialQualified){
                    loan.setLoanStatus(Status.Qualified);
                    loan.setLoanAmount(requestedLoanAmount);
                }
                else {
                    loan.setLoanStatus(Status.Denied);
                    loan.setLoanAmount(0);

                }
            }
           else {
               if (getAvailableFundsForLoan()>=requestedLoanAmount && (loan.getQualification()==Qualification.Qualified || loan.getQualification()==Qualification.PartialQualified)){
                    loan.setLoanStatus(Status.Approved);
                    setAvailableFundsForLoan(getAvailableFundsForLoan()-requestedLoanAmount);
               }else{
                   loan.setLoanStatus(Status.OnHold);
               }
                loan.setLoanAmount(requestedLoanAmount);

            }
            return null;
        }
        return "warning to not proceed";

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
