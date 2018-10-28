/*package com.wecancodeit.reviewssite;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
class ReviewRepository {
  Map<Long, Review> reviews = new HashMap<>();
  int iReviewIndex = 0;
  
  public ReviewRepository()
  { // Title, imageUrl, Category, Content
	this.reviews.put(100L, new Review(100L, "We Happy Few", "wehappyfew.jpg", "Survival", "ThaZombieGirl: So i just went in to one of my saves for Sally and it was bugging out i couldn't walk around only turn on the spot, then when i loaded the other sally save up i went in to the garden district where i changed her clothes for the area but still got attacked for no reason? then when i died in the save it loaded me back up as arther, and it was laggy as hell. when i did arthers story it wasnt glichy at all, but i took a few weeks off the game and now it is. please fix! really did enjoy up to this point but cant recomend it as of now due to these issues. "));
	this.reviews.put(101L, new Review(101L, "Crusader Kings II", "ckii.jpg", "Strategy, Historical", "dario2213714: cool i guess... didnt play it that much "));
	this.reviews.put(102L, new Review(102L, "Dishonored 2", "dis2.jpg", "Stealth", "Waterclock and the Golden Gold: this and dark messiah taught me one oddly specific thing: arkane does the best kicking mechanics i've ever seen in any game because your leg is apparently the god hand"));
	this.reviews.put(103L, new Review(103L, "The Elder Scrolls V: Skyrim", "skyrim.jpg", "Adventure", "Klappspaten: This game is the love of my life and i'm not ashamed of it.\n*installs bouncy boobs mod*"));
  }
  
  public Review getEntry(long lReviewId)
  {
	  return this.reviews.get(lReviewId);
//    if(this.reviews.containsKey(new Long(sEntryId))) {
//	  Review answer = this.reviews.get(new Long(sEntryId));
//	  return answer;
//	} else {
//	  return new Review(0,"", "", "", "");
//    }
  }
  
  public Collection<Review> getAllEntries()
  {
    return this.reviews.values();
  }
  
  public int getFullRepositoryCount()
  {
	return this.reviews.size();
  }
  
  public Review getCurrentEntry()
  {
	Review[] aCollectionToArray = new Review[this.getFullRepositoryCount()];
	this.reviews.values().toArray(aCollectionToArray);
	return aCollectionToArray[this.iReviewIndex];
  }
  
  public void nextEntry()
  {
	this.iReviewIndex++;
  }
  
  public boolean isValidIndex()
  {
	return this.iReviewIndex < this.getFullRepositoryCount();
  }
  
  public void resetIndex()
  {
	this.iReviewIndex = 0;
  }

  public boolean isValidIndex(long reviewId)
  {
    return this.reviews.containsKey(new Long(reviewId));
  }
}
*/