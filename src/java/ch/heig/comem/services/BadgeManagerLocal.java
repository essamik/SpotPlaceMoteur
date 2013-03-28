/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Badge;
import javax.ejb.Local;

/**
 *
 * @author Karim
 */
@Local
public interface BadgeManagerLocal {

    Badge createBadge(String name, String description, String icon);

    void removeBadge(Long badgeId);

    Badge updateBadge(Badge badge);

    Badge findBadge(Long idBadge);

    Badge addJoueur(long idBadge, long idPlayer);

    void addRule(long idBadge, long idRule);
    
}
