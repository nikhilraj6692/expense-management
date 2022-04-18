package com.example.geektrust.service;

@FunctionalInterface
public interface ICommand
{

    /*
    max number of members allowed in a group
     */
    int MAX_MEMBERS = 3;

    String invokeCommand(String[] command);
}
