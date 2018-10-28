package com.wecancodeit.reviewssite;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javax.annotation.Resource;

import org.assertj.core.util.Arrays;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class ReviewsiteController {
  
  @Resource
  IReviewRepository oReviewRepository;
  @Resource
  IGameRepository oGameRepository;
  @Resource
  ITagRepository oTagRepository;
  
//  @RequestMapping("/reviews")
//  public String findAllReviews(Model model)
//  {
//    model.addAttribute("reviews", oReviewRepository.getAllEntries());
//	return "reviews";
//  }
//  
//  @RequestMapping("/review")
//  public String findAReview(@RequestParam(value="reviewId", required=false, 
//	      defaultValue="")String reviewId, Model model)
//  {
//	  model.addAttribute("review", oReviewRepository.getEntry(new Long(reviewId)));
//	  return "review";
//  }
  @RequestMapping("/game")
	public String findOneGame(@RequestParam(value="gameId") long lGameId, Model model) throws GameNotFoundException
	{
	  Optional<Game> game = oGameRepository.findById(lGameId);
	  if(game.isPresent()){
		model.addAttribute("gameQueried", game.get());
  		Collection<Tag> oAssociatedTags = oTagRepository.findByGamesContains(game.get());
//		Collection<Review> oAssociatedReview = oReviewRepository.findByGame(game.get());
  		model.addAttribute("associatedTags", oAssociatedTags);
//		model.addAttribute("associatedReviews", oAssociatedReview);
		return "game";
	  } else {
		  throw new GameNotFoundException();
	  }
	}

	@RequestMapping("/games")
	public String findAllGames(
			Model model/*,
			@RequestParam(value="tagName", defaultValue="", required=false) String sTagName,
			@RequestParam(value="tagId", defaultValue="", required=false) long lTagId*/
	)
	{
	  Collection<Game> oQueryResult;
	  /*if(sTagName.length()>0) {
		oQueryResult = oGameRepository.findByTagsContains(new Tag(sTagName));
	  } else if(new Long(lTagId).longValue() > 0L) {
		oQueryResult = oGameRepository.findByTagsId(new Long(lTagId));
	  } else */{
		oQueryResult = (Collection<Game>) oGameRepository.findAll();
	  }
	  model.addAttribute("gamesQueried", oQueryResult);
	  return "games";
	}
	
  @RequestMapping("/tag")
  public String findOneTag(@RequestParam(value="tagId") long lTagId, Model model) throws TagNotFoundException
  {
	  Optional<Tag> tag = oTagRepository.findById(lTagId);
	  if(tag.isPresent()){
	  model.addAttribute("tagQueried", tag.get());
	  return "tag";
	} else {
	  throw new TagNotFoundException();
	}
  }

  @RequestMapping("/tags")
  public String findAllTags(Model model)
  {
	Collection<Tag> oQueryResult = (Collection<Tag>) oTagRepository.findAll();
	model.addAttribute("tagsQueried", oQueryResult);
	return "tags";
  }

  @RequestMapping("/search-by-tag-name")
  public String findGamesByTag(Tag tagSought, Model model)
  {
	Collection<Game> oQueryResult = (Collection<Game>)oGameRepository.findByTagsContains(tagSought);
	model.addAttribute("gamesQueried", oQueryResult);
	return "games";
  }

  @RequestMapping("/search-by-game")
  public String findTagsByGame(Game gameSought, Model model)
  {
	Collection<Tag> oQueryResult = (Collection<Tag>)oTagRepository.findByGamesContains(gameSought);
	model.addAttribute("tagsQueried", oQueryResult);
	return "tags";
  }

  @RequestMapping("/get-game-reviews")
  public String findReviewsByGame(Game gameParent, Model model)
  {
	Collection<Review> oQueryResult = oReviewRepository.findByGame(gameParent);
	model.addAttribute("reviewsQueried", oQueryResult);
	return "reviews";
  }

  @RequestMapping("add-game")
  public String addGame(String name, String descriptionShort, String gameUrl, String tags)
  {
	String [] tagsExploded = tags.replace("  ", " ").split("[ ]+");
	System.out.println("ReviewsiteController addGame. provided tags: " + tags);
//	Tag[] tagsAll = new Tag[0];
	Collection<Tag> oTagCollection = new ArrayList();
	System.out.println("ReviewsiteController addGame. After splitting to array: " + tagsExploded.toString());
	for(int i = 0; i<tagsExploded.length; i++) {
	  Optional<Tag> oTagMaybe = oTagRepository.findByName(tagsExploded[i]);
	  if(oTagMaybe.isPresent() && oTagMaybe.get()!=null) {
		System.out.println("ReviewsiteController addGame. A valid Tag has been found by name. It is: " + oTagMaybe.get().toString());
		oTagCollection.add((Tag)oTagMaybe.get()); 
	  }
	}
	Optional<Game> oGameMaybe = oGameRepository.findByName(name);
	if(!oGameMaybe.isPresent()){
	  oGameRepository.save(new Game(name, descriptionShort, gameUrl, (Tag[])oTagCollection.toArray()));
	}
    return "redirect:/games";
  }
}
