/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.heig.comem.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Karim
 */
@Entity
@XmlRootElement
public class Application implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private long apiKey;
  
    private long apiSecret;
    
    @OneToMany(mappedBy = "application")
    
    private List<Rule> rules = new LinkedList<Rule>();
     @OneToMany(mappedBy= "application")
     
     private List<Event> events = new LinkedList<Event>();
    
    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
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
    
    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }
    
    public void addRule(Rule rule) {
      this.rules.add(rule);
    }
    
    public void addEvent(Event event){
    this.events.add(event);
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
    
    public Application() {
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Application)) {
            return false;
        }
        Application other = (Application) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ch.heig.comem.model.Application[ id=" + id + " ]";
    }



}