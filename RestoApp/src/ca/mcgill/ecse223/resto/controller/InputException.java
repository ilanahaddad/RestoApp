package ca.mcgill.ecse223.resto.controller;

/**
 * Custom exception to handle any user invalid input
 * @param msg   Message explaining the error
 */
public class InputException extends Exception
{
    private static final long serialVersionUID = 1L;

    public InputException(String msg) 
    {
        super(msg); 
    }
}