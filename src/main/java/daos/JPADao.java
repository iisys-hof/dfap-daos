/*
 * Copyright 2018 Thomas Winkler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package daos;


import utils.XEntityManagerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;


public abstract class JPADao implements Dao {
    protected Class entityClass;
    //  @PersistenceContext(unitName = "dfap")
    @Inject
    @XEntityManagerFactory
    protected EntityManager em;


    public JPADao() {

    }

    @Override
    public void create(DfapEntity entity) {

    }

    @Override
    public void update(DfapEntity entity) {

    }

    @Override
    public DfapEntity read(DfapEntity id) {
        return null;
    }

    @Override
    public void delete(DfapEntity entity) {

    }
}
