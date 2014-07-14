/*******************************************************************************
 * Copyright 2014 John D. Ament
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package ws.ament.ms.extension;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by johnament on 7/13/14.
 */
public class RestMonitorExtension implements Extension{
    private Set<Class<?>> restEndpointClasses = new HashSet<>();
    private Set<Class<?>> providerClasses     = new HashSet<>();
    public void watchForRestEndpoints(@Observes @WithAnnotations(Path.class)
                                      ProcessAnnotatedType processAnnotatedType) {
        restEndpointClasses.add(processAnnotatedType.getAnnotatedType().getJavaClass());
    }

    public void watchForProviders(@Observes @WithAnnotations(Provider.class)
                                  ProcessAnnotatedType processAnnotatedType) {
        providerClasses.add(processAnnotatedType.getAnnotatedType().getJavaClass());
    }

    public Set<Class<?>> getRestEndpointClasses() {
        return Collections.unmodifiableSet(restEndpointClasses);
    }

    public Set<Class<?>> getProviderClasses() {
        return Collections.unmodifiableSet(providerClasses);
    }
}
