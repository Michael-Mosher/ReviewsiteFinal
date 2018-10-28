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
		oQueryResult = (Collection<Game>) oGameRepository.findAllByOrderByNameAsc();
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
//	String [] tagsExploded = tags.replace("  ", " ").split("[ ]+");
//	Tag[] tagsAll = new Tag[0];
//	for(int i = 0; i<tagsExploded.length; i++) {
	Optional<Tag> oTagMaybe = oTagRepository.findByName(tags);
//	  if(oTagMaybe.isPresent() && oTagMaybe.get()!=null) { 
//	  }
//	}
	Optional<Game> oGameMaybe = oGameRepository.findByName(name);
	if(!oGameMaybe.isPresent()){
//	  if(oTagMaybe.isPresent()) oGameRepository.save(new Game(name, descriptionShort, gameUrl, oTagMaybe.get()));
	 /* else*/ oGameRepository.save(new Game(name, descriptionShort, gameUrl, oTagMaybe.get()));
	}
    return "redirect:/games";
  }

  @RequestMapping("remove-game")
  public String removeGame(String gameName)
  {
    Optional<Game> oGameCheck = oGameRepository.findByName(gameName);
    if(oGameCheck.isPresent()) {
      System.out.println("Controller.removeGame. The game is present. We found: " + oGameCheck.get());
      oGameRepository.delete(oGameCheck.get());
    }
    return "redirect:/games";
  }

  @RequestMapping("remove-game-id")
  public String removeGameById(long gameId)
  {
    oGameRepository.deleteById(new Long(gameId));
	return "redirect:/games";
  }
  
  @RequestMapping("find-game-by-tag-name")
  public String queryGamesByTopicName(String tagName, Model model)
  {
    Optional<Tag> oTagSought = oTagRepository.findByName(tagName);
//    Collection<Game> oQueryResult = oGameRepository.findByTagsContainsByOrderByNameAsc(oTagSought.get());
    Collection<Game> oQueryResult = oGameRepository.findByTagsOrderByNameAsc(oTagSought.get());
    model.addAttribute("gamesQueried", oQueryResult);
	return "/games";
  }
}
