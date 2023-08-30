package project.demo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogAOP {
	// chan truoc khi thuc thi ham
	@Before("execution(* project.demo.service.DepartmentService.getById(*))")
	public void getByDepartmentId(JoinPoint joinPoint) {
		// getArgs tra ve mang cac tham so id =0
		int id = (Integer) joinPoint.getArgs()[0];
		log.info("JOIN POINT: " + id);
	}
}
