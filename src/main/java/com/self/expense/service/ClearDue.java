package com.self.expense.service;

import com.self.expense.domain.Expense;
import com.self.expense.enums.Status;
import java.util.Map;

/*
Class to execute 'CLEAR_DUE' command
 */
public class ClearDue implements ICommand
{

    private Map<String, Expense> memberToDebts;

    public ClearDue(Map<String, Expense> memberToDebts)
    {
        super();
        this.memberToDebts = memberToDebts;
    }

    @Override
    public String invokeCommand(String[] command)
    {
        String amountClearedBy = command[1];
        String amountClearedTo = command[2];
        int amountCleared = Integer.parseInt(command[3]);

        Expense debtorExpense = memberToDebts.get(amountClearedBy);
        Expense creditorExpense = memberToDebts.get(amountClearedTo);

        /*If there is no member for which command is being executed, then it is considered as a
        bogus member
         */
        if (null == debtorExpense || null == creditorExpense)
        {
            return Status.MEMBER_NOT_FOUND.name();
        }

        /*
        if debtMetaData of 'amountClearedBy' member contains a member for which dues has to be
        cleared ('amountClearedTo') and the amount to be cleared is less than or equal to the amount
         owed, then
        1) if total amount owed is cleared, remove 'amountClearedTo' member entry from debtMetaData
        map, since there is no more dues corresponding to 'amountClearedTo' member
        2) if partial amount owed is cleared, update dues corresponding to 'amountClearedTo' member

        Also, update totalAmountInDebtOrCredit of 'amountClearedTo' member
         */
        if (debtorExpense.getMemberNameToDebt().containsKey(amountClearedTo)
            && debtorExpense.getMemberNameToDebt().get(amountClearedTo) >= amountCleared)
        {
            int currDebt = debtorExpense.getMemberNameToDebt().get(amountClearedTo);
            if (currDebt - amountCleared == 0)
            {
                debtorExpense.getMemberNameToDebt().remove(amountClearedTo);
            } else
            {
                debtorExpense.getMemberNameToDebt()
                    .put(amountClearedTo, currDebt - amountCleared);
            }

            memberToDebts.get(amountClearedBy).setTotalExpense(
                memberToDebts.get(amountClearedBy).getTotalExpense() + amountCleared);
            memberToDebts.get(amountClearedTo).setTotalExpense(
                memberToDebts.get(amountClearedTo).getTotalExpense() - amountCleared);
        } else
        {
            //in case of dues to be settled is greater than amount owed
            return Status.INCORRECT_PAYMENT.name();
        }

        int amtRemaining = debtorExpense.getMemberNameToDebt()
            .getOrDefault(amountClearedTo, 0);
        return String.valueOf(amtRemaining);
    }

}
