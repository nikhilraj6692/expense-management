package com.self.expense.service;

import com.self.expense.domain.Expense;
import com.self.expense.enums.Status;
import java.util.HashMap;
import java.util.Map;

/*
Class to execute 'MOVE_IN' command
 */
public class MoveIn implements ICommand
{

    private Map<String, Expense> memberNameToExpense;

    public MoveIn(Map<String, Expense> memberNameToExpense)
    {
        super();
        this.memberNameToExpense = memberNameToExpense;
    }

    @Override
    public String invokeCommand(String[] command)
    {
        //should not allow to add if map already contains maximum allowable number of members
        if (memberNameToExpense.size() == MAX_MEMBERS)
        {
            return Status.HOUSEFUL.name();
        } else
        {
            //put member's name and initialize value
            memberNameToExpense.put(command[1], new Expense(new HashMap<>()));
        }

        return Status.SUCCESS.name();
    }

}
