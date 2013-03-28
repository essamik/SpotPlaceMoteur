/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.heig.comem.services;

import ch.heig.comem.model.Application;
import ch.heig.comem.model.Event;
import ch.heig.comem.model.Rule;
import javax.ejb.Local;

/**
 *
 * @author Karim
 */
@Local
public interface ApplicationManagerLocal {

    Application createApplication(String name, String description, long apiKey, long apiSecret);

    Application addRule(long idApplication, long idRule);

    Application findApplication(long id);

    boolean updateApplication(Application application);

    boolean deleteApplication(long id);

    boolean addEvent(long idApplication, long idEvent);
    
}