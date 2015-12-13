import java.io.*;
import java.util.*;
import java.text.*;

/**
 * This program implements an imagined ticket kiosk system called Quibble.
 * It reads in and stores a list of current events at the beginning of each user session,
 * 		and outputs a record of all transactions completed at the end of each user session.
 * This program is designed to behave as a real-life ticket kiosk would, so its functionality is
 * 		implemented within an infinite loop, with an infinite number of user sessions theoretically possible.
 * 
 * @author Chris Thomas, 10066835
 * @author Nicholas Smith, 10098522
 */
public class MainClass {

	// related lists containing data about all events currently on file
	static ArrayList<String> names;
	static ArrayList<String> dates;
	static ArrayList<Integer> tickets;
	static ArrayList<Boolean> forSale;

	// boolean indicating if this is an admin session or a sales session
	static boolean admin;

	// strings storing the current daily output file
	static ArrayList<String> transactions;
	
	static Scanner sc = new Scanner(System.in);


	/**
	 * The main method loops through an infinite number of Quibble kiosk sessions.
	 * @param args: unused
	 */
	public static void main (String[] args) {

		while(true){
			names = new ArrayList<String>();
			dates = new ArrayList<String>();
			tickets = new ArrayList<Integer>();
			forSale = new ArrayList<Boolean>();
			admin = false;
			transactions = new ArrayList<String>();
			
			String line = "";

			// prompt the user to log in
			System.out.println("");
			System.out.println("Type 'login' to begin.");
			line = sc.nextLine();

			// no command should be accepted except "login"
			if (line.equalsIgnoreCase("login")){
				// call the kiosk method upon successful login
				kiosk();
			}
			else{
				System.out.println("Try again.");
			}
		}
	}

	/**
	 * The kiosk method implements the functionality of the Quibble kiosk system, as described in the requirements document.
	 */
	public static void kiosk() {
		// initialize the scanner and input string
		String input = "";

		System.out.println("");
		System.out.println("Welcome to the Queens Kiosk Master. Sales or admin?");

		// logs the user in as either sales or admin
		// loop executes until user enters valid input
		while(true){
			// receive user input
			input = sc.nextLine();
			if (input.equalsIgnoreCase("admin")){
				// user is an admin
				admin = true;
				break;
			} else if (!input.equalsIgnoreCase("sales")){
				// invalid user input
				System.out.println("Please try again.  Sales or admin?");
			}
			else{
				// user is sales
				break;
			}
		}

		String line = "";
		String name = "";
		String ticketsS = "";
		int numTickets = -1;

		// try to read data from current events file
		try {
			// Scanner to read in event info from current events file
			Scanner s = new Scanner(new FileReader("currentEvents.txt"));
			
			// receive user input
			line = s.nextLine();
			
			// loop through each line in the input file
			while(!line.equals("END                 00000")) {
				
				// break up the line into the event's name and the number of tickets
				name = line.substring(0, 19);
				ticketsS = line.substring(20);
				numTickets = Integer.parseInt(ticketsS);
				
				// add current event info to the lists of event information
				names.add(name.trim());
				dates.add("000000");
				tickets.add(numTickets);
				forSale.add(true);
				
				// receive next line of user input
				line = s.nextLine();
			}
			s.close();
		}

		// if the current events file does not exist
		catch (FileNotFoundException e) {
			System.out.println("Fatal error: no current events file found.");
			e.printStackTrace();
			System.exit(-1);
			return;
		}
		
		// if there is an error reading data from the file
		catch(NoSuchElementException e){
			System.out.println("Fatal error: could not read line from file.");
			e.printStackTrace();
			System.exit(-1);
			return;
		}

		// present the user with a list of possible transactions
		// loop executes until the user logs out
		while(true){
			System.out.println("What event operation would you like to do?");
			System.out.println("Purchase tickets for an event ('sell')");
			System.out.println("Return purchased tickets ('return')");
			if(admin){
				System.out.println("Create event ('create')");
				System.out.println("Delete event ('delete')");
				System.out.println("Add tickets to an event ('add')");
			}
			System.out.println("Logout of this session ('logout')");

			// receive user input
			input = sc.nextLine();
			while (input.length() < 1)
				input = sc.nextLine();

			// switch statement on the user's possible actions
			switch (input) {
			case "create":
				// user is creating a new event
				// check if they are authorized to do so
				if(!admin){
					System.out.println("Unauthorized transaction, try again.");
				}
				else{
					// call method to create new event
					create();
				}
				break;

			case "delete":
				// user is deleting an event
				// check if they are authorized to do so
				if(!admin){
					System.out.println("Unauthorized transaction, try again.");
				}
				else{
					// call method to delete event
					delete();
				}
				break;

			case "add":
				// user is adding tickets to an event
				// check if they are authorized to do so
				if(!admin){
					System.out.println("Unauthorized transaction, try again.");
				}
				else{
					// call method to add tickets to event
					add();
				}
				break;

			case "sell":
				// user is purchasing tickets for an event
				// call method to sell tickets to the user
				sell();
				break;

			case "return":
				// user is returning previously purchased tickets
				// call method to return tickets
				returnTickets();
				break;

			case "logout":
				// user is logging out of the current session
				logout();
				return;

			default:
				// any other input is not a valid command
				System.out.println("Invalid input, try again.");
				break;
			}
		}
	}


	/**
	 * This method is called when the user is creating a new event.
	 */
	public static void create(){

		// initialize local scanner and variables
		String name = "";
		String date = "";
		int numTickets = 0;

		// loop executes until user has entered a valid event name
		while (true){
			System.out.print("Please enter a new event name: ");

			// receive user input
			name = sc.nextLine();
			if (name.length() > 20){
				// the event name is too long
				System.out.println("Event name must be <20 characters.");
			}
			else if (names.contains(name)){
				// an event with this name already exists
				System.out.println("Event already exists.  Try again.");
			}
			else{
				break;
			}
		}

		// loop executes until user has entered a valid event date
		while(true){
			System.out.print("Please enter the date of the event (YYMMDD): ");

			// attempt to receive valid user input
			try{
				// receive user input
				date = sc.nextLine();

				// format the user's input into a Date type
				SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
				Date formatted = formatter.parse(date);

				// get tomorrow's date and the date two years from now
				Calendar cal = Calendar.getInstance();
				Date today = cal.getTime();
				cal.add(Calendar.YEAR, 2);
				Date twoYears = cal.getTime();
				Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));

				if (tomorrow.after(formatted) || formatted.after(twoYears)){
					// the date is outside the allowed range
					System.out.println("Event must be between tomorrow and two years from now.  Try again.");
				}
				else{
					break;
				}
			}

			// catch illegal user input
			catch (ParseException e) {
				System.out.println("Invalid date format.  Try again.");
			}
		}

		// loop executes until user has entered a valid number of tickets
		while(true){
			System.out.print("Please enter the number of tickets for the event: ");

			// attempt to receive valid user input
			try{
				// receive user input
				numTickets = sc.nextInt();

				if (numTickets < 1 || numTickets > 99999){
					// the number of tickets is outside the allowed range
					System.out.println("Number of tickets must be between 1 and 99,999.  Try again.");
				}
				else{
					break;
				}
			}

			// catch illegal user input
			catch (InputMismatchException e){
				System.out.println("Invalid number format.  Try again.");
				sc.next();
			}
		}
		
		// add the new event to the lists containing event data
		names.add(name);
		dates.add(date);
		tickets.add(numTickets);
		forSale.add(false);
		
		// construct the output string to the daily transaction file
		String s = "03 " + name;
		for (int i = s.length(); i < 24; i++){
			s += ' ';
		}
		s += (date + ' ');
		
		String num = Integer.toString(numTickets);
		for (int i = num.length(); i < 5; i++){
			num = '0' + num;
		}
		
		s += num;
		s += '\n';
		
		// add the line of output to the list of daily transactions
		transactions.add(s);
		
		System.out.println("Event successfully created.");
		System.out.println("");
		
		return;
	}


	/**
	 * This method is called when the user is deleting an existing event.
	 */
	public static void delete(){

		// initialize local scanner and variables
		String name = "";
		int index = -1;

		// loop executes until user has entered a valid event name
		while (true){
			System.out.print("Please enter the event name: ");

			// receive user input
			name = sc.nextLine();
			if (!names.contains(name)){
				// the event does not exist
				System.out.println("Event does not exist.  Try again.");
			}
			else{
				break;
			}
		}
		
		// remove references to the event stored in the current events lists
		index = names.indexOf(name);
		names.remove(index);
		dates.remove(index);
		tickets.remove(index);
		forSale.remove(index);
		
		// construct the output string to the daily transaction file
		String s = "05 " + name;
		for (int i = s.length(); i < 24; i++){
			s += ' ';
		}
		
		s += "000000 00000";
		s += '\n';
		
		// add the line of output to the list of daily transactions
		transactions.add(s);
		
		System.out.println("Event deleted.");
		System.out.println("");
		
		return;
	}


	/**
	 * This method is called when the user is adding more tickets to an existing event.
	 */
	public static void add(){

		// initialize local scanner and variables
		String name = "";
		int added = 0;
		int index = -1;

		// loop executes until user has entered a valid event name
		while (true){
			System.out.print("Please enter the event name: ");

			// receive user input
			name = sc.nextLine();

			// get index of event stored in master list
			index = names.indexOf(name);

			if (!names.contains(name) || !forSale.get(index)){
				// the event does not exist
				System.out.println("Event does not exist.  Try again.");
			}
			else{
				break;
			}
		}

		// loop executes until user has entered number of tickets to add
		while(true){
			System.out.print("Enter the number of tickets to add: ");

			// attempt to receive valid user input
			try{
				// receive user input
				added = sc.nextInt();

				// get index of event stored in master list
				index = names.indexOf(name);

				if (tickets.get(index) + added > 99999 || added < 1){
					// if the user tries to add an illegal number of tickets to the event
					System.out.println("Invalid number of tickets.  Try again.");
				}
				else{
					break;
				}
			}

			// catch illegal user input
			catch(InputMismatchException e){
				System.out.println("Invalid number format.  Try again.");
				sc.next();
			}
		}

		tickets.set(index, tickets.get(index) + added);
		
		// construct the output string to the daily transaction file
		String s = "04 " + name;
		for (int i = s.length(); i < 24; i++){
			s += ' ';
		}
		s += "000000 ";
		
		String num = Integer.toString(added);
		for (int i = num.length(); i < 5; i++){
			num = '0' + num;
		}
		
		s += num;
		s += '\n';
		
		// add the line of output to the list of daily transactions
		transactions.add(s);
		
		System.out.println("Tickets successfully added.");
		System.out.println("");
		
		return;
	}


	/**
	 * This method is called when the user is buying tickets from the kiosk.
	 */
	public static void sell(){

		// initialize local scanner and variables
		String name = "";
		int buying = 0;
		int index = -1;

		// loop executes until user has entered a valid event name
		while (true){
			System.out.print("Please enter the event name: ");

			// receive user input
			name = sc.nextLine();
			if (!names.contains(name)){
				// the event does not exist
				System.out.println("Event does not exist.  Try again.");
			}
			else{
				break;
			}
		}

		// loop executes until user has entered number of tickets to purchase
		while(true){
			System.out.print("How many tickets would you like to purchase? ");

			// attempt to receive valid user input
			try{
				// receive user input
				buying = sc.nextInt();

				// get index of event stored in master list
				index = names.indexOf(name);

				if (tickets.get(index) - buying < 0 || (admin == false && buying > 8)){
					// if the user tries to buy an illegal number of tickets
					System.out.println("Invalid number of tickets.  Try again.");
				}
				else{
					break;
				}
			}

			// catch illegal user input
			catch(InputMismatchException e){
				System.out.println("Invalid number format.  Try again.");
				sc.next();
			}
		}
		
		// decrement the number of tickets available
		tickets.set(index, tickets.get(index) - buying);
		
		// construct the output string to the daily transaction file
		String s = "01 " + name;
		for (int i = s.length(); i < 24; i++){
			s += ' ';
		}
		s += "000000 ";
		
		String num = Integer.toString(buying);
		for (int i = num.length(); i < 5; i++){
			num = '0' + num;
		}
		
		s += num;
		s += '\n';
		
		// add the line of output to the list of daily transactions
		transactions.add(s);
		
		System.out.println("Tickets successfully bought.");
		System.out.println("");
		
		return;
	}


	/**
	 * This method is called when the user is returning previously purchased tickets.
	 */
	public static void returnTickets(){

		// initialize local scanner and variables
		String name = "";
		int returning = 0;
		int index = -1;

		// loop executes until user has entered a valid event name
		while (true){
			System.out.print("Please enter the event name: ");

			// receive user input
			name = sc.nextLine();
			if (!names.contains(name)){
				// the event does not exist
				System.out.println("Event does not exist.  Try again.");
			}
			else{
				break;
			}
		}

		// loop executes until user has entered number of tickets to return
		while(true){
			System.out.print("How many tickets would you like to return? ");

			// attempt to receive valid user input
			try{
				// receive user input
				returning = sc.nextInt();

				// get index of event stored in master list
				index = names.indexOf(name);

				if (tickets.get(index) + returning > 99999 || returning < 1 || (admin == false && returning > 8)){
					// the user tried to return an illegal number of tickets
					System.out.println("Invalid number of tickets.  Try again.");
				}
				else{
					break;
				}
			}

			// catch illegal user input
			catch(InputMismatchException e){
				System.out.println("Invalid number format.  Try again.");
				sc.next();
			}
		}
		
		// update the number of tickets available for the event
		tickets.set(index, tickets.get(index) + returning);
		
		// construct the output string to the daily transaction file
		String s = "03 " + name;
		
		for (int i = s.length(); i < 24; i++){
			s += ' ';
		}
		s += "000000 ";
		
		String num = Integer.toString(returning);
		for (int i = num.length(); i < 5; i++){
			num = '0' + num;
		}
		
		s += num;
		s += '\n';
		
		// add the line of output to the list of daily transactions
		transactions.add(s);
		
		System.out.println("Tickets successfully returned.");
		System.out.println("");
		
		return;
	}


	/**
	 * This method is called when the user wishes to log out of the current kiosk session.
	 */
	public static void logout(){
		
		// writes output to a file
		PrintWriter transactionsFile = null;
		
		// try to create the PrintWriter
		try {
			transactionsFile = new PrintWriter("dailyTransactions.txt");
		}
		
		// catches any errors writing to the transaction file
		catch (FileNotFoundException e) {
			System.out.println("Fatal error: could not write to daily transaction file.");
			return;
		}
		
		// Loop through the list of daily transactions to output
		for (int i = 0; i < transactions.size(); i++){
			transactionsFile.write(transactions.get(i));
		}
		
		transactionsFile.write("00");
		
		// close the file writer
		transactionsFile.close();
		
		System.out.println("Logged out.");
		
		return;
	}
}