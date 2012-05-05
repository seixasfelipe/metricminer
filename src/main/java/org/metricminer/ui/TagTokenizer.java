package org.metricminer.ui;

import java.util.ArrayList;
import java.util.List;


import org.apache.commons.lang3.StringUtils;
import org.metricminer.model.Tag;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class TagTokenizer {

	public String tags(List<Tag> tags) {
		List<String> tagNames = new ArrayList<String>();
		
		for(Tag t : tags) tagNames.add(t.getName());
		
		return StringUtils.join(tagNames, ",");
		
	}
}
