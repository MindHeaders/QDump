package org.dataart.qdump.service.rest;

import org.dataart.qdump.service.resourceImpl.AnswerEntityResourceBean;
import org.dataart.qdump.service.resourceImpl.PersonAnswerEntityResourceBean;
import org.dataart.qdump.service.resourceImpl.PersonEntityResourceBean;
import org.dataart.qdump.service.resourceImpl.PersonQuestionEntityResourceBean;
import org.dataart.qdump.service.resourceImpl.PersonQuestionnaireEntityResourceBean;
import org.dataart.qdump.service.resourceImpl.QuestionEntityResourceBean;
import org.dataart.qdump.service.resourceImpl.QuestionnaireEntityResourceBean;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/rest")
public class MyApplication extends Application{
	
	HashSet<Object> singletons = new HashSet<>();
	
	public MyApplication() {
		singletons.add(new AnswerEntityResourceBean());
		singletons.add(new PersonAnswerEntityResourceBean());
		singletons.add(new PersonEntityResourceBean());
		singletons.add(new PersonQuestionEntityResourceBean());
		singletons.add(new PersonQuestionnaireEntityResourceBean());
		singletons.add(new QuestionnaireEntityResourceBean());
		singletons.add(new QuestionEntityResourceBean());
	}
	@Override
	public Set<Class<?>> getClasses() {
        return new HashSet<>();
	}
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
	
	
}
