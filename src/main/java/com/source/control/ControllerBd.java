package com.source.control;

import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.hibernate.Query;

import com.source.Aplicacao;
import com.source.model.Acesso;
import com.source.model.AgrotoxicoProibi;
import com.sun.el.stream.Optional;

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

	@SuppressWarnings("unchecked")
	public static Stream<Acesso> getAcessoAsStream() {
		return em.createQuery("SELECT a FROM ACESSO a").getResultStream();
	}

	public static void begin() throws PersistenceException {
		checkTrans();
		trans.begin();
	}

	public static void checkTrans() throws PersistenceException {
		if (trans.isActive()) {
			trans.rollback();
		}
	}



	public static AgrotoxicoProibi findAgroProib(String agro) {
		try {
			TypedQuery<AgrotoxicoProibi> q = em
					.createQuery("SELECT a FROM AGROTOXICO_PROIBI a WHERE a.agrotoxico = ?1", AgrotoxicoProibi.class)
					.setParameter(1, agro);
			return q.getSingleResult();
		} catch (Exception e) {
		}
		return null;

	}
}
