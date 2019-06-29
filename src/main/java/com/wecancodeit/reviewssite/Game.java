package com.wecancodeit.reviewssite;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

//import org.springframework.stereotype.Repository;

@Entity
class Game {

  @GeneratedValue
  @Id
  private long id;
  private String name;
  private String gameDescriptionShort;
  private String imageUrl;
  private boolean deleted;
  @OneToMany(mappedBy="game")
  private Collection<Review> reviews;
  @ManyToMany
  @JoinTable(
    name = "game_tag",
    joinColumns = { @JoinColumn(name = "fk_game") },
    inverseJoinColumns = { @JoinColumn(name = "fk_tag") }
  )
  private Set<Tag> tags;
  
  public Game()
  {
	  super();
  }
  
  public Game(String name, String descriptionShort, String imageUrl, Tag ...tags)
  {
    this.name = name;
    this.gameDescriptionShort = descriptionShort;
    this.imageUrl = imageUrl;
    this.tags = new HashSet<>();
    for (Tag t : tags) {
      this.addTag(t);
    }
    this.deleted = false;
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
    if( newTag != null ){
      this.tags.add(newTag);
      newTag.getGames().add(this);
    }
  }
  
  public Set<Tag> getTags()
  {
    return this.tags;
  }
  
  public void removeTag(Tag tagToRemove)
  {
    this.tags.remove(tagToRemove);
  }
  
  public void removeReview(Review reviewToRemove)
  {
    this.reviews.remove(reviewToRemove);
  }
  
  public void delete()
  {
    System.out.println("Game.delete");
    this.deleted = true;
  }

  /**
   * @return Deleted status
   */
  public boolean isDeleted()
  {
    return this.deleted;
  }
  
  @Override
  public int hashCode()
  {
	  final int prime = 31;
	  int result = 1;
	  result = prime * result + (int) (this.id ^ (this.id >>> 32));
	  return result;
  }

  @Override
  public boolean equals(Object obj)
  {
	  if (this == obj) return true;
	  if (obj == null) return false;
	  if (getClass() != obj.getClass()) return false;
	  Game other = (Game) obj;
	  if (this.id != other.id) return false;
	  return true;
  }

  @Override
  public String toString()
  {
	  return "{ type: object, object : { class: Game,name :" + this.name
		    + ", gameDescriptionShort : " + this.gameDescriptionShort + ", imageUrl : "
		    + this.imageUrl + ", deleted: " + this.deleted + "} }";
  }

  @Override
  protected void finalize()
  {
    for (Tag t : this.tags) {
      this.removeTag(t);
    }
  }
}
