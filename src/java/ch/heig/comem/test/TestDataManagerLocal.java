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
import javax.ejb.Local;

/**
 *
 * @author Karim
 */
@Local
public interface TestDataManagerLocal {

 
    void populateDatabase();

}
