package com.self.expense.config;

import com.self.expense.domain.DebtComparator;
import com.self.expense.domain.Expense;
import com.self.expense.enums.Command;
import com.self.expense.service.ClearDue;
import com.self.expense.service.Dues;
import com.self.expense.service.ICommand;
import com.self.expense.service.MinimizeCashFlowService;
import com.self.expense.service.MoveIn;
import com.self.expense.service.MoveOut;
import com.self.expense.service.Spend;
import java.util.Map;

/*
Configuration class to initialize variables on bootstrap of application
 */
public class Configuration
{

    private final Map<Command, ICommand> commandToCommandClazz;
    private Map<String, Expense> memberNameToExpenses;

    public Configuration(Map<Command, ICommand> commandToCommandClazz,
        Map<String, Expense> memberNameToExpenses)
    {
        this.commandToCommandClazz = commandToCommandClazz;
        this.memberNameToExpenses = memberNameToExpenses;
    }

    /*
    returns corresponding class for command in execution from commands map
     */
    public ICommand fromCommand(Command command)
    {
        return commandToCommandClazz.get(command);
    }

    /*
    Fill commands map to their respective classes. This will invoke execute() method dynamically on
    the basis of command in execution.
     */
    public void initialize()
    {
        commandToCommandClazz.put(Command.MOVE_IN, moveIn());
        commandToCommandClazz.put(Command.MOVE_OUT, moveOut());
        commandToCommandClazz.put(Command.CLEAR_DUE, clearDue());
        commandToCommandClazz.put(Command.DUES, dues());
        commandToCommandClazz.put(Command.SPEND, spend());
    }

    public MoveIn moveIn()
    {
        return new MoveIn(this.memberNameToExpenses);
    }

    public MoveOut moveOut()
    {
        return new MoveOut(this.memberNameToExpenses);
    }

    public ClearDue clearDue()
    {
        return new ClearDue(this.memberNameToExpenses);
    }

    public Dues dues()
    {
        return new Dues(this.memberNameToExpenses, debtComparator());
    }

    public Spend spend()
    {
        return new Spend(this.memberNameToExpenses, minimizeCashFlowService());
    }

    public DebtComparator debtComparator()
    {
        return new DebtComparator();
    }

    public MinimizeCashFlowService minimizeCashFlowService()
    {
        return new MinimizeCashFlowService();
    }

    //destroy map
    public void destroy()
    {
        commandToCommandClazz.clear();
        memberNameToExpenses = null;
    }
}
