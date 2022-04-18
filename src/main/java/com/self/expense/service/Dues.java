package com.self.expense.service;

import com.self.expense.domain.DebtComparator;
import com.self.expense.domain.Expense;
import com.self.expense.domain.Pair;
import com.self.expense.enums.Status;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
Class to execute 'DUES' command
 */
public class Dues implements ICommand
{

    private Map<String, Expense> memberNameToExpense;
    private DebtComparator debtComparator;

    public Dues(Map<String, Expense> memberNameToExpense, DebtComparator debtComparator)
    {
        super();
        this.memberNameToExpense = memberNameToExpense;
        this.debtComparator = debtComparator;
    }

    @Override
    public String invokeCommand(String[] command)
    {
        List<Pair<String, Integer>> memberNameToDebts = new ArrayList<>();
        Iterator<String> memberNames = memberNameToExpense.keySet().iterator();

        Expense expense = memberNameToExpense.get(command[1]);

        /*If there is no member for which command is being executed, then it is considered as a
        bogus member
         */
        if (null == expense)
        {
            return Status.MEMBER_NOT_FOUND.name();
        }

        /*
        for each member in the set except the member for which dues has to be calculated, collect
        owed amount details. In case, the member does not owe any amount to a member in the set,
        consider dues as 0
         */
        while (memberNames.hasNext())
        {
            String memberName = memberNames.next();
            if (!memberName.equals(command[1]))
            {
                memberNameToDebts.add(new Pair<>(memberName,
                    expense.getMemberNameToDebt().getOrDefault(memberName, 0)));
            }
        }

        //sort list in descending order of second value, then ascending order of first value
        Collections.sort(memberNameToDebts, debtComparator);

        StringBuilder memberNameToDebtBuilder = new StringBuilder();
        memberNameToDebts.stream().forEach(
            pair -> memberNameToDebtBuilder.append(pair.getFirst()).append(" ")
                .append(pair.getSecond())
                .append("\n"));

        return memberNameToDebtBuilder.toString().trim();
    }

}
