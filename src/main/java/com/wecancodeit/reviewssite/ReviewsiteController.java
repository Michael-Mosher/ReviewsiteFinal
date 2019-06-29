package com.wecancodeit.reviewssite;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
  
  @RequestMapping("/game")
	public String findOneGame(@RequestParam(value="gameId") long lGameId, Model model) throws GameNotFoundException
	{
	  Optional<Game> game = oGameRepository.findById(lGameId);
	  if(game.isPresent()){
		model.addAttribute("gameQueried", game.get());
  		Collection<Tag> oAssociatedTags = oTagRepository.findByGamesContains(game.get());
  		model.addAttribute("associatedTags", oAssociatedTags);
		return "game";
	  } else {
		  throw new GameNotFoundException();
	  }
	}

  @RequestMapping("/games")
  public String findAllGames(Model model)
  {
    Collection<Game> oQueryResult;
	oQueryResult = (Collection<Game>) oGameRepository.findByDeletedOrderByNameAsc(false);
	for (Game var : oQueryResult) {
		System.out.println("games findAllGames result of findByDeletedOrderByNameAsc"
		    + var);
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
	Collection<Game> oQueryResult = (Collection<Game>)oGameRepository.findByTagsContainsAndDeleted(tagSought, false);
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
  public String addGame(String name, String descriptionShort, String gameUrl,
      String tags, Model model)
  {
    String [] tagsExploded = tags.replace("  ", " ").split("[ ]+");
	Tag[] tagsAll = new Tag[0];
	Tag[] newTagArray = new Tag[0];
	System.out.println(tagsExploded);
	int i;
	Optional<Tag> oTagMaybe = Optional.empty();
  	for(i = 0; i < tagsExploded.length; i++) {
	  oTagMaybe = oTagRepository.findByName(tagsExploded[i]);
  	  if(oTagMaybe.isPresent() && oTagMaybe.get()!=null){
		newTagArray = new Tag[] {oTagMaybe.get()};
  	  } else if(tagsExploded[i].length() > 2){
		newTagArray = new Tag[] {oTagRepository.save(new Tag(tagsExploded[i])) };
	  }
	  tagsAll = Utilities.concatenateTagArrays(tagsAll, newTagArray);
	}
	System.out.println(tagsAll);
	Optional<Game> oGameMaybe = oGameRepository.findByName(name);
	if(!oGameMaybe.isPresent()){
	  oGameRepository.save(new Game(name, descriptionShort, gameUrl, tagsAll));
	}
    return "redirect:/games";
  }

  @RequestMapping("remove-game")
  public String removeGame(@RequestParam(value="gameName") String gameName, Model model)
  {
	Optional<Game> oGameCheck = oGameRepository.findByName(gameName);
	System.out.println("removeGame Was the game, " + gameName + ", found: " + oGameCheck.isPresent());
    if(oGameCheck.isPresent()) {
	  System.out.println("Controller.removeGame. The game is present. We found: " + oGameCheck.get());
	  oGameCheck.get().delete();
	  oGameRepository.save(oGameCheck.get());
	  System.out.println("removeGame after Game.delete. The game is deleted: " + oGameCheck.get().isDeleted());
    }
    return "redirect:/games";
  }

  @RequestMapping("remove-game-id")
  public String removeGameById(long gameId, Model model)
  {
	Optional<Game> oGameCheck = oGameRepository.findById(gameId);
    if(oGameCheck.isPresent()) {
		int i;
		System.out.println("Controller.removeGame. The game is present. We found: " + oGameCheck.get());
		Collection<Tag> tagsToRemove =  oGameCheck.get().getTags();
		for(i = tagsToRemove.size() -1; i>-1; i--){
		  oGameCheck.get().removeTag((Tag) tagsToRemove.toArray()[i]);
		}
		Collection<Review> reviewsToRemove = oGameCheck.get().getReviews();
		for(i = reviewsToRemove.size() -1; i>-1; i--){
		  oGameCheck.get().removeReview((Review) reviewsToRemove.toArray()[i]);
		}
		oGameCheck.get().delete();
	}
	return "redirect:/games";
  }
  
  @RequestMapping("find-game-by-tag-name")
  public String queryGamesByTopicName(@RequestParam(value="tagName") String tagName, Model model)
  {
	Collection<Game> oQueryResult;
	Tag oTagActual = new Tag(tagName);
	Optional<Tag> oTagSought = oTagRepository.findByName(oTagActual.getTag());
	System.out.println("find-game-by-tag-name tagName: " + tagName + " was found by repository: " + oTagSought.isPresent());
	if(oTagSought.isPresent()){
	  oQueryResult = oGameRepository.findByTagsAndDeletedOrderByNameAsc(oTagSought.get(), false);
	  model.addAttribute("gamesQueried", oQueryResult);
	} else model.addAttribute("gamesQueried", null);
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
	Tag oTagActual = new Tag(tag);
	Optional<Tag> oTagTest = oTagRepository.findByName(oTagActual.getTag());
	if(oTagTest.isPresent()) {
      Collection<Game> oTaggedGames = oGameRepository.findByTagsAndDeletedOrderByNameAsc(oTagTest.get(), false);
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
  
  @RequestMapping("/game/{gameId}/addTag")
  public String addTagAsynchronously(@PathVariable ("gameId") long gameId, @RequestParam List<String> tagName,
      Model model)
  {
	tagName.removeIf(t -> t.trim().length() < 1);
	System.out.println("addTagAsynchronously. The tags provided: " + tagName.get(0));
	if(tagName.isEmpty()){
	  return "";
	} else {
	  Optional<Game> maybeGame = oGameRepository.findById(gameId);
	  if(maybeGame.isPresent()){
		Game theGame = maybeGame.get();
		for( String name : tagName) {
		  Tag oTagToAdd = new Tag(name);
		  Optional<Tag> oMaybeTag = oTagRepository.findByName(oTagToAdd.getTag());
		  if(oMaybeTag.isPresent()){
            oTagToAdd = oMaybeTag.get();
		  } else {
		    oTagToAdd = oTagRepository.save(oMaybeTag.get());
		  }
		  theGame.addTag(oTagToAdd);
		  oTagRepository.save(oTagToAdd);
		}
		oGameRepository.save(theGame);
		return "partials/added-tag-confirmation";
	  }
	  return "";
	}
  }
  
  @RequestMapping("/game/{gameId}/addReview")
  public String addReviewAsynchronously(
	  @PathVariable("gameId") long gameId, @RequestParam String reviewAuthor,
	  @RequestParam String reviewContent,
	  @RequestParam(value="reviewTitle", required=false) String reviewTitle, Model model)
  {
	Optional<Game> oTheGame = oGameRepository.findById(gameId);
	if(oTheGame.isPresent() && !reviewAuthor.isEmpty() && !reviewContent.isEmpty()){
      oReviewRepository.save(new Review(reviewAuthor, reviewContent, oTheGame.get(), reviewTitle));
	}
	return "partials/added-review-confirmation";
  }
}
