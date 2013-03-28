/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.restService;

import ch.heig.comem.dto.ApplicationDto;
import ch.heig.comem.dto.BadgeDto;
import ch.heig.comem.dto.RuleDto;
import ch.heig.comem.model.Rule;
import ch.heig.comem.services.RuleManagerLocal;
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
@Path("rules")
public class RuleFacadeREST extends AbstractFacade<Rule> {

    @Context
    UriInfo uriInfo;
    @PersistenceContext(unitName = "SpotPlaceMoteurPU")
    private EntityManager em;
    @EJB
    private RuleManagerLocal ruleManager;

    public RuleFacadeREST() {
        super(Rule.class);
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public Response createRule(Rule entity) {
        Rule rule = this.ruleManager.createRule(entity.getOnEventType(), entity.getNumberOfPoints(), entity.getBadge().getId(), entity.getApplication().getId());

        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        URI createdUri = ub.path(Long.toString(rule.getId())).build();
        return Response.created(createdUri).build();
    }

    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(Rule entity) {
        this.ruleManager.updateRule(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        this.ruleManager.removeRule(id);
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public RuleDto find(@PathParam("id") Long id) {
        Rule rule = this.ruleManager.findRule(id);
        RuleDto ruleDto = new RuleDto();

        ApplicationDto appDto = new ApplicationDto();
        appDto.setApiKey(rule.getApplication().getApiKey());
        appDto.setApiSecret(rule.getApplication().getApiSecret());
        appDto.setDescription(rule.getApplication().getDescription());
        appDto.setId(rule.getApplication().getId());
        appDto.setName(rule.getApplication().getName());
        ruleDto.setApplication(appDto);

        BadgeDto badgeDto = new BadgeDto();
        badgeDto.setId(rule.getBadge().getId());
        badgeDto.setName(rule.getBadge().getName());
        ruleDto.setBadge(badgeDto);

        ruleDto.setId(rule.getId());
        ruleDto.setNumberOfPoints(rule.getNumberOfPoints());
        ruleDto.setOnEventType(rule.getOnEventType());

        return ruleDto;
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<RuleDto> findAllRules() {
        List<Rule> rules = super.findAll();
        List<RuleDto> rulesDto = new LinkedList<RuleDto>();

        for (Rule r : rules) {
            RuleDto rdto = new RuleDto();
            rdto.setId(r.getId());
            rdto.setNumberOfPoints(r.getNumberOfPoints());
            rdto.setOnEventType(r.getOnEventType());
            BadgeDto bdto = new BadgeDto();
            bdto.setId(r.getBadge().getId());
            bdto.setName(r.getBadge().getName());
            rdto.setBadge(bdto);
            rulesDto.add(rdto);
        }
        return rulesDto;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Rule> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
