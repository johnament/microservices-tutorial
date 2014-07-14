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

package ws.ament.ms.server;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ListenerInfo;
import io.undertow.servlet.api.ServletInfo;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.jboss.resteasy.cdi.CdiInjectorFactory;
import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher;
import org.jboss.resteasy.spi.ResteasyDeployment;
import ws.ament.ms.config.ServerConfigurationManager;
import ws.ament.ms.extension.RestMonitorExtension;
import ws.ament.ms.listener.CDIListener;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.ServletException;

/**
 * Created by johnament on 7/13/14.
 */
@ApplicationScoped
public class WebServerLauncher {
    @Inject
    private RestMonitorExtension restMonitorExtension;

    @Inject
    private ServerConfigurationManager configurationManager;

    @Inject private Event<ExceptionToCatchEvent> catchEvent;

    private Undertow undertow;

    public void start() {
        ResteasyDeployment deployment = new ResteasyDeployment();
        deployment.getActualResourceClasses().addAll(restMonitorExtension.getRestEndpointClasses());
        deployment.getActualProviderClasses().addAll(restMonitorExtension.getProviderClasses());
        deployment.setInjectorFactoryClass(CdiInjectorFactory.class.getName());

        ListenerInfo listener = Servlets.listener(CDIListener.class);

        ServletInfo resteasyServlet = Servlets.servlet("ResteasyServlet", HttpServlet30Dispatcher.class)
                .setAsyncSupported(true)
                .setLoadOnStartup(1)
                .addMapping("/*");

        DeploymentInfo di = new DeploymentInfo()
                .addListener(listener)
                .setContextPath(configurationManager.getContextRoot())
                .addServletContextAttribute(ResteasyDeployment.class.getName(), deployment)
                .addServlet(resteasyServlet).setDeploymentName("ResteasyUndertow")
                .setClassLoader(ClassLoader.getSystemClassLoader());

        DeploymentManager deploymentManager = Servlets.defaultContainer().addDeployment(di);
        deploymentManager.deploy();
        Undertow server = null;
        try {
            server = Undertow.builder()
                    .addHttpListener(configurationManager.getPort(), configurationManager.getBindAddress())
                    .setHandler(deploymentManager.start())
                    .build();
        } catch (ServletException e) {
            catchEvent.fire(new ExceptionToCatchEvent(e));
        }
        server.start();
        this.undertow = server;
    }

    public void stop() {
        this.undertow.stop();
    }
}
