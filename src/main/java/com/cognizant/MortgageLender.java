package com.cognizant;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class MortgageLender {

    private HashMap<String,Fund> funds= new HashMap<>();
    private HashMap<String,Loan> loans= new HashMap<>();

    private long availableFundsForLoan;
    private long pendingFundsForLoan;


    public long getAvailableFundsForLoan() {
        return availableFundsForLoan;
    }

    public void setAvailableFundsForLoan(long availableFundsForLoan) {
        this.availableFundsForLoan = availableFundsForLoan;
    }

    public HashMap<String,Fund> getAvailableLoans(String fundName) {
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
                    setPendingFundsForLoan(requestedLoanAmount);

               }else{
                   loan.setLoanStatus(Status.OnHold);
               }
                loan.setLoanAmount(requestedLoanAmount);

            }
           loan.setDateRequested(LocalDateTime.now());
           loans.put(loan.getId() ,loan);
            return null;
        }
        loan.setDateRequested(LocalDateTime.now());
        loans.put(loan.getId() ,loan);
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

    public void setPendingFundsForLoan(long pendingFundsForLoan) {
        if (this.pendingFundsForLoan>0){
            this.pendingFundsForLoan += pendingFundsForLoan;

        }
        else {
            this.pendingFundsForLoan = pendingFundsForLoan;
        }
        setAvailableFundsForLoan(getAvailableFundsForLoan()-pendingFundsForLoan);

    }

    public long getPendingFundsForLoan() {
        return pendingFundsForLoan;
    }

    public void acceptLoanOffer(Loan loan) {
        this.pendingFundsForLoan -= loan.getLoanAmount();
        loan.setLoanStatus(Status.Accepted);
    }

    public void rejectedLoanOffer(Loan loan) {
        this.pendingFundsForLoan -= loan.getLoanAmount();
        this.availableFundsForLoan += loan.getLoanAmount();
        loan.setLoanStatus(Status.Rejected);
    }

    public void CheckExpiredLoans() {
        Iterator it = loans.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Loan loan=(Loan)pair.getValue();
            boolean isAfter = LocalDateTime.now().isAfter(loan.getDateRequested());
            if( isAfter){
                loans.get(loan.getId()).setLoanStatus(Status.Expired);
                returnPendingFund(loan.getLoanAmount());
            };
            it.remove(); // avoids a ConcurrentModificationException
        }

    }

    private void returnPendingFund(long loanAmount) {
        this.pendingFundsForLoan-=loanAmount;
        this.availableFundsForLoan+=loanAmount;
    }

    public Map<String,Loan>  searchLoanByStatus(Status approved) {

        Map<String,Loan> result = loans.entrySet()
                .stream()
                .filter(map -> ((Loan)map.getValue()).getLoanStatus() == approved)
                .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));

        return result;
    }
}
