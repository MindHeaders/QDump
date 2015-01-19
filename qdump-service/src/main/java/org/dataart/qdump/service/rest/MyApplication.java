package org.dataart.qdump.service.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.dataart.qdump.service.resourceImpl.*;

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
		HashSet<Class<?>> set = new HashSet<>();
		return set;
	}
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
	
	
}
