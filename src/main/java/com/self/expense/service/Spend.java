package com.self.expense.service;

import com.self.expense.domain.Expense;
import com.self.expense.enums.Status;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/*
Class to execute 'SPEND' command
 */
public class Spend implements ICommand
{

    private Map<String, Expense> memberNameToExpense;
    private MinimizeCashFlowService minimizeCashFlowService;

    public Spend(Map<String, Expense> memberNameToExpense,
        MinimizeCashFlowService minimizeCashFlowService)
    {
        this.memberNameToExpense = memberNameToExpense;
        this.minimizeCashFlowService = minimizeCashFlowService;
    }

    @Override
    public String invokeCommand(String[] command)
    {
        int amountSpent = Integer.parseInt(command[1]);
        String amountSpentBy = command[2];
        String[] amountSpentAmong = Arrays.copyOfRange(command, 2, command.length);

        /*
        check all members involved in amountSpentAmong[]. in case, any member found in
        array which is not found in the map, then it is considered as bogus member
         */
        Optional<String> bogusMember = Arrays.stream(amountSpentAmong)
            .filter(memberName -> !memberNameToExpense.containsKey(memberName)).findAny();

        if (bogusMember.isPresent())
        {
            return Status.MEMBER_NOT_FOUND.name();
        }

        //calculate share accordingly and put in map. - is for debt and + is for credit
        int sharePerMember = amountSpent / amountSpentAmong.length;

        //credit sharePerMember * (amountSpentAmong.length - 1) against creditor entry in map
        memberNameToExpense.compute(amountSpentBy, (key, val) -> {
            val.setTotalExpense(
                val.getTotalExpense() + sharePerMember * (amountSpentAmong.length
                    - 1));
            return val;
        });

        for (int i = 1; i < amountSpentAmong.length; i++)
        {
            //debit sharePerMember against debitor entry in map
            memberNameToExpense.compute(amountSpentAmong[i], (key, val) -> {
                val.setTotalExpense(
                    val.getTotalExpense() - sharePerMember);
                return val;
            });

        }

        //minimize cash flow among members
        minimizeCashFlowService.minimizeCashFlow(memberNameToExpense);

        return Status.SUCCESS.name();
    }

}
