package com.wecancodeit.reviewssite;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
class Review {
  @GeneratedValue
  @Id
  long id;
  String author;
  @Lob
  String content;
  String reviewTitle = "";
  @ManyToOne
  private Game game;

  public Review()
  {
	super();
  }
	
  public Review(String author, String reviewContent, Game gameParent, String reviewTitle)
  {
	this.author = author;
	this.content = reviewContent;
	this.game = gameParent;
	this.reviewTitle = reviewTitle;
  }
  
  public Review(String author, String reviewContent, Game gameParent)
  {
	this.author = author;
	this.content = reviewContent;
	this.game = gameParent;
  }

  public long getId()
  {
    return this.id;
  }

  /**
   * @return the author
   */
  public String getAuthor()
  {
	return this.author;
  }

  /**
   * @return the content
   */
  public String getContent()
  {
    return this.content;
  }

  /**
   * @return the reviewTitle
   */
  public String getReviewTitle()
  {
    return this.reviewTitle;
  }

  /**
   * @return the game
   */
  public Game getGame()
  {
    return game;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (int) (prime * result + ((this.getId() == 0) ? 0 : this.getId()));
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if (obj == null) return false;
    if (this.getClass() != obj.getClass()) return false;
		Review other = (Review) obj;
    if (this.id == 0) {
      if (other.id != 0) return false;
    } else if (this.getId()!=other.id) return false;
    return true;
  }
}
