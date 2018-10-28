package com.wecancodeit.reviewssite;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.springframework.stereotype.Repository;

@Entity
class Game {

  @GeneratedValue
  @Id
  private long id;
  private String name;
  private String gameDescriptionShort;
  private String imageUrl;
  @OneToMany(mappedBy="game")
  private Collection<Review> reviews;
  @ManyToMany
  private Collection<Tag> tags;
  
  public Game()
  {
	super();
  }
  
  public Game(String name, String descriptionShort, String imageUrl, Tag ...tags)
  {
	System.out.println("Game constructor. The supplied tags: " + tags.toString());
    this.name = name;
    this.gameDescriptionShort = descriptionShort;
    this.imageUrl = imageUrl;
    this.tags = Arrays.asList(tags);
  }
  public long getId()
  {
    return this.id;
  }
  public String getGameName()
  {
    return this.name;
  }
  
  public String getDescriptionShort()
  {
	return this.gameDescriptionShort;
  }
  public Collection<Review> getReviews()
  {
    return this.reviews;
  }
  
  public String getImageUrl()
  {
	return this.imageUrl;
  }
  
  public void addTag(Tag newTag)
  {
	this.tags.add(newTag);
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
	Game other = (Game) obj;
	if (this.id != other.id)
		return false;
	return true;
}

  public Collection<Tag> getTags()
  {
    return this.tags;
  }
}
