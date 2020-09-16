package it.sparks.postinstagram.wsrest;

import it.sparks.postinstagram.utilities.logging.LOG;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("/services")
public class WebServices {

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<?> getDati() {
        LOG.traceIn();
        List<?> dati = new ArrayList();

        LOG.traceOut();
        return dati;
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void newUser(
        @FormParam("id") String id,
        @FormParam("nome") String nome,
        @FormParam("cognome") String cognome,
        @FormParam("eta") String eta,
        @FormParam("telefono") String telefono,
        @FormParam("email") String email,
        @Context HttpServletResponse servletResponse
    ) throws IOException {
        //
    }
}
