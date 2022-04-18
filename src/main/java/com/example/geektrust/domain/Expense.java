package com.example.geektrust.domain;

import java.util.Map;

/*
Class to contain debt and credit info a member
 */
public class Expense
{

    //- means in debt (to pay someone) and + means in credit (to receive from someone)
    private int totalExpense;

    private Map<String, Integer> memberNameToDebt;

    public Expense(Map<String, Integer> memberNameToDebt)
    {
        this.memberNameToDebt = memberNameToDebt;
    }

    public int getTotalExpense()
    {
        return totalExpense;
    }

    public void setTotalExpense(int totalExpense)
    {
        this.totalExpense = totalExpense;
    }

    public Map<String, Integer> getMemberNameToDebt()
    {
        return memberNameToDebt;
    }

    public void setMemberNameToDebt(Map<String, Integer> memberNameToDebt)
    {
        this.memberNameToDebt = memberNameToDebt;
    }
}
