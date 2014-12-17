package org.dataart.qdump.persistence.config;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

import org.dataart.qdump.entities.questionnaire.BaseEntity;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

public class GlobalInterceptor extends EmptyInterceptor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4605249677380841090L;

	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		if(entity instanceof BaseEntity) {
			for(int i = 0; i < propertyNames.length; i++) {
				if(propertyNames[i].equals("modifiedDate")) {
					currentState[i] = new Date();
					return true;
				}
			}
		}
		return super.onFlushDirty(entity, id, currentState, previousState,
				propertyNames, types);
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		if(entity instanceof BaseEntity) {
			for(int i = 0; i < propertyNames.length; i++) {
				if(propertyNames[i].equals("createdDate")) {
					state[i] = new Date();
					return true;
				}
			}
		}
		return super.onSave(entity, id, state, propertyNames, types);
	}

	@Override
	public void postFlush(Iterator entities) {
		super.postFlush(entities);
	}

	@Override
	public void preFlush(Iterator entities) {
		super.preFlush(entities);
	}

	@Override
	public void afterTransactionBegin(Transaction tx) {
		super.afterTransactionBegin(tx);
	}

	@Override
	public void afterTransactionCompletion(Transaction tx) {
		super.afterTransactionCompletion(tx);
	}

	@Override
	public void beforeTransactionCompletion(Transaction tx) {
		super.beforeTransactionCompletion(tx);
	}
}
