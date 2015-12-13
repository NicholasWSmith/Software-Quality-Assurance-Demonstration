import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * BackEnd.java is responsible for reading in event information  (in the form of .txt files) from the previous day's front-end terminals.
 * It then modifies and updates this information as necessary, and outputs two new .txt files for use by the front-end terminals the following day.
 * 
 * Christopher Thomas, 10066835
 * Nicholas Smith, 10098522
 */
public class BackEnd {

	// related ArrayLists containing data about all events currently on file
	static ArrayList<String> names;
	static ArrayList<String> dates;
	static ArrayList<Integer> tickets;


	/**
	 * The main method calls each of the three helper methods sequentially: masterEvents(), mergedEvents, and outputEvents().
	 * @param args: unused
	 */
	public static void main (String[] args) {

		// call all helper methods
		names = new ArrayList<String>();
		dates = new ArrayList<String>();
		tickets = new ArrayList<Integer>();
		masterEvents();
		mergedEvents();
		outputEvents();
	} // end main



	/*
	 * MasterEvents: Parameters - None, Returns - None
	 * 
	 * masterEvents reads in masterEvents.txt to record information on all of the events currently in Quibble's system.
	 * 
	 * This event information is used in the mergedEvents() method. 
	 */
	public static void masterEvents(){

		// try to read data from master events file
		try {
			// Scanner to read in event information from master events file
			Scanner s = new Scanner(new FileReader("masterEvents.txt"));

			String line = "";
			line = s.nextLine();

			String name = "";
			String date = "";
			int numTickets = -1;

			// loop through each line in the input file
			while(!line.equals("")) {

				// break up the line into the event's date, name, and the number of tickets
				date = line.substring(0,6);
				numTickets = Integer.parseInt(line.substring(7,12));
				name = line.substring(13);

				// format the event's date into a Date type
				SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
				Date formatted = formatter.parse(date);

				// get today's date
				Calendar cal = Calendar.getInstance();
				Date today = cal.getTime();

				if (!today.after(formatted)){
					// the event's date has not yet passed
					// add current event info to the lists of event information
					names.add(name.trim());
					dates.add(date);
					tickets.add(numTickets);
				}

				// read next line of text input
				if(s.hasNextLine())
					line = s.nextLine();
				else
					line = "";
			}

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

		// if there is an error parsing an event's date
		catch (ParseException e) {
			System.out.println("Fatal error: event date could not be parsed.");
			e.printStackTrace();
			System.exit(-1);
			return;
		}

		return;
	} //end masterEvents



	/*
	 * MergedEvents: Paramters - None, Returns - None
	 * MergedEvents reads in mergedDailyTransactions.txt and then applies all the changes to the global variables.
	 * For example, selling 3 tickets or adding a new event are all down through here. 
	 */
	public static void mergedEvents(){
		// try to read data from merged daily transactions file
		try {
			// Scanner to read in event info from merged daily transactions file
			Scanner s = new Scanner(new FileReader("dailyTransactions.txt"));

			String line = "";
			line = s.nextLine();

			int transCode = 0;
			String date = "";
			String name = "";
			int numTickets = -1;
			int index = -1;

			// loop through each line in the input file
			while(!line.equals("00")) {

				//read in the transaction code
				transCode = Integer.parseInt(line.substring(0, 2));

				//read in the name of the event
				name = line.substring(3,23);
				name = name.trim();


				//read in the date of the event
				date = line.substring(24,30);

				//read in the number of the tickets in the transaction
				numTickets = Integer.parseInt(line.substring(31, 36));

				//sell tickets transaction
				if (transCode == 1){
					index = names.indexOf(name);
					if(index != -1){
						if(tickets.get(index) - numTickets >= 0)
							tickets.set(index, tickets.get(index) - numTickets);
						else
							System.out.println("Error: event " + name + " cannot oversell its tickets");
					}
					else{
						System.out.println("Error: event " + name + " does not exist.");
					}
				}

				//return or add tickets transaction
				else if (transCode == 2 || transCode == 4){
					index = names.indexOf(name);
					if(index != -1){
						if(tickets.get(index) + numTickets < 100000)
							tickets.set(index, tickets.get(index) + numTickets);
						else
							System.out.println("Error: event " + name + " cannot have more than 100,000 tickets available.");
					}
					else{
						System.out.println("Error: event " + name + " does not exist.");
					}
				}

				// create event
				else if (transCode == 03){
					if(!names.contains(name)){
						names.add(name);
						dates.add(date);
						tickets.add(numTickets);
					}
					else{
						System.out.println("Error: event with the name " + name + " already exists.");
					}
				}

				// delete event
				else if (transCode == 05){
					if (names.contains(name)){
						names.remove(name);
						dates.remove(date);
						tickets.remove(numTickets);
					}
					else{
						System.out.println("Error: cannot delete event " + name + ", as it does not exist.");
					}
				} 

				//get next line of the text file
				line = s.nextLine();
			}
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

		return;
	} //end mergedEvents()



	/*
	 * CurrentEvents: Parameters - None, Returns - None.
	 * 
	 * the CurrentEvents method prints out the information from the global arrays to the text files.
	 * 
	 * Specifically:
	 * 		the names and number of tickets to the currentEvents.txt file
	 * 		the names, dates, and number of tickets to the masterEvents.txt file
	 */
	public static void outputEvents(){

		//writes the new/updated events to currentEvents.txt and masterEvents.txt
		try {
			// currentEvents.txt
			PrintWriter current = new PrintWriter("currentEvents.txt");

			// loop through all events
			for(int k = 0; k < names.size(); k++){

				// format the event's name to be written to the file
				String printName = names.get(k);
				while(printName.length() < 20)
					printName += ' ';

				// format the number of tickets to be written to the file
				String printTickets = tickets.get(k).toString();
				while(printTickets.length() < 5)
					printTickets = '0' + printTickets;

				current.println(printName + " " + printTickets);
			}
			
			// write the EOF flag to the file
			current.println("END                  00000");

			// sort events in ascending order by date before writing to master events file
			SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
			ArrayList<Date> tempDates = new ArrayList<Date>();
			
			// ArrayLists to store the sorted event information
			ArrayList<String> sortedNames = new ArrayList<String>();
			ArrayList<String> sortedDates = new ArrayList<String>();
			ArrayList<Integer> sortedTickets = new ArrayList<Integer>();

			// convert each date string into a Java Date object
			for (int i = 0; i < dates.size(); i++){
				tempDates.add(formatter.parse(dates.get(i)));
			}
			
			// stores the index of the current earliest event
			int min = 0;
			
			// while there are still potentially unsorted events to consider
			while(tempDates.size() > 1){
				min = 0;
				
				// find the earliest event currently on file
				for (int x = 0; x < tempDates.size(); x++){
					if(tempDates.get(min).after(tempDates.get(x)))
						min = x;
				}
				
				// add the earliest event found to the sorted ArrayLists
				sortedNames.add(names.get(min));
				sortedDates.add(dates.get(min));
				sortedTickets.add(tickets.get(min));
				
				// remove the earliest event from the old, unsorted ArrayLists
				tempDates.remove(min);
				names.remove(min);
				dates.remove(min);
				tickets.remove(min);
			}
			
			// add the latest event to the sorted ArrayLists
			sortedNames.add(names.get(0));
			sortedDates.add(dates.get(0));
			sortedTickets.add(tickets.get(0));
			
			// masterEvents.txt
			PrintWriter master = new PrintWriter("masterEvents.txt");

			// loop through all events
			for(int k = 0; k < sortedNames.size(); k++){

				// format the event's name to be written to the file
				String printName = sortedNames.get(k);
				while(printName.length() < 20)
					printName += ' ';

				// format the number of tickets to be written to the file
				String printTickets = sortedTickets.get(k).toString();
				while(printTickets.length() < 5)
					printTickets = '0' + printTickets;

				master.println(sortedDates.get(k) + " " + printTickets + " " + printName);
			}
			
			current.close();
			master.close();
		}

		// if the output files could not be written to
		catch (FileNotFoundException e) {
			System.out.println("Fatal error: could not write to output file.");
			e.printStackTrace();
			System.exit(-1);
			return;
		}

		// if there is an error handling an event's date
		catch (ParseException e) {
			System.out.println("Fatal error: event date could not be parsed.");
			e.printStackTrace();
			System.exit(-1);
			return;
		}

		return;
	} //end outputEvents
} //end BackEnd class