/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Application;
import ch.heig.comem.model.Badge;
import ch.heig.comem.model.Rule;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Karim
 */
@Stateless
public class RuleManager implements RuleManagerLocal {

    @PersistenceContext
    private EntityManager em;
    @EJB
    BadgeManagerLocal badgeManager;
    @EJB
    ApplicationManagerLocal applicationManager;

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public Rule createRule(String onEventType, int numberOfPoints, long idBadge, long idApplication) {
              
        
        Application application = applicationManager.findApplication(idApplication);
        Rule rule = new Rule();
        rule.setOnEventType(onEventType);
        rule.setNumberOfPoints(numberOfPoints);
          
        if(idBadge!=0){
        Badge badge = badgeManager.findBadge(idBadge);
        rule.setBadge(badge);
        badge.addRule(rule);
        this.em.persist(badge);
        }
        
        rule.setApplication(application);
        //applicationManager.addRule(idApplication, idBadge); //TEST
        
        application.addRule(rule);

        this.em.persist(application);
        
        this.em.persist(rule);
        this.em.flush();
        
        
        
//        badge.addRule(rule);
        return rule;
    }

    @Override
    public Rule findRule(long id) {
        return this.em.find(Rule.class, id);
    }
     @Override
    public Rule findRuleByType(String onEventType) {
        Query q = em.createNamedQuery("findUserByName");
        q.setParameter("onEventType", onEventType);
        List<Rule> candidates = q.getResultList();
        if (candidates.isEmpty()) {
            return null;
        } else {
            return candidates.get(0);
        }
    }

    @Override
    public void updateRule(Rule ruleToUpdate) {
        Rule rule = this.em.find(Rule.class, ruleToUpdate.getId());
        rule.setBadge(ruleToUpdate.getBadge());
        rule.setNumberOfPoints(ruleToUpdate.getNumberOfPoints());
        rule.setOnEventType(ruleToUpdate.getOnEventType());

        this.em.persist(rule);
        this.em.flush();
    }

    @Override
    public void removeRule(long id) {
        Rule rule = this.em.find(Rule.class, id);
        Application app = rule.getApplication();

        if (app.getRules().contains(rule)) {
            app.getRules().remove(rule);
        }

        if (rule.getBadge().getRules().contains(rule)) {
            rule.getBadge().getRules().remove(rule);
        }
        this.em.remove(rule);
    }

}
