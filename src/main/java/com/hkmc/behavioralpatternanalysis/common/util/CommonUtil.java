package com.hkmc.behavioralpatternanalysis.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import lombok.Getter;
import lombok.Setter;


public class CommonUtil {

	@Getter @Setter
	private static long totalCount = 0;
	
	@Getter @Setter
	private static int currentPage = 0;
	
	public static String getFormattedDate(int i) {
	    Calendar cal = new GregorianCalendar();
	    cal.add(Calendar.DATE, i);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    return sdf.format(cal.getTime());
	}
}
