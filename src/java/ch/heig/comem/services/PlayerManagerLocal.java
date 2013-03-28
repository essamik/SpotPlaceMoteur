/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Badge;
import ch.heig.comem.model.Event;
import ch.heig.comem.model.Player;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Karim
 */
@Local
public interface PlayerManagerLocal {

    Player createPlayer(String userName, String mail);

    void removePlayer(Long idPlayer);

    Player updatePlayer(Player player);

    Player findPlayer(Long idPlayer);

    Player addEvent(long idPlayer, long idEvent);

    Player addBadge(long idPlayer, long idBadges);

}
