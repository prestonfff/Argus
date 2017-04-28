/*
 * Copyright (c) 2016, Salesforce.com, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of Salesforce.com nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.salesforce.dva.argus.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.eclipse.persistence.annotations.CascadeOnDelete;

import java.math.BigInteger;
import java.util.Objects;

import static com.salesforce.dva.argus.system.SystemAssert.requireArgument;

/**
 * It encapsulates the user preference information
 *
 * @author pfu (pfu@salesforce.com)
 */
@SuppressWarnings("serial")
@Entity
@NamedQueries(
		{
			@NamedQuery(name = "Preference.findByUserAndEntity", query = "SELECT p FROM Preference p WHERE p.user = :user AND p.entity = :jpaEntity")	
		}
)
@Table(indexes = { @Index(columnList = "entity_id") })
public class Preference implements Serializable, Identifiable {
	
	//~ Instance fields ******************************************************************************************************************************
	
	@Basic(optional = false)
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private BigInteger id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private PrincipalUser user;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "entity_id", nullable = true)
	private JPAEntity entity;
	
	@Lob
	private String options;
	
	  //~ Constructors *********************************************************************************************************************************
	/**
	 * Creates a new Preference object
	 * 
	 * @param user			The user who sets up this preference. Cannot be null.
	 * @param entity 		The entity regarding this preference. Cannot be null.
	 * @param options		The preference options. Cannot be null.
	 */
	public Preference(PrincipalUser user, JPAEntity entity, String options) {
	  setUser(user);
	  setEntity(entity);
	  setOptions(options);
	}
	
	/** Creates a new Preference object.**/
	protected Preference(){}
	
	//~ Methods **************************************************************************************************************************************
	/**
	 * 
	 * @param em 		The entity manager. Cannot be null.
	 * @param user		The user regarding the preference. Cannot be null.
	 * @param entity	The entity regarding the preference. Cannot be null.
	 * @return
	 */
	public static Preference findByUserAndEntity(EntityManager em, PrincipalUser user, JPAEntity entity){
		requireArgument(em != null, "Entity manager cannot be null");
		requireArgument(user != null, "User cannot be null");
		requireArgument(entity != null, "Entity cannot be null");
		
		TypedQuery<Preference> query = em.createNamedQuery("Preference.findByUserAndEntity", Preference.class);
		try {
			query.setParameter("user", user);
			query.setParameter("jpaEntity", entity);
			return query.getSingleResult();
		} catch (NoResultException ex){
			return null;
		}
	}
	
	
	  //~ Methods **************************************************************************************************************************************
	
	@Override
	public BigInteger getId() {
		return id;
	}
	
	/**
	 *  Returns the user.
	 *  
	 * @return The user.
	 */
	public PrincipalUser getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 * 
	 * @param user
	 */
	public void setUser(PrincipalUser user) {
		this.user = user;
	}

	/**
	 * Returns the JPA entity.
	 * 
	 * @return The entity.
	 */
	public JPAEntity getEntity() {
		return entity;
	}

	/**
	 * Sets the JPA entity.
	 * 
	 * @param entity
	 */
	public void setEntity(JPAEntity entity) {
		this.entity = entity;
	}

	/**
	 * Returns the preference options.
	 * 
	 * @return The preference.
	 */
	public String getOptions() {
		return options;
	}

	/**
	 * Sets the preference options.
	 * 
	 * @param preference
	 */
	public void setOptions(String options) {
		this.options = options;
	}
	
	@Override
	public int hashCode(){
		int hash = 3;
		hash = 29 * hash + Objects.hashCode(this.user);
		hash = 29 * hash + Objects.hashCode(this.entity);
		return hash;
	}
	
	@Override	
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		
		final Preference other = (Preference) obj;
		
		if(!Objects.equals(this.user, other.user)){
			return false;
		}
		if(!Objects.equals(this.entity,other.entity)){
			return false;
		}
		return true;
	}
	
	@Override
	public String toString(){
		return "Preference{" + "user=" + user + ", entity=" + entity + ", options=" + options + "}";
	}
   
}
