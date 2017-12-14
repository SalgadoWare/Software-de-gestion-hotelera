package codigo.com.toedter.micalendar;

import java.util.Date;

public class Dia {
	public Integer firstHour, lastHour, first2hour, last2hour;
	public Date date;
	
	public Dia(Integer fh, Integer lh,Integer f2h,Integer l2h, Date d) {
		lastHour = lh;
		firstHour = fh;
		last2hour =l2h;
		first2hour=f2h; 
		date = d;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ("  fh " + firstHour+ "   lh " + lastHour+ "    " + "  f2h  " + first2hour+ "   l2h " + last2hour+ "   " + date.toString()  );
	}
}
