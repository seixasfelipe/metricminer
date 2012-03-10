package controller;

import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class ProjectController {

	private final Result result;
	
	public ProjectController(Result result) {
		this.result = result;
	}

}
