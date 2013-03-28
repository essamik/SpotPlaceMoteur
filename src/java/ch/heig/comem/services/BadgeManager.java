/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Badge;
import ch.heig.comem.model.Event;
import ch.heig.comem.model.Player;
import ch.heig.comem.model.Rule;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Karim
 */
@Stateless
public class BadgeManager implements BadgeManagerLocal {

    @PersistenceContext
    private EntityManager em;
    @EJB
    private PlayerManagerLocal playerManager;
    @EJB
    private RuleManagerLocal ruleManager;

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public Badge createBadge(String name, String description, String icon) {
        Badge badge = new Badge();
        badge.setName(name);
        badge.setDescription(description);
        badge.setIcon(icon);

        this.em.persist(badge);
        this.em.flush();

        return badge;
    }

    @Override
    public void removeBadge(Long badgeId) {
        Badge badgeToRemove = this.em.find(Badge.class, badgeId);

        //We have to set to null the badge field from the rules associated with this badge
        for(Player player : badgeToRemove.getPlayers()) {
            if(player.getBadges().contains(badgeToRemove)) {
                player.getBadges().remove(badgeToRemove);
            }
        }
        
        for(Rule rule : badgeToRemove.getRules()) {
            if(rule.getBadge().getId() == badgeToRemove.getId())
                rule.setBadge(null);
        }

        this.em.remove(badgeToRemove);
    }

    @Override
    public Badge updateBadge(Badge badgeToUpdate) {
        Badge badge = this.em.find(Badge.class, badgeToUpdate.getId());
        if (badge != null) {
            badge.setName(badgeToUpdate.getName());
            badge.setDescription(badgeToUpdate.getDescription());
            badge.setIcon(badgeToUpdate.getDescription());
            for (Rule rule : badgeToUpdate.getRules()) {
                if (!badge.getRules().contains(rule)) {
                    this.addRule(badge.getId(), rule.getId());
                }
            }
            this.em.persist(badge);
            this.em.flush();
        }
        return badge;
    }

    @Override
    public Badge findBadge(Long idBadge) {
        return this.em.find(Badge.class, idBadge);
    }

    @Override
    public Badge addJoueur(long idBadge, long idPlayer) {
        Badge badge = this.findBadge(idBadge);
        Player player = playerManager.findPlayer(idPlayer);
        badge.addPlayer(player);
        this.em.persist(badge);
        return badge;
    }

    @Override
    public void addRule(long idBadge, long idRule) {
        Badge badge = findBadge(idBadge);
        Rule rule = ruleManager.findRule(idRule);
        badge.addRule(rule);
        this.em.persist(badge);
        this.em.flush();

    }
}
