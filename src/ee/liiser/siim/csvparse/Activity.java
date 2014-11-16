package ee.liiser.siim.csvparse;

import java.time.Duration;
import java.time.LocalDateTime;

public class Activity {
	private String ID;
	private LocalDateTime earliest;
	private LocalDateTime latest;
	
	public Activity(String ID, LocalDateTime time){
		this.ID = ID;
		earliest = time;
		latest = time;
	}
	
	public void updateTime(LocalDateTime time){
		if(time.isAfter(latest)){
			latest = time;
		}else if(time.isBefore(earliest)){
			earliest = time;
		}
	}
	
	public Duration difference(){
		return Duration.between(earliest, latest);
	}
	
	public String getID(){
		return ID;
	}

}
