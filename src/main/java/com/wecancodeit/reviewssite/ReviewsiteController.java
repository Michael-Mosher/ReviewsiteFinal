package com.wecancodeit.reviewssite;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javax.annotation.Resource;

import org.assertj.core.util.Arrays;
import org.omg.IOP.TAG_RMI_CUSTOM_MAX_STREAM_FORMAT;
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
  
  @RequestMapping("/review")
  public String findReviewById(@RequestParam(value="reviewId") long reviewId, Model model)
  {
	Optional<Review> oReviewSought = oReviewRepository.findById(reviewId);
	if(oReviewSought.isPresent()) model.addAttribute("reviewQueried", oReviewSought.get());
	return "review";
  }

  @RequestMapping("/search-by-game")
  public String findTagsByGame(Game gameSought, Model model)
  {
	Collection<Tag> oQueryResult = (Collection<Tag>)oTagRepository.findByGamesContains(gameSought);
	model.addAttribute("tagsQueried", oQueryResult);
	return "tags";
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
  public String queryGamesByTopicName(String tag, Model model)
  {
    Optional<Tag> oTagSought = oTagRepository.findByName(tag);
//    Collection<Game> oQueryResult = oGameRepository.findByTagsContainsByOrderByNameAsc(oTagSought.get());
    Collection<Game> oQueryResult = oGameRepository.findByTagsOrderByNameAsc(oTagSought.get());
    model.addAttribute("gamesQueried", oQueryResult);
	return "/games";
  }
  
  @RequestMapping("count-tag-use")
  public String queryTagUseCount(Model model)
  {
	Collection<Tag> theTags = (Collection<Tag>) oTagRepository.findAll();
	Collection<Long> oTagCount = oGameRepository.countByTags(theTags);
	model.addAttribute("tagsCount", oTagCount);
	return "/tags";
  }
  
  @RequestMapping("find-reviews-by-tag")
  public String findReviewsByGameTags(String tag, Model model)
  {
	Optional<Tag> oTagTest = oTagRepository.findByName(tag);
	if(oTagTest.isPresent()) {
      Collection<Game> oTaggedGames = oGameRepository.findByTagsOrderByNameAsc(oTagTest.get());
      System.out.println("Controller.findReviewsByGameTags. The tag, " + tag + ", was found. The number of games associated: " + oTaggedGames.size());
      if(!oTaggedGames.isEmpty()){
    	Collection<Review> oReviewsOfTagged = null;
    	for (Game game : oTaggedGames){
    	  if(oReviewsOfTagged==null){
    		oReviewsOfTagged = oReviewRepository.findByGameName(game.getGameName());
    		System.out.println("Controller.findReviewsByGameTags. The first first game has returned: " + oReviewsOfTagged.size());
    	  } else {
	        oReviewsOfTagged.addAll(oReviewRepository.findByGameName(game.getGameName()));
	        System.out.println("Controller.findReviewsByGameTags. The subsequent review query has returned: " + oReviewsOfTagged.size());
    	  }
		}
    	model.addAttribute("reviewsQueried", oReviewsOfTagged);
      }
	}
	return "/reviews";
  }
}
