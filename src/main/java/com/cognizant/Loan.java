package com.cognizant;

public class Loan {
    private String loanId;
    private double dti;
    private long credit_score;
    private long savings;
    private long loanAmount;
    private Status loanStatus;
    //private DateTime dateRequested ;


    public long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(long loanAmount) {
        this.loanAmount = loanAmount;
    }



    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    private Qualification qualification;
    public Status getLoanStatus() {
        return loanStatus;
    }

    /**
     * must have debt-to-income (DTI) less than 36%,
     * credit score above 620
     * and savings worth 25% of requested loan amount.
     */
    public void setLoanStatus(Status loanStatus) {
            this.loanStatus=loanStatus;
    }


    public Loan(String id ,long dti, long credit_score, long savings) {
        this.dti = dti;
        this.credit_score = credit_score;
        this.savings = savings;
        this.loanId=id;
    }

    public double getDti() {
        return dti;
    }

    public void setDti(long dti) {
        this.dti = dti;
    }

    public long getCredit_score() {
        return credit_score;
    }

    public void setCredit_score(long credit_score) {
        this.credit_score = credit_score;
    }

    public long getSavings() {
        return savings;
    }

    public void setSavings(long savings) {
        this.savings = savings;
    }

    public Status getStatus() {
        return loanStatus;
    }

    public boolean checkSavings(long requestedLoanAmount) {
        return getSavings()>=(requestedLoanAmount*0.25);
    }

    public String getId() {
        return loanId;
    }
}
