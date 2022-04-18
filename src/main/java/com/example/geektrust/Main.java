package com.example.geektrust;

import com.example.geektrust.config.Configuration;
import com.example.geektrust.service.ExpenseServiceImpl;
import com.example.geektrust.service.IExpenseService;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedHashMap;

/*
This is the main class of your program which will be executed
 */
public class Main
{

    public static void main(String[] args) throws IOException
    {
        /*
        initialize necessary configurations
         */
        Configuration configuration = new Configuration(new HashMap<>(), new LinkedHashMap<>());
        configuration.initialize();

        /*
        Takes in input file name to execute, runs command one by one present in the file and prints
        output
         */
        try (
            Reader reader = new FileReader(args[0]);
            BufferedReader bufferedReader = new BufferedReader(reader))
        {

            String command;
            IExpenseService service = new ExpenseServiceImpl(configuration);

            while ((command = bufferedReader.readLine()) != null)
            {
                System.out.println(service.execute(command));
            }
        }

    }
}
