package br.com.caelum.revolution.changesets;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.scm.SCM;

public class ChangeSetsInPeriodFactory implements SpecificChangeSetFactory {

	private final SimpleDateFormat sdf;

	public ChangeSetsInPeriodFactory() {
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public ChangeSetCollection build(SCM scm, Config config) {
		try {
			Calendar startPeriod = Calendar.getInstance();
			startPeriod.setTime(sdf.parse(config.asString("changesets.all.startPeriod")));
			Calendar endPeriod = Calendar.getInstance();
			endPeriod.setTime(sdf.parse(config.asString("changesets.all.endPeriod")));
			
			return new ChangeSetsInPeriod(scm, startPeriod, endPeriod);
		}
		catch(Exception e) {
			throw new ChangeSetNotFoundException(e);
		}
	}

}
