/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Application;
import ch.heig.comem.model.Badge;
import ch.heig.comem.model.Rule;
import javax.ejb.Local;

/**
 *
 * @author Karim
 */
@Local
public interface RuleManagerLocal {

    Rule createRule(String onEventType, int numberOfPoints, long idBadge, long idApplication);

    Rule findRule(long id);

    void updateRule(Rule ruleToUpdate);

    void removeRule(long id);
    
    public Rule findRuleByType(String onEventType);
    
}
