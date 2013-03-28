/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.dto;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pascal
 */
@XmlRootElement
public class ApplicationDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String description;
    private long apiKey;
    private long apiSecret;
    private List<RuleDto> rules = new LinkedList<RuleDto>();
    private List<EventDto> events = new LinkedList<EventDto>();
    
    public List<EventDto> getEvents() {
        return events;
    }

    public void setEvents(List<EventDto> events) {
        this.events = events;
    }

    public List<RuleDto> getRules() {
        return rules;
    }

    public void setRules(List<RuleDto> rules) {
        this.rules = rules;
    }
    
    public void addRule(RuleDto rule) {
      this.rules.add(rule);
    }
    
    public void addEvent(EventDto event){
    this.events.add(event);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getApiKey() {
        return apiKey;
    }

    public void setApiKey(long apiKey) {
        this.apiKey = apiKey;
    }

    public long getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(long apiSecret) {
        this.apiSecret = apiSecret;
    }
    
    
}
