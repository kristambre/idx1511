package util;

import java.util.Date;

public class Validator {

	public static boolean validateDate(Date d) {
		if (d.before(new Date())) {
			return false;
		}
		return true;
	}
}
