package com.source.control;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import com.source.Aplicacao;

/**
 * Classe para operações CRUD generico, esta classe não é Thread safe e possui
 * somente um EntityManager
 */

public class ControllerBd {

	public static EntityManager em = Aplicacao.em;
	private static EntityTransaction trans = em.getTransaction();

	public static void create(Object ob) {
		try {
			checkTrans();
			begin();
			em.persist(ob);
			trans.commit();
		} catch (PersistenceException e) {
			checkTrans();
			e.printStackTrace();
		}
	}

	public static void delete(Object obj) {
		try {
			checkTrans();
			begin();
			em.remove(obj);
			trans.commit();
		} catch (PersistenceException e) {
			checkTrans();
			e.printStackTrace();
		}
	}

	/**
	 * Check if the instance is a managed entity instance belonging to the current
	 * persistence context.
	 * 
	 * @param entity entity instance
	 * @return boolean indicating if entity is in persistence context
	 * @throws IllegalArgumentException if not an entity
	 */
	public static boolean checkPersist(Object obj) {
		try {
			return em.contains(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Object findById(Class<?> classe, Integer id) throws IllegalArgumentException {
		return em.find(classe, id);
	}

	public static Object findByIdDeatch(Class<?> classe, Integer id) throws IllegalArgumentException {
		Object o = em.find(classe, id);
		em.detach(o);
		return o;
	}

	public static void commit() throws Exception {
		if (trans.isActive()) {
			trans.commit();
		} else {
			throw new Exception();
		}

	}

	public static void begin() throws PersistenceException {
		checkTrans();
		trans.begin();
	}

	private static void checkTrans() throws PersistenceException {
		if (trans.isActive()) {
			trans.rollback();
		}
	}

}
