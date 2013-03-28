/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.restService;

import ch.heig.comem.dto.ApplicationDto;
import ch.heig.comem.dto.BadgeDto;
import ch.heig.comem.dto.EventDto;
import ch.heig.comem.dto.RuleDto;
import ch.heig.comem.model.Application;
import ch.heig.comem.model.Event;
import ch.heig.comem.model.Rule;
import ch.heig.comem.services.ApplicationManagerLocal;
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
@Path("applications")
public class ApplicationFacadeREST extends AbstractFacade<Application> {

    @Context
    UriInfo uriInfo;
    @PersistenceContext(unitName = "SpotPlaceMoteurPU")
    private EntityManager em;
    @EJB
    private ApplicationManagerLocal applicationManager;

    public ApplicationFacadeREST() {
        super(Application.class);
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public Response createApplication(Application entity) {
        Application app = this.applicationManager.createApplication(entity.getName(), entity.getDescription(), entity.getApiKey(), entity.getApiSecret());

        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        URI createdUri = ub.path(Long.toString(app.getId())).build();
        return Response.created(createdUri).build();
    }

    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(Application entity) {
        this.applicationManager.updateApplication(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        this.applicationManager.deleteApplication(id);
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public ApplicationDto find(@PathParam("id") Long id) {
        Application app = this.applicationManager.findApplication(id);
        ApplicationDto appDto = new ApplicationDto();

        appDto.setApiKey(app.getApiKey());
        appDto.setApiSecret(app.getApiSecret());
        appDto.setDescription(app.getDescription());
        appDto.setId(app.getId());
        appDto.setName(app.getName());

        List<RuleDto> rulesDto = new LinkedList<RuleDto>();
        for (Rule rule : app.getRules()) {
            RuleDto ruleDto = new RuleDto();
            ruleDto.setId(rule.getId());

            BadgeDto badgeDto = new BadgeDto();
            badgeDto.setName(rule.getBadge().getName());
            badgeDto.setId(rule.getBadge().getId());
            ruleDto.setBadge(badgeDto);

            ruleDto.setNumberOfPoints(rule.getNumberOfPoints());
            ruleDto.setOnEventType(rule.getOnEventType());
            rulesDto.add(ruleDto);
        }
        appDto.setRules(rulesDto);

        List<EventDto> eventsDto = new LinkedList<EventDto>();
        for (Event event : app.getEvents()) {
            EventDto eventDto = new EventDto();
            eventDto.setId(event.getId());
            eventDto.setTimeStamp(event.getTimeStamp());
            eventDto.setType(event.getType());
            eventsDto.add(eventDto);
        }
        appDto.setEvents(eventsDto);

        return appDto;
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<ApplicationDto> findAllApplication() {

        List<Application> apps = super.findAll();
        List<ApplicationDto> appsDto = new LinkedList<ApplicationDto>();
        for (Application app : apps) {
            ApplicationDto appDto = new ApplicationDto();
            appDto.setApiKey(app.getApiKey());
            appDto.setApiSecret(app.getApiSecret());
            appDto.setDescription(app.getDescription());
            appDto.setId(app.getId());
            appDto.setName(app.getName());
            appsDto.add(appDto);
        }
        
        return appsDto;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Application> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
