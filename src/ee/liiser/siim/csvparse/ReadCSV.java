package ee.liiser.siim.csvparse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.List;

public class ReadCSV {

	List<Activity> activities = new ArrayList<Activity>();
	DateTimeFormatter format = DateTimeFormatter
			.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
	private static final String csvFile = "financial_log.csv";
	private static final String outputFile = "output.txt";
	private static final String cvsSplitBy = ",";
	
	static PrintStream out;

	public static void main(String[] args) {

		try {
			out = new PrintStream(new File(outputFile));
			
			// Measuring time taken
			Instant startTime = Instant.now();
			ReadCSV obj = new ReadCSV();
			obj.run();
			/*out.println("Run time: "
					+ toNiceTime(Duration.between(startTime, Instant.now())));*/
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally{
			if(out != null){
				out.close();
			}
		}

	}

	public void run() {

		
		BufferedReader br = null;
		String line = "";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use previously declared separator (usually comma)
				String[] elements = line.split(cvsSplitBy);

				// Parse the time from the line
				LocalDateTime time = LocalDateTime.parse(elements[3], format);
				
				//adding to csv code start
				
				/*out.print(line);
				out.print(cvsSplitBy);
				int dayOfWeek = time.get(ChronoField.DAY_OF_WEEK);
				out.print(!(dayOfWeek == 6 || dayOfWeek == 7));
				out.print(cvsSplitBy);
				int timeOfDay = time.get(ChronoField.HOUR_OF_DAY);
				out.print(timeOfDay > 8 && timeOfDay < 18);
				out.println();*/
				
				//adding to csv code end

				// Look through all already existing activities
				boolean matchedIDFound = false;
				for (Activity a : activities) {
					if (a.getID().equals(elements[0])) {
						// If the ID already exists, then just update the
						// timestamp on it
						a.updateTime(time);
						matchedIDFound = true;
						break;
					}
				}
				if (!matchedIDFound) {
					// If the ID didn't match any, then create a new activity
					activities.add(new Activity(elements[0], time));
				}

				// Move on to next line
			}

			// All lines processed

			for (Activity a : activities) {
				// Here I just print out all the differences, you probably want
				// to do something else with them
				out.println(/*"For ID: " + */a.getID()
								+ ","/*", the process time is "*/
								+ /*toNiceTime(*/a.difference().getSeconds()/3600)/*)*/;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					// Making sure the file gets closed
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		//out.println("Done");
	}

	// Just a method to make the output of duration nicer to read.
	// Not really necessary.
	// Feel free to modify this to make the output as you like it.
/*	private static String toNiceTime(Duration time) {
		return time.toString().replace("PT", "").replace("H", "h ")
				.replace("M", "m ").replace("S", "s");
	}*/
	
	

}