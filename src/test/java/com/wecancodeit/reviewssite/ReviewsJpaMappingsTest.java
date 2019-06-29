package com.wecancodeit.reviewssite;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.contains;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class ReviewsJpaMappingsTest {
  @Resource
  private TestEntityManager entityManager;
  @Resource
  private IGameRepository gameRepo;
  @Resource
  private IReviewRepository reviewRepo;
  @Resource
  private ITagRepository tagRepo;

  
  @Test
  public void shouldSaveGame()
  {
	Game oGameTested = gameRepo.save(new Game());
	assertTrue(oGameTested instanceof Game);
  }
  
  @Test
  public void shouldSaveAndLoadGame()
  {
	String name = "Game Name";
	String description = "Brief Description";
	String url = "skyrim.jpg";
	Game oGameTested = gameRepo.save(new Game(name, description, url));
	long lGameId = oGameTested.getId();
	entityManager.flush();
	entityManager.clear();
	Optional<Game> oFoundResult = gameRepo.findById(lGameId);
	oGameTested = oFoundResult.get();
	assertThat(name, is(oGameTested.getGameName()));
	assertThat(description, is(oGameTested.getDescriptionShort()));
	assertThat(url, is(oGameTested.getImageUrl()));
  }
  
  @Test
  public void shouldEstablishGameToReviewRelationship()
  {
	// One to Many
	Game oGameRelation = gameRepo.save(new Game("Game Name", "Game Short Description", "skyrim.jpg"));
	long lExpectedGameId = oGameRelation.getId();
	Review oFirstReview = reviewRepo.save(new Review("Review Author", "Indulgent Review", oGameRelation, "Review Title"));
	Review oSecondReview = reviewRepo.save(new Review("Questionable Author", "Brusk Review", oGameRelation, "Review Title"));
	entityManager.flush();
	entityManager.clear();
	Optional<Game> oOptionalGame = gameRepo.findById(lExpectedGameId);
	oGameRelation = oOptionalGame.get();
	assertThat(oGameRelation.getReviews(), containsInAnyOrder(oFirstReview, oSecondReview));
  }
  
  @Test
  public void shouldSaveTag()
  {
	Tag oTagTested = tagRepo.save(new Tag("female protagonist"));
	assertTrue(oTagTested instanceof Tag);
  }
  
  @Test
  public void shouldFindGameByTag()
  {
	Tag oFirstTag = tagRepo.save(new Tag("FPS"));
	Tag oSecondTag = tagRepo.save(new Tag("female protagonist"));
	Game oFirstGame = gameRepo.save(new Game("Game Name", "Game description", "dis2.jpg", oSecondTag, oFirstTag));
	Game oSecondGame = gameRepo.save(new Game("Another Name", "better description", "skyrim.jpg", oFirstTag));
	entityManager.flush();
	entityManager.clear();
	Collection<Game> oGamesForFirstTag = gameRepo.findByTagsContainsAndDeleted(oFirstTag, false);
	assertThat(oGamesForFirstTag, containsInAnyOrder(oFirstGame, oSecondGame));
	Collection<Game> oGamesForSecondTag = gameRepo.findByTagsContainsAndDeleted(oSecondTag, false);
	assertThat(oGamesForSecondTag, containsInAnyOrder(oFirstGame));
  }
  
  @Test
  public void shouldFindGameByTagId()
  {
	Tag oFirstTag = tagRepo.save(new Tag("FPS"));
	long lFirstTag = oFirstTag.getId();
	Tag oSecondTag = tagRepo.save(new Tag("female protagonist"));
	long lSecondTag = oSecondTag.getId();
	Game oFirstGame = gameRepo.save(new Game("Game Name", "Game description",  "dis2.jpg", oSecondTag, oFirstTag));
	Game oSecondGame = gameRepo.save(new Game("Another Name", "better description", "skyrim.jpg", oFirstTag));
	entityManager.flush();
	entityManager.clear();
	Collection<Game> oGamesForFirstTag = gameRepo.findByTagsIdAndDeleted(lFirstTag, false);
	assertThat(oGamesForFirstTag, containsInAnyOrder(oFirstGame, oSecondGame));
	Collection<Game> oGamesForSecondTag = gameRepo.findByTagsIdAndDeleted(lSecondTag, false);
	assertThat(oGamesForSecondTag, containsInAnyOrder(oFirstGame));
  }
  
  @Test
  public void shouldConfirmGameToTagsRelationship()
  {
	Tag oFirstTag = tagRepo.save(new Tag("FPS"));
    Tag oSecondTag = tagRepo.save(new Tag("female protagonist"));
	Game oFirstGame = gameRepo.save(new Game("Game Name", "Game description",  "dis2.jpg", oSecondTag, oFirstTag));
	long lFirstGameId = oFirstGame.getId();
	entityManager.flush();
	entityManager.clear();
	Optional<Game> oOptionalGame = gameRepo.findById(lFirstGameId);
	oFirstGame = oOptionalGame.get();
	assertThat(oFirstGame.getTags(), containsInAnyOrder(oFirstTag, oSecondTag));
  }

  @Test
  public void confirmTagRemovedFromGame()
  {
	Tag oFirstTag = tagRepo.save(new Tag("FPS"));
    Tag oSecondTag = tagRepo.save(new Tag("female protagonist"));
	Game oFirstGame = gameRepo.save(new Game("Game Name", "Game description",  "dis2.jpg", oSecondTag, oFirstTag));
	Game oSecondGame = gameRepo.save(new Game("Another Name", "better description", "skyrim.jpg", oFirstTag));
	long lFirstGameId = oFirstGame.getId();
	oFirstGame.removeTag(oFirstTag);
	entityManager.flush();
	entityManager.clear();
	Optional<Game> oOptionalGame = gameRepo.findById(lFirstGameId);
	oFirstGame = oOptionalGame.get();
	assertThat(oFirstGame.getTags(), containsInAnyOrder(oSecondTag));
	assertThat(oSecondGame.getTags(), containsInAnyOrder(oFirstTag));
	assertThat(tagRepo.findByGamesContains(oSecondGame), containsInAnyOrder(oFirstTag));
  }
  
  @Test
  public void shouldReturnGamesSortedNameAscending()
  {
	Game oGameSkyrim = gameRepo.save(
			new Game(
					"Skyrim: Dragons Punked",
					"Some times the bear eats you, but sometimes you eat the bear",
					"skyrim.jpg",
					tagRepo.save(new Tag("FPS"))
			)
	);
	Game oGameDishonored2 = gameRepo.save(
			new Game(
					"Dishonored, Again",
					"When someone takes your toys you kill her, and everyone who has ever helped her",
					"dis2.jpg",
					tagRepo.save(new Tag("female protagonist"))
			)
	);
	entityManager.flush();
	entityManager.clear();
	Collection<Game> oActualResult = gameRepo.findByDeletedOrderByNameAsc(false);
	assertThat(oActualResult, contains(oGameDishonored2, oGameSkyrim));
  }
}
