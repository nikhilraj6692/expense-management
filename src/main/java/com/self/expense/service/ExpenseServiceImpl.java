package com.self.expense.service;

import com.self.expense.config.Configuration;
import com.self.expense.enums.Command;
import com.self.expense.enums.Status;

/*
Service class to render command string and execute command
 */
public class ExpenseServiceImpl implements IExpenseService
{

    private Configuration configuration;

    public ExpenseServiceImpl(Configuration configuration)
    {
        this.configuration = configuration;
    }

    @Override
    public String execute(String commandStr)
    {
        try
        {
            //get and invoke class corresponding to command in execution
            String[] commandStrArr = commandStr.split(" ");
            Command command = Command.valueOf(commandStrArr[0]);
            ICommand commandClazz = configuration.fromCommand(command);

            return commandClazz.invokeCommand(commandStrArr);
        } catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            return Status.FAILURE.name();
        }
    }
}
