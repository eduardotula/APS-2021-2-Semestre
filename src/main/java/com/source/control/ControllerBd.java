package com.source.control;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import com.source.Aplicacao;


/**
 * Classe para operações CRUD generico, esta classe não é Thread safe e possui somente um EntityManager*/

public class ControllerBd {

	public static EntityManager em = Aplicacao.em;
	private static EntityTransaction trans = em.getTransaction();


	public static void create(Object ob) throws Exception {
		begin();
		em.persist(ob);
		trans.commit();
	}

	
	public static Object findById(Class<?> classe,Integer id) throws IllegalArgumentException{
		return em.find(classe, id);
	}
	
	public static Object findByIdDeatch(Class<?> classe, Integer id) throws IllegalArgumentException{
		Object o = em.find(classe, id);
		em.detach(o);
		return o;
	}
	
	public static void commit() throws Exception {
		if(trans.isActive()) {trans.commit();
		}else { throw new Exception();}
		
	}
	
	public static void begin() throws PersistenceException{
		checkTrans();
		trans.begin();
	}

	private static void checkTrans() throws PersistenceException {
		if (trans.isActive()) {
			trans.rollback();
		}
	}

}
