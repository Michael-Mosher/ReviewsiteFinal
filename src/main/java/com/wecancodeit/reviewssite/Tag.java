package com.wecancodeit.reviewssite;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Tag {
  @GeneratedValue
  @Id
  private long id;
  private String name;
  @ManyToMany
  private Collection<Game> games;
  
  public Tag()
  {
	super();
  }
  
  public Tag(String tag)
  {
	  this.name = tag;
  }
  
  public String getTag()
  {
	return this.name;
  }
  
  @Override
  public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (id ^ (id >>> 32));
	return result;
  }

  @Override
  public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Tag other = (Tag) obj;
	if (id != other.id)
		return false;
	return true;
  }

  public long getId() {
    return this.id;
  }
}
