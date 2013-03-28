/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.restService;

import ch.heig.comem.dto.BadgeDto;
import ch.heig.comem.dto.PlayerDto;
import ch.heig.comem.dto.RuleDto;
import ch.heig.comem.model.Badge;
import ch.heig.comem.model.Player;
import ch.heig.comem.model.Rule;
import ch.heig.comem.services.BadgeManagerLocal;
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
@Path("badges")
public class BadgeFacadeREST extends AbstractFacade<Badge> {

    @Context
    UriInfo uriInfo;
    @EJB
    private BadgeManagerLocal badgeManager;
    @PersistenceContext(unitName = "SpotPlaceMoteurPU")
    private EntityManager em;

    public BadgeFacadeREST() {
        super(Badge.class);
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public Response createBadge(Badge entity) {
        //super.create(entity);
        Badge badge = badgeManager.createBadge(entity.getName(), entity.getDescription(), entity.getIcon());
        
        
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        URI createdUri = ub.path(Long.toString(badge.getId())).build();
        return Response.created(createdUri).build();
    }

    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(Badge entity) {
        //super.edit(entity);
        badgeManager.updateBadge(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
//        super.remove(super.find(id));
        badgeManager.removeBadge(id);
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public BadgeDto find(@PathParam("id") Long id) {
//        return super.find(id);
        Badge badge = badgeManager.findBadge(id);
        BadgeDto bdto = new BadgeDto();
        bdto.setId(badge.getId());
        bdto.setName(badge.getName());
        bdto.setDescription(badge.getDescription());
        bdto.setIcon(badge.getIcon());
        List<PlayerDto> playersDto = new LinkedList<PlayerDto>();
        List<RuleDto> rulesDto = new LinkedList<RuleDto>();
        
        for (Player p : badge.getPlayers()) {
            PlayerDto pdto = new PlayerDto();
            pdto.setId(p.getId());
            pdto.setUserName(p.getUserName());
            pdto.setMail(p.getMail());
            pdto.setScore(p.getScore());
            playersDto.add(pdto);
        }
        bdto.setPlayers(playersDto);

        for (Rule r : badge.getRules()) {
            RuleDto rdto = new RuleDto();
            rdto.setId(r.getId());
            rdto.setNumberOfPoints(r.getNumberOfPoints());
            rdto.setOnEventType(r.getOnEventType());
            rulesDto.add(rdto);
        }
        bdto.setRules(rulesDto);


        return bdto;
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<BadgeDto> findAllBadges() {
        List<Badge> badges = super.findAll();
        List<BadgeDto> badgesDto = new LinkedList<BadgeDto>();
        for (Badge b : badges) {
            BadgeDto bdto = new BadgeDto();
            bdto.setId(b.getId());
            bdto.setName(b.getName());
            bdto.setIcon(b.getIcon());
            bdto.setDescription(b.getDescription());
//            List<PlayerDto> playersDto = new LinkedList<PlayerDto>();
//            
//            for (Player p : b.getPlayers()) {
//                PlayerDto pdto = new PlayerDto();
//                pdto.setId(p.getId());
//                pdto.setUserName(p.getUserName());
//                pdto.setMail(p.getMail());
//                pdto.setScore(p.getScore());
//                playersDto.add(pdto);
//            }
            badgesDto.add(bdto);
        }
        return badgesDto;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Badge> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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