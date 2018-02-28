package ca.mcgill.ecse223.resto.controller;

/**
 * Custom exception to handle any user invalid input
 * @param msg   Message explaining the error
 */
public class InvalidInputException extends Exception
{
    private static final long serialVersionUID = 1L;

    public InvalidInputException(String msg)
    {
        super(msg); 
    }
}