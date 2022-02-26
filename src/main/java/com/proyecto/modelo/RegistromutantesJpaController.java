/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.modelo;

import com.proyecto.modelo.exceptions.NonexistentEntityException;
import com.proyecto.modelo.exceptions.RollbackFailureException;
import com.proyecto.persistencia.Registromutantes;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.json.*;
/**
 *
 * @author Camilo
 */
public class RegistromutantesJpaController implements Serializable {
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;
    
    public RegistromutantesJpaController(UserTransaction utx,EntityManagerFactory emf) {
        this.utx= utx;
        this.emf = emf; 
    }
    

    public EntityManager getEntityManager() {
        return this.emf.createEntityManager();
    }

    public void create(Registromutantes registromutantes) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(registromutantes);
            utx.commit();
            
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void save (Registromutantes registromutantes,EntityManagerFactory emf ){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx= em.getTransaction();
        try{
            tx.begin();
            em.persist(registromutantes);
            tx.commit();
        } catch (RuntimeException ex){
            if (tx!=null && tx.isActive()) tx.rollback();
        } finally {
            em.close();
        }
    }
    
    public JSONObject getMutantStat (EntityManagerFactory emf ){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx= em.getTransaction();
       // String cadena="";
        
            Query q = em.createNativeQuery("SELECT esmutante,count(1) FROM mutantesdb.registromutantes group by esmutante");
            List<Object[]> a = q.getResultList();
            long mutant=0;
            long human=0;
            int cont=0;
            for (Object[] r : a) {
             
                if ((int)r[0]==0){
                    human=(long)r[1];
                }
                else{
                  mutant=(long)r[1];
                }
           }
            double result =Double.parseDouble(""+mutant)/Double.parseDouble(""+human);
            DecimalFormat df = new DecimalFormat("###.##");
            String cadenaJson = "{\"nombre\":\"Maggie\",\"edad\":3}";
           // Crear un nuevo objeto JSON
           //JSONObject objetoJson = new JSONObject(cadenaJson);
             JSONObject cadena = new JSONObject();

        // Cadenas de texto básicas
             cadena.put("count_mutant_dna", mutant);
             cadena.put("count_human_dna", human);
             cadena.put("ratio", df.format(result));
            System.out.println(""+cadena.toString());
           //cadena="count_mutant_dna:"+mutant+" count_human_dna:"+human+" ratio:"+df.format(result);
        return cadena;
    }
    
    public void edit(Registromutantes registromutantes) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            registromutantes = em.merge(registromutantes);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = registromutantes.getId();
                if (findRegistromutantes(id) == null) {
                    throw new NonexistentEntityException("The registromutantes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Registromutantes registromutantes;
            try {
                registromutantes = em.getReference(Registromutantes.class, id);
                registromutantes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The registromutantes with id " + id + " no longer exists.", enfe);
            }
            em.remove(registromutantes);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Registromutantes> findRegistromutantesEntities() {
        return findRegistromutantesEntities(true, -1, -1);
    }

    public List<Registromutantes> findRegistromutantesEntities(int maxResults, int firstResult) {
        return findRegistromutantesEntities(false, maxResults, firstResult);
    }

    private List<Registromutantes> findRegistromutantesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Registromutantes.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Registromutantes findRegistromutantes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Registromutantes.class, id);
        } finally {
            em.close();
        }
    }

    public int getRegistromutantesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Registromutantes> rt = cq.from(Registromutantes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
