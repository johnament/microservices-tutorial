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

package ws.ament.ms.auth.api;

import ws.ament.ms.auth.model.LoginModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Handles application authentication.
 */
@Path("/authentication")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AuthenticationEndpoint {
    /**
     * Validates the credential information passed in via a login model.
     *
     * @param loginModel
     * @return a LoginResponse object is contained as the entity.  It is wrapped in 200 if everything is OK,
     *  401 if not authorized.
     */
    @POST
    public Response validate(LoginModel loginModel);

    /**
     * Validates a session Id
     * @param sessionId the sessionId to query for.  For this method, clients must be externally authenticated.
     * @return a Response that contains a {@see UserModel} if the session is valid.
     * Returns back a 401 if the external auth is not set.
     * Returns back a 404 if the sessionId is not present.
     */
    @GET
    public Response validate(@QueryParam("sessionId") String sessionId);
}
