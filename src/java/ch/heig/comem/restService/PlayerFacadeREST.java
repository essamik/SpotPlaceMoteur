/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.restService;

import ch.heig.comem.dto.BadgeDto;
import ch.heig.comem.dto.PlayerDto;
import ch.heig.comem.model.Badge;
import ch.heig.comem.model.Player;
import ch.heig.comem.services.PlayerManagerLocal;
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
@Path("players")
public class PlayerFacadeREST extends AbstractFacade<Player> {
    
    @Context UriInfo uriInfo;

    @EJB
    private PlayerManagerLocal playerManager;
    @PersistenceContext(unitName = "SpotPlaceMoteurPU")
    private EntityManager em;

    public PlayerFacadeREST() {
        super(Player.class);
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public Response createPlayer(Player entity) {
//        super.create(entity);
        Player player = playerManager.createPlayer(entity.getUserName(), entity.getMail());
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        URI createdUri = ub.path(Long.toString(player.getId())).build();
        return Response.created(createdUri).build();
    }

    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(Player entity) {
//        super.edit(entity);
        playerManager.updatePlayer(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        //super.remove(super.find(id));
        playerManager.removePlayer(id);
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public PlayerDto find(@PathParam("id") Long id) {
//        return super.find(id);
        Player player =  playerManager.findPlayer(id);
        PlayerDto pdto = new PlayerDto();
        pdto.setId(player.getId());
        pdto.setMail(player.getMail());
        pdto.setScore(player.getScore());
        pdto.setUserName(player.getUserName());
        
        List<BadgeDto> badgesDto = new LinkedList<BadgeDto>();
        
       for(Badge b : player.getBadges()){
           BadgeDto bdto = new BadgeDto();
           bdto.setId(b.getId());
           bdto.setName(b.getName());
           bdto.setDescription(b.getDescription());
           bdto.setIcon(b.getIcon());
           badgesDto.add(bdto);
       }
       pdto.setBadges(badgesDto); 
       
       return pdto;
    }

//    @GET
//    @Override
//    @Produces({"application/xml", "application/json"})
//    public List<Player> findAll() {
//        return super.findAll();
//    }
    @GET
    @Produces({"application/xml", "application/json"})
    public List<PlayerDto> findAllPlayers() {
        List<Player> players = super.findAll();
        List<PlayerDto> playersDto = new LinkedList<PlayerDto>();

        for (Player p : players) {
            List<BadgeDto> badgesDto = new LinkedList<BadgeDto>();

            PlayerDto playerDto = new PlayerDto();
            playerDto.setUserName(p.getUserName());
            playerDto.setMail(p.getMail());
            playerDto.setScore(p.getScore());

            playersDto.add(playerDto);
        }

        return playersDto;

    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Player> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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