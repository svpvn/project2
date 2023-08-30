	package project.demo.jobscheduler;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import project.demo.entity.User;
import project.demo.repository.UserRepo;
import project.demo.service.EmailService;

@Component
@Slf4j
public class JobSchedule {
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	EmailService emailService;
	
	//@Scheduled(fixedDelay = 60000)
	public void hello() {
//		log.info("Hello ");
//		emailService.testEmail();
	}
	
	// GIAY - PHUT - GIO - NGAY - THANG - THU
	//@Scheduled(cron = "0 0 0 * * *")
	public void morning() {
		Calendar cal = Calendar.getInstance();
		int date = cal.get(Calendar.DATE);
		
		//thang 1 tuong duong 0
		int month = cal.get(Calendar.MONTH) + 1;
		
		List<User> users = userRepo.searchByBirthday(date, month);
		
		for(User u : users) {
			log.info("Happy Birthday " + u.getName());
			emailService.sendBrithdayEmail(u.getEmail(), u.getName());
		}
		log.info("Good Moring");
	}
	
}
