/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Application;
import ch.heig.comem.model.Event;
import ch.heig.comem.model.Player;
import java.sql.Timestamp;
import javax.ejb.Local;

/**
 *
 * @author Karim
 */
@Local
public interface EventManagerLocal {

    Event createEvent(long idApplication, long idPlayer, String type);
    Event findEvent(Long idEvent);

    Event updateEvent(Event event);

    void removeEvent(Long eventId);
    
}
