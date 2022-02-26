package com.proyecto.rest;

import com.proyecto.modelo.Matriz;
import com.proyecto.modelo.exceptions.PrerequisitosException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("XMEN")
public class XmenResource {

    @Context
    private UriInfo context;

    public XmenResource() {
    }


    @GET
    @Path("stats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getstats() {
         System.out.println(" getstats");
        Matriz matriz= new Matriz();
              
        return Response.ok((matriz.getStat()).toString(),MediaType.APPLICATION_JSON).build(); 
    }

     
    @POST
    @Path("mutant")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response mutant(Matriz matriz ) {
        //String[] dna = {"ATGCGA","CAGTCC","TTATGT","AGACGG","GCGTCA","TCACTG"};
       // matriz = new Matriz(dna);
      if (matriz == null){
          return Response.status(Response.Status.FORBIDDEN).entity("No se envio cadena de DNA").build();
      }
          
        try {
            if (matriz.isMutant()){
                return Response.ok("Mutante").build();
            }
            else{
                return Response.status(Response.Status.FORBIDDEN).entity("No Mutante").build();
            }
        } catch (PrerequisitosException ex) {
            
            Logger.getLogger(XmenResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.FORBIDDEN).entity(ex.toString()).build();
        } catch(Exception ex){
            Logger.getLogger(XmenResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.FORBIDDEN).entity(ex.toString()).build();
        }
        
    }
}
