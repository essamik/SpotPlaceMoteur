/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.restService;

import ch.heig.comem.dto.ApplicationDto;
import ch.heig.comem.dto.EventDto;
import ch.heig.comem.dto.PlayerDto;
import ch.heig.comem.model.Event;
import ch.heig.comem.services.EventManagerLocal;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Pascal
 */
@Stateless
@Path("events")
public class EventFacadeREST extends AbstractFacade<Event> {

    @Context
    UriInfo uriInfo;
    @EJB
    private EventManagerLocal eventManager;
    @PersistenceContext(unitName = "SpotPlaceMoteurPU")
    private EntityManager em;

    public EventFacadeREST() {
        super(Event.class);
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public Response createEvents(Event entity) {
        Event ev = this.eventManager.createEvent(entity.getApplication().getId(), entity.getPlayer().getId(), entity.getType());
        
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        URI createdUri = ub.path(Long.toString(ev.getId())).build();
        return Response.created(createdUri).build();
    }

    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(Event entity) {
        this.eventManager.updateEvent(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public EventDto find(@PathParam("id") Long id) {
        
        Event ev = this.eventManager.findEvent(id);
        EventDto evDto = new EventDto();
        evDto.setId(ev.getId());
        
        PlayerDto playerDto = new PlayerDto();
        playerDto.setId(ev.getPlayer().getId());
        playerDto.setUserName(ev.getPlayer().getUserName());
        evDto.setPlayer(playerDto);
        
        evDto.setTimeStamp(ev.getTimeStamp());
        evDto.setType(ev.getType());
        
        ApplicationDto appDto = new ApplicationDto();
        appDto.setId(ev.getApplication().getId());
        appDto.setName(ev.getApplication().getName());
        appDto.setApiKey(ev.getApplication().getApiKey());
        appDto.setApiSecret(ev.getApplication().getApiSecret());
        appDto.setDescription(ev.getApplication().getDescription());
        evDto.setApplication(appDto);
        
        return evDto;
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<EventDto> findAllEvents() {
        List<Event> events = super.findAll();
        List<EventDto> eventsDto = new LinkedList<EventDto>();

        for (Event e : events) {
            EventDto edto = new EventDto();
            edto.setId(e.getId());
            edto.setTimeStamp(e.getTimeStamp());
            edto.setType(e.getType());
            eventsDto.add(edto);
//            PlayerDto pdto = new PlayerDto();
//            pdto.setId(e.getPlayer().getId());
//            pdto.setUserName(e.getPlayer().getUserName());
//            edto.setPlayer(pdto);
        }
        return eventsDto;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Event> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
