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

package com.salesforce.dva.argus.service.preference;

import javax.persistence.EntityManager;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.salesforce.dva.argus.entity.JPAEntity;
import com.salesforce.dva.argus.entity.Preference;
import com.salesforce.dva.argus.entity.PrincipalUser;
import com.salesforce.dva.argus.inject.SLF4JTypeListener;
import com.salesforce.dva.argus.service.PreferenceService;
import com.salesforce.dva.argus.service.jpa.DefaultJPAService;
import com.salesforce.dva.argus.system.SystemConfiguration;
import static com.salesforce.dva.argus.system.SystemAssert.requireArgument;

public class DefaultPreferenceService extends DefaultJPAService implements PreferenceService {

	 //~ Instance fields ******************************************************************************************************************************

    @SLF4JTypeListener.InjectLogger
    private Logger _logger;
    
    @Inject
    private Provider<EntityManager> emf;
    
    //~ Methods **************************************************************************************************************************************
	protected DefaultPreferenceService(SystemConfiguration config) {
		//TODO Decide whether an audit is needed here
		super(null, config);
	}

	@Override
	@Transactional
	public Preference updatePreference(Preference preference) {
		requireNotDisposed();
		requireArgument(preference != null, "Cannot update a null preference");
		
		EntityManager em = emf.get();
		Preference result = mergeEntity(em, preference);
		
		em.flush();
		_logger.debug("Updated preference to : {}", result);
		
		return result;
	}

	@Override
	@Transactional
	public Preference findByUserAndEntity(PrincipalUser user, JPAEntity entity) {
		requireNotDisposed();
		requireArgument(user != null, "User of the preference cannot be null");
		requireArgument(entity != null, "Entity of the preference cannot be null");
		_logger.debug("Querying Preference by user: {} and entity: {}", user, entity);
		
		Preference result = Preference.findByUserAndEntity(emf.get(), user, entity);
		
		_logger.debug("Found Preference: {}", result);
		return result;
	}

}
