/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Application;
import ch.heig.comem.model.Event;
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
public class ApplicationManager implements ApplicationManagerLocal {

    @PersistenceContext
    private EntityManager em;
    @EJB
    RuleManagerLocal ruleManager;
    @EJB
    EventManagerLocal eventManager;

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public Application createApplication(String name, String description, long apiKey, long apiSecret) {
        Application application = new Application();
        application.setName(name);
        application.setDescription(description);
        application.setApiKey(apiKey);
        application.setApiSecret(apiSecret);

        this.em.persist(application);
        this.em.flush();

        return application;
    }

    @Override
    public Application addRule(long idApplication, long idRule) {
        Application application = this.findApplication(idApplication);
        Rule rule = this.ruleManager.findRule(idRule);
        application.addRule(rule);
        this.em.persist(application);
        this.em.flush();
        return application;
    }

    @Override
    public Application findApplication(long id) {
        Application application = this.em.find(Application.class, id);
        return application;
    }

    @Override
    public boolean updateApplication(Application applicationToUpdate) {
        Application application = this.em.find(Application.class, applicationToUpdate.getId());
        application.setName(applicationToUpdate.getName());
        application.setDescription(applicationToUpdate.getDescription());
        application.setApiSecret(applicationToUpdate.getApiKey());
        application.setApiKey(applicationToUpdate.getApiKey());

        for (Event e : applicationToUpdate.getEvents()) {
            if (!application.getEvents().contains(e)) {
                this.addEvent(application.getId(), e.getId());
            }
        }

        for (Rule r : applicationToUpdate.getRules()) {
            if (!application.getRules().contains(r)) {
                this.addRule(application.getId(), r.getId());
            }
        }

        this.em.persist(application);
        this.em.flush();
        return true;
    }

    @Override
    public boolean deleteApplication(long id) {

        Application application = this.em.find(Application.class, id);

        for (Event e : application.getEvents()) {
            e.setApplication(null);
        }

        for (Rule e : application.getRules()) {
            e.setApplication(null);
        }

        this.em.remove(application);
        this.em.flush();
        return true;
    }

    @Override
    public boolean addEvent(long idApplication, long idEvent) {
        Application application = this.findApplication(idApplication);
        Event event = eventManager.findEvent(idEvent);
        application.addEvent(event);
        this.em.persist(application);
        this.em.flush();
        return true;
    }
}
