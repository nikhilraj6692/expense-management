package com.self.expense.domain;

import java.util.Comparator;

/*
Comparator to sort on the basis of amount in descending order (pair.second) and if amount is same,
then sort on the basis of name in ascending order (pair.first). It is used in showing final dues of
a member
 */
public class DebtComparator implements Comparator<Pair<String, Integer>>
{

    @Override
    public int compare(Pair<String, Integer> memberNameToDebt1,
        Pair<String, Integer> memberNameToDebt2)
    {
        if (memberNameToDebt1.getSecond().equals(memberNameToDebt2.getSecond()))
        {
            return memberNameToDebt1.getFirst().compareTo(memberNameToDebt2.getFirst());
        } else
        {
            return memberNameToDebt2.getSecond() - memberNameToDebt1.getSecond();
        }
    }

}
