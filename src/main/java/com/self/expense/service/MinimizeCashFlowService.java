package com.self.expense.service;

import com.self.expense.domain.Expense;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class MinimizeCashFlowService
{

    /*
    The method below makes use of minimizing cash flow algorithm. The steps to minimize cash flow
     are:
    1) Find max creditor (+) in map
    2) Find max debtor (-) in map
    3) If both are equal to zero, it means that we have reached at a point where cash flow is
    already minimized, otherwise go to step 4
    4) Find minimum of max creditor and max debitor, so that the same can be balanced between two
    5) Subtract minimum amount from creditor and add minimum amount to debitor.
    6) Recur from step 1 to step 5 until step 3 is achieved.
     */
    public void minimizeCashFlow(Map<String, Expense> memberNametoExpense)
    {
        Map<String, Integer> memberNameToCreditOrDebt = new HashMap<>();

        //fill totalDebtOrCreditAmountMap with totalAmountInDebtOrCredit values
        memberNametoExpense.entrySet().stream().forEach(entry -> {
            memberNameToCreditOrDebt.put(entry.getKey(), entry.getValue().getTotalExpense());

            //reinitialize debt meta data
            entry.getValue().setMemberNameToDebt(new HashMap<>());
        });

        minimize(memberNametoExpense, memberNameToCreditOrDebt);
    }

    private void minimize(Map<String, Expense> memberNametoExpense,
        Map<String, Integer> memberNameToCreditOrDebt)
    {
        //find max creditor and max debitor
        Entry<String, Integer> maxCreditor = null;
        Entry<String, Integer> maxDebtor = null;

        Optional<Entry<String, Integer>> maxCreditorOptional = memberNameToCreditOrDebt.entrySet()
            .stream()
            .max(Entry.comparingByValue());
        Optional<Entry<String, Integer>> maxDebtorOptional = memberNameToCreditOrDebt.entrySet()
            .stream()
            .min(Entry.comparingByValue());

        if (maxCreditorOptional.isPresent())
        {
            maxCreditor = maxCreditorOptional.get();
        }

        if (maxDebtorOptional.isPresent())
        {
            maxDebtor = maxDebtorOptional.get();
        }

        /*if both values are not equal to 0, find minimum of two. subtract minOfTwo from creditor
         and add minOfTwo to debtor. Finally, fill debtMetaData map. This map contains minimized
         debt of a member
         */
        if (null != maxCreditor && null != maxDebtor && maxCreditor.getValue() != 0
            && maxDebtor.getValue() != 0)
        {
            int minOfTwo = Math.min(maxCreditor.getValue(), -(maxDebtor.getValue()));
            memberNameToCreditOrDebt.compute(maxCreditor.getKey(), (key, val) -> val -= minOfTwo);
            memberNameToCreditOrDebt.compute(maxDebtor.getKey(), (key, val) -> val += minOfTwo);

            Entry<String, Integer> finalMaxCreditor = maxCreditor;
            memberNametoExpense.compute(maxDebtor.getKey(), (key, val) -> {
                val.getMemberNameToDebt().put(finalMaxCreditor.getKey(), minOfTwo);
                return val;
            });

            //recursive call
            minimize(memberNametoExpense, memberNameToCreditOrDebt);
        }
    }
}
