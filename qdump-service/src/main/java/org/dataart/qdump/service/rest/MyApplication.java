package org.dataart.qdump.service.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.dataart.qdump.service.resources.PersonEntityResource;
import org.dataart.qdump.service.resources.TestResource;

@ApplicationPath("/rest")
public class MyApplication extends Application{
	
	HashSet<Object> singletons = new HashSet<Object>();
	
	public MyApplication() {
		singletons.add(new TestResource());
		singletons.add(new PersonEntityResource());
	}
	@Override
	public Set<Class<?>> getClasses() {
		HashSet<Class<?>> set = new HashSet<Class<?>>();
		return set;
	}
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
	
	
}
