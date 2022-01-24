package com.bfxy.esjob.annotation;

import org.apache.commons.lang3.time.FastDateFormat;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;

@Aspect
public class JobTraceInterceptor implements Ordered {	// TraceComponent, 

    private static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");

    public JobTraceInterceptor() {
    }
    
    //TODO bhz
	@Around("@annotation(jobTrace)")
	public Object proceed(ProceedingJoinPoint proceedingJoinPoint, JobTrace jobTrace) throws Throwable {
		try {
			long beginTime = System.currentTimeMillis();
//			System.err.println("---------> beginTime " + beginTime);
			Object result = proceedingJoinPoint.proceed();
	        long runTime = (System.currentTimeMillis() - beginTime);
//			System.err.println("---------> runTime " + runTime);
			
			return result;			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			
		}

	}
	
	@Override
	public int getOrder() {
		return 1;
	}

}
