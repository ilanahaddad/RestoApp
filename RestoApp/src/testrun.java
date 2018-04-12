import java.util.Scanner;

public class testrun {
	public static void main(String[] args) {
		new testcontroller();
        System.out.println("Welcome To Table Manager!");

    	//Scanner reader = new Scanner(System.in);
        while (true) {  // Reading from System.in
        	Scanner reader = new Scanner(System.in);
        	System.out.println("What do you want to do? ");
        	String n = reader.next(); // Scans the next token of the input as an         
        	//reader.close();
        	switch (n) {
        	case "list": testcontroller.ListTables();
        	break;
        	case "move": testcontroller.MoveTable();
        	break;
        	case "details": testcontroller.details();
        	break;
        	case "add": testcontroller.AddTable();
        	break;
        	case "remove": testcontroller.RemoveTable();
        	break;
        	case "exit": reader.close(); return;
        	}
        
        }
	}
}
