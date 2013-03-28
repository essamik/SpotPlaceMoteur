/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.test;

import ch.heig.comem.model.Application;
import ch.heig.comem.model.Badge;
import ch.heig.comem.model.Event;
import ch.heig.comem.model.Player;
import ch.heig.comem.model.Rule;
import ch.heig.comem.services.ApplicationManagerLocal;
import ch.heig.comem.services.BadgeManagerLocal;
import ch.heig.comem.services.EventManagerLocal;
import ch.heig.comem.services.PlayerManagerLocal;
import ch.heig.comem.services.RuleManagerLocal;
import java.sql.Timestamp;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author Karim
 */
@Stateless
@WebService
public class TestDataManager implements TestDataManagerLocal {

    @EJB
    private RuleManagerLocal ruleManager;
    @EJB
    private EventManagerLocal eventManager;
    @EJB
    private ApplicationManagerLocal applicationManager;
    @EJB
    private PlayerManagerLocal playerManager;
    @EJB
    private BadgeManagerLocal badgeManager;



   @Override
    public void populateDatabase() {
        Application app = this.applicationManager.createApplication("SpotPlace", "THE app", 12, 13);
               
  
        
        // Badges par rapport au score
        Badge badgeChoc = this.badgeManager.createBadge("Medaille de chocolat", "La medaille comestible", "badge_choc.png");
        Badge badgeBronze = this.badgeManager.createBadge("Medaille de bronze", "La medaille des newbie", "badge_bronze.png");
        Badge badgeArgent = this.badgeManager.createBadge("Medaille d'argent", "La medaille des Like a Sir", "badge_argent.png");
        Badge badgeOr = this.badgeManager.createBadge("Medaille d'or", "La medaille des Kings", "badge_or.png");
        
        //Badge par rapport au Event
        Badge badgeLike = this.badgeManager.createBadge("Medaille de follower", "La medaille des likers", "badge_like.png");
        Badge badgeComment = this.badgeManager.createBadge("Medaille de chatteur", "La medaille des commenteurs", "badge_comment.png");
        Badge badgeSpot = this.badgeManager.createBadge("Medaille d'explorer", "La medaille des Spoteurs", "badge_spot.png");
        
        // Règles sans badges        
        Rule ruleLike = this.ruleManager.createRule("like", 2, 0, app.getId());
        Rule ruleComment = this.ruleManager.createRule("comment", 5, 0, app.getId());
        Rule ruleSpot = this.ruleManager.createRule("spot", 10, 0, app.getId());
        
        // Règles à badge
        Rule ruleBadgeChoc = this.ruleManager.createRule("20", 10, badgeChoc.getId(), app.getId());
        Rule ruleBadgeBronze = this.ruleManager.createRule("45", 10, badgeBronze.getId(), app.getId());
        Rule ruleBadgeArgent = this.ruleManager.createRule("70", 10, badgeArgent.getId(), app.getId());
        Rule ruleBadgeOr = this.ruleManager.createRule("100", 10, badgeOr.getId(), app.getId());
        Rule ruleBadgeLike = this.ruleManager.createRule("likes", 10, badgeLike.getId(), app.getId());
        Rule ruleBadgeComment = this.ruleManager.createRule("comments", 10, badgeComment.getId(), app.getId());
        Rule ruleBadgeSpot = this.ruleManager.createRule("spots", 10, badgeSpot.getId(), app.getId());
        
 
    }


}
