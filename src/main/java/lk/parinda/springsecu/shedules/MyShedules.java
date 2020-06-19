package lk.parinda.springsecu.shedules;

import java.util.Calendar;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyShedules {
	
	@Scheduled(fixedRate = 2000)
	public void pringSomething() {
		//System.out.println(Calendar.DATE);
	}

}
