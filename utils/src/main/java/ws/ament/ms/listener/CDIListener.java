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

package ws.ament.ms.listener;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.api.ContextControl;

import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * Created by johnament on 7/13/14.
 */
public class CDIListener implements ServletRequestListener{

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        ContextControl contextControl = container.getContextControl();
        contextControl.stopContext(RequestScoped.class);
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        ContextControl contextControl = container.getContextControl();
        contextControl.startContext(RequestScoped.class);
    }
}
