package com.wecancodeit.reviewssite;
import javax.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
@Component
public class GamePopulator implements CommandLineRunner {
  @Resource
  private IGameRepository gameRepo;
  @Resource
  private IReviewRepository reviewRepo;
  @Resource
  private ITagRepository tagRepo;
  
  @Override
  public void run(String ...args) throws Exception
  {
	
	  Tag tagSurvival = tagRepo.save(new Tag("Survival"));
	  Tag tagStrategy = tagRepo.save(new Tag("Strategy"));
  	Tag tagStealth = tagRepo.save(new Tag("Stealth"));
	  Tag tagAdventure = tagRepo.save(new Tag("Adventure"));
  	Tag tagFemaleProtagonist = tagRepo.save(new Tag("Female_Protagonist"));
	  Tag tagHistorical = tagRepo.save(new Tag("Historical"));
  	Tag tagFPS = tagRepo.save(new Tag("FPS"));
	  Game gameWeHappyFew = gameRepo.save(new Game("We Happy Few", "A game of paranoia and survival, in a drugged-out,\r\n" + 
		  	"dystopian English city in 1964.", "wehappyfew.jpg", tagSurvival, tagStealth, tagFemaleProtagonist, tagFPS));
	  Game gameCKII = gameRepo.save(
		  	new Game(
			      "Crusader Kings II",
  					"Europe is in turmoil. Increase your prestige & listen to the world whisper your name in awe. Do you have what it takes to become a Crusader King?",
	  				"ckii.jpg",
		  			tagStrategy, tagHistorical
			  )
	  );
	  Game gameDishonored2 = gameRepo.save(
		    new Game(
		  	    "Dishonored 2",
			      "Reprise your role as a supernatural assassin in Dishonored 2.",
  			    "dis2.jpg", tagStealth, tagFemaleProtagonist, tagFPS
	      )
	  );
	  Game gameSkyrim = gameRepo.save(
	      new Game(
		  	    "The Elder Scrolls V: Skyrim",
			      "After escaping execution, the last living Dragonborn must grow in strength and power to defeat the dragons that have once again begun to plague the land of Skyrim.",
			      "skyrim.jpg", tagAdventure,tagFPS
		    )
	  );
	  Review review1 = reviewRepo.save(
		    new Review(
				    "ThaZombieGirl",
					  "So i just went in to one of my saves for Sally and it was bugging out i couldn't walk around "
					  + "only turn on the spot, then when i loaded the other sally save up i went in to the garden "
					  + "district where i changed her clothes for the area but still got attacked for no reason? "
				    + "then when i died in the save it loaded me back up as arther, and it was laggy as hell. "
				    + "when i did arthers story it wasnt glichy at all, but i took a few weeks off the game and "
				    + "now it is. please fix! really did enjoy up to this point but cant recomend it as of now due to these issues.",
					  gameWeHappyFew
		    )
	  );
	  Review review2 = reviewRepo.save(
		    new Review(
				    "dario2213714", "cool i guess... didnt play it that much", gameCKII
		    )
	  );
	  Review review3 = reviewRepo.save(
	    new Review(
	        "Waterclock and the Golden Gold",
	    	  "this and dark messiah taught me one oddly specific thing: arkane does the best kicking mechanics i've ever "
	    	  + "seen in any game because your leg is apparently the god hand",
	        gameDishonored2
	    )
	  );
	  Review review4 = reviewRepo.save(new Review("Klappspaten","This game is the love of my life and i'm not ashamed of it.\n*installs bouncy boobs mod*", gameSkyrim));
	  Review review5 = reviewRepo.save(new Review("Krav", "Games you always go back to.\r\n10/10 masterpiece, thanks Todd Howard", gameSkyrim));
	  Review review6 = reviewRepo.save(
		  	new Review(
			  		"Mercy Failing",
				  	"I have this game on PC (duh) and PS4, I have beaten the game a few times on each now and i thoroughly enjoyed it."
					  + " I came into it expecting the first game and was pleasantly surprised that it is everything i loved from the first game, and nothing i hated about it."
					  + " they added a voice to corvo and changed the way the playable characters react to small things based on how you play."
					  + " its a very fun and very good game, just not as long as i would like it to be, that being said however it has a lot of"
					  + " replay value because of the size of the maps and the different endings/play styles. 10/10 would bang again.",
					  gameDishonored2
		    )
	  );
  }
}
