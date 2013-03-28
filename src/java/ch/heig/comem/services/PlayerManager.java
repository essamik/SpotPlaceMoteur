/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Badge;
import ch.heig.comem.model.Event;
import ch.heig.comem.model.Player;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Karim
 */
@Stateless
public class PlayerManager implements PlayerManagerLocal {
    @PersistenceContext
    private EntityManager em;
    @EJB
    BadgeManagerLocal badgeManager;
    @EJB
    EventManagerLocal eventManager;

    

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public Player createPlayer(String userName, String mail) {
        Player player = new Player();
        player.setUserName(userName);
        player.setMail(mail);
       this.em.persist(player);
       this.em.flush();
       
       return player;
    }

    @Override
    public void removePlayer(Long idPlayer) {
            Player player = this.em.find(Player.class, idPlayer);
            for(Badge badge : player.getBadges()) {
                if(badge.getPlayers().contains(player)) {
                    badge.getPlayers().remove(player);
                }
            }
            
            //Suppression auto des events en cascade
//            for(Event event : player.getEvents()) {
//                if(event.getPlayer().getId() == player.getId()) {
//                    event.setPlayer(null);
//                }
//            }
            this.em.remove(player);
    }

    @Override
    public Player updatePlayer(Player playerToUpdate) {
        Player player = this.em.find(Player.class, playerToUpdate.getId());
        player.setUserName(playerToUpdate.getUserName());
        player.setMail(playerToUpdate.getMail());
        player.setScore(playerToUpdate.getScore());
        //player.setBadges(playerToUpdate.getBadges());
        for(Badge badge : playerToUpdate.getBadges()) {
            if(!player.getBadges().contains(badge)) {
                this.addBadge(player.getId(), badge.getId());
            }
        }
        
        for(Event event : playerToUpdate.getEvents()) {
            if(!player.getEvents().contains(event)) {
                this.addEvent(player.getId(), event.getId());
            }
        }
        //player.setEvents(playerToUpdate.getEvents());
        
        this.em.persist(player);
        this.em.flush();
        
        return player;
    }

    @Override
    public Player findPlayer(Long idPlayer) {
        Player player =  this.em.find(Player.class, idPlayer);
        return player;
    }
    
    @Override
    public Player addBadge(long idPlayer, long idBadges) {
        Player player = this.findPlayer(idPlayer);
        Badge badge = this.badgeManager.findBadge(idBadges);
        player.addBadge(badge);
        
        this.em.persist(player);
        this.em.flush();
        return player;
    }

    @Override
    public Player addEvent(long idPlayer, long idEvent) {
        Player player = this.findPlayer(idPlayer);
        Event event = eventManager.findEvent(idEvent);
        player.addEvent(event);
        this.em.persist(player);
        this.em.flush();
        return player;
    }
    
}
