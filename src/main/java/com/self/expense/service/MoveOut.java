package com.self.expense.service;

import com.self.expense.domain.Expense;
import com.self.expense.enums.Status;
import java.util.Map;

/*
Class to execute 'MOVE_OUT' command
 */
public class MoveOut implements ICommand
{

    private Map<String, Expense> memberNameToExpense;

    public MoveOut(Map<String, Expense> memberNameToExpense)
    {
        super();
        this.memberNameToExpense = memberNameToExpense;
    }

    @Override
    public String invokeCommand(String[] command)
    {
        String memberName = command[1];

        /*If there is no member for which command is being executed, then it is considered as a
        bogus member
         */
        if (!memberNameToExpense.containsKey(memberName))
        {
            return Status.MEMBER_NOT_FOUND.name();
        }

        /*
        remove member from map, in case the member neither owes any amount to anyone, nor has lent
        any amount to anyone
         */
        if (memberNameToExpense.get(memberName).getTotalExpense() == 0
            && memberNameToExpense.get(memberName).getMemberNameToDebt().size() == 0)
        {
            memberNameToExpense.remove(memberName);
            return Status.SUCCESS.name();
        }
        return Status.FAILURE.name();
    }

}
