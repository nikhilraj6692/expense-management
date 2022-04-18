package com.example.geektrust.service;

import com.example.geektrust.config.Configuration;
import com.example.geektrust.enums.Command;
import com.example.geektrust.enums.Status;

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
