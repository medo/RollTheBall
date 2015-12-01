package eg.edu.guc.rolltheball.logic.grammer;

import java.util.ArrayList;
import java.util.HashSet;

public class Clause extends ArrayList<Literal> {

	@Override
	public boolean equals(Object o) {
		Clause c = (Clause) o;
		for (Literal l : c) {
			boolean found = false;
			for (Literal l1 : this) {
				if (l1.equals(l)) found = true;
			}
			if (!found) return false;
		}
		return true;
	}
	
}
