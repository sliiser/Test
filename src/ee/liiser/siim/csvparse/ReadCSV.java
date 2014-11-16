package ee.liiser.siim.csvparse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReadCSV {

	List<Activity> activities = new ArrayList<Activity>();
	DateTimeFormatter format = DateTimeFormatter
			.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
	String csvFile = "financial_log.csv";
	String cvsSplitBy = ",";

	public static void main(String[] args) {

		// Measuring time taken
		Instant startTime = Instant.now();
		ReadCSV obj = new ReadCSV();
		obj.run();
		System.out.println("Run time: "
				+ toNiceTime(Duration.between(startTime, Instant.now())));

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
				System.out
						.println("For ID: " + a.getID()
								+ ", the process time is "
								+ toNiceTime(a.difference()));
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

		System.out.println("Done");
	}

	// Just a method to make the output of duration nicer to read.
	// Not really necessary.
	// Feel free to modify this to make the output as you like it.
	private static String toNiceTime(Duration time) {
		return time.toString().replace("PT", "").replace("H", "h ")
				.replace("M", "m ").replace("S", "s");
	}

}