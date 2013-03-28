/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Application;
import ch.heig.comem.model.Event;
import ch.heig.comem.model.Player;
import ch.heig.comem.model.Rule;
import java.sql.Timestamp;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Karim
 */
@Stateless
public class EventManager implements EventManagerLocal {

    @PersistenceContext
    private EntityManager em;
    @EJB
    ApplicationManagerLocal applicationManager;
    @EJB
    PlayerManagerLocal playerManager;
    @EJB
    RuleManagerLocal ruleManager;

    public void persist(Object object) {
        em.persist(object);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Event createEvent(long idApplication, long idPlayer, String type) {

        Application application = applicationManager.findApplication(idApplication);
        Player player = playerManager.findPlayer(idPlayer);
        Event event = new Event();
        event.setApplication(application);
        event.setPlayer(player);
        event.setType(type);
        event.setTimeStamp(new Timestamp(new Date().getTime()));

        player.addEvent(event);

        for (Rule r : application.getRules()) {
            if (r.getOnEventType().equals(type)) {
                player.setScore(player.getScore() + r.getNumberOfPoints());
               
                    if (!player.getBadges().contains(r.getBadge())) {
                        player.addBadge(r.getBadge());
                    }
                
            }

        }
        

        this.em.persist(player);
        this.em.persist(event);
        this.em.flush();

        return event;
    }

    //checkforrules
    @Override
    public Event findEvent(Long idEvent) {
        return this.em.find(Event.class, idEvent);
    }

    @Override
    public Event updateEvent(Event event) {
        Event eventToUpdate = this.findEvent(event.getId());
        if (eventToUpdate != null) {
            eventToUpdate.setType(event.getType());
            this.em.persist(eventToUpdate);
            this.em.flush();
        }
        return eventToUpdate;
    }

    @Override
    public void removeEvent(Long idEvent) {
        Event eventToDelete = this.findEvent(idEvent);
        eventToDelete.getApplication().getEvents().remove(eventToDelete);

        this.em.remove(eventToDelete);

    }
}
