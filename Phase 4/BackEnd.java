import java.awt.List;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.*;


/**
 * BackEnd.java is responsible for taking event information  (in the form of .txt files) from the previous day's front-end terminals.
 * It then modifies and updates this information as necessary, and outputs two new .txt files for use by the front-end terminals the following day.
 * Christopher Thomas, 10066835
 * Nicholas Smith, 10098522
 */
public class BackEnd {

	// related arrays containing data about all events currently on file
	static String[] names = new String[1000];
	static String[] dates = new String[1000];
	static int[] tickets = new int[1000];
	
	
	/**
	static ArrayList<String> names;
	static ArrayList<String> dates;
	static ArrayList<Integer> tickets;
	static ArrayList<Boolean> forSale;
	
	 * The main method loops through an infinite number of Quibble kiosk sessions.
	 * @param args: unused
	 */
	public static void main (String[] args) {

			
			/*
			names = new ArrayList<String>();
			dates = new ArrayList<String>();
			tickets = new ArrayList<Integer>();
			forSale = new ArrayList<Boolean>();
		*/
		
		
		masterEvents();
		mergedEvents();
		currentEvents();
	}
	
	
	/*
	 * MasterEvents: Parameters - None, Returns - None
	 * 
	 * masterEvents reads in the masterEventsFile.txt to see all of the events that are happening. 
	 * 
	 * This is used for the changes that the mergedEvents() method handles. 
	 */
	public static void masterEvents(){
	
		// try to read data from current events file
				try {
					// Scanner to read in event info from current events file
					Scanner s = new Scanner(new FileReader("masterEventsFile.txt"));
					
					String line = "";
					String name = "";
					String ticketsS = "";
					String date = "";
					int numTickets = -1;
					int i = 0;
					line = s.nextLine();
					
					// loop through each line in the input file
					while(!line.equals(null)) {
						
						// break up the line into the event's name and the number of tickets
						date = line.substring(0,6);
						ticketsS = line.substring(7,12);
						name = line.substring(13);
						
						numTickets = Integer.parseInt(ticketsS);
						
						// add current event info to the lists of event information
						
						/*
						names.add(name.trim());
						dates.add(date);
						tickets.add(numTickets);
						forSale.add(true);
						*/
						
						//adds the name to the array
						names[i] = name;
						//adds the date to the array
						dates[i] = date;
						//adds the number of tickets to the array
						tickets[i] = numTickets;
						i++;
						// receive next line of text input
						line = s.nextLine();
					}
					
				}// if the current events file does not exist
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
	}//end masterEvents
	
	
	
	/*
	 * MergedEvents: Paramters - None, Returns - None
	 * MergedEvents reads in the mergedDailyTransactions.txt and then applies all the changes to the global variables.
	 * For example, selling 3 tickets or adding a new event are all down through here. 
	 */
	public static void mergedEvents(){
		// try to read data from current events file
		try {
			// Scanner to read in event info from current events file
			Scanner s = new Scanner(new FileReader("mergedDailyTransactions.txt"));
			
			String line = "";
			line = s.nextLine();
			
			int transCode = 0;
			String transCodeS = "";
			String date = "";
			String name = "";
			String ticketsS = "";
			int numTickets = -1;
			
			// loop through each line in the input file
			while(!line.equals("00")) {
				
				//getting the transaction code
				transCodeS = line.substring(0,1);
				transCode = Integer.parseInt(transCodeS);
				
				//getting the name of the transaction
				name = line.substring(3,23);
				name = name.trim();
				
				
				//getting the date of the event
				date = line.substring(24,30);
				
				//getting the number of the tickets in the transaction
				ticketsS = line.substring(31,36);
				numTickets = Integer.parseInt(ticketsS);
				
				//sell
				if (transCode == 01){
					for (int i = 0; i < names.length; i++){
						if (names[i].equals(name)){
							tickets[i] = tickets[i] - numTickets;
						}
						
					}
					
					
					
				//return
				} else if (transCode == 02){
					for (int i = 0; i < names.length; i++){
						if (names[i].equals(name)){
							tickets[i] = tickets[i] + numTickets;
						}
						
					}
					
				//create
				} else if (transCode == 03){
					int i = 0;
					while (!names[i].equals(null)){
						i++;
					}
					
					//now i+1 is equal to the the first null spot of the array. We can add the new element.
					names[i+1] = name;
					dates[i+1] = date;
					tickets[i+1] = numTickets;
					
					
				//add
				} else if (transCode == 04){
					for (int i = 0; i < names.length; i++){
						if (names[i].equals(name)){
							tickets[i] = tickets[i] + numTickets;
						}
						
					}
					
					
					
				//delete
				} else if (transCode == 05){
					for (int i = 0; i < names.length; i++){
						if (names[i].equals(name)){
							ArrayList<String> list = new ArrayList<String>(Arrays.asList(names));
							list.remove(name);
							names = list.toArray(names);
							
							ArrayList<String> list1 = new ArrayList<String>(Arrays.asList(dates));
							list1.remove(date);
							dates = list1.toArray(dates);
							
							ArrayList<Integer> list2 = new ArrayList<Integer>();
							for (int x = 0; x < tickets.length; x++){
								list2.add(tickets[x]);
							}
							list2.remove(i);
							tickets = list2.toArray(tickets);
						}
					}
				} 
				
				
				
				//get next line of the text file
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
		
		
		
		
		
		
		
		
		
		
	}//end mergedEvents()
	
	/**
	 * CurrentEvents: Parameters - None, Returns - None.
	 * 
	 * the CurrentEvents method prints out the information from the global arrays to the text files.
	 * 
	 * Specifically, the names and number of tickets to the currentEvents.txt
	 * and the date, the number of tickets, and the name to the masterEvents.txt
	 */
	public static void currentEvents(){
		
		
		//writes the new events to the currentEvents.txt
		String fileName = "currentEvents.txt";
		String fileName2 = "masterEvents.txt";
		try {
			PrintWriter outputStream = new PrintWriter(fileName);
			for(int k = 0; k < names.length; k++){
				outputStream.println(names[k] + "" + tickets[k]);
				outputStream.println("\n");
				outputStream.println("\n");
			}
			outputStream.println("END 00000");
			
			PrintWriter outputStream2 = new PrintWriter(fileName2);
			for(int k = 0; k < names.length; k++){
				outputStream2.println(dates[k] + "" + tickets[k] + "" + names[k]);
				outputStream2.println("\n");
				outputStream2.println("\n");
			}
			
			outputStream.close();
			outputStream2.close();
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}//end currentEvents()
}//end class
