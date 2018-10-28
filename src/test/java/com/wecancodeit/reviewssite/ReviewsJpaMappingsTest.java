package com.wecancodeit.reviewssite;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jayway.jsonpath.Option;

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
	Collection<Game> oGamesForFirstTag = gameRepo.findByTagsContains(oFirstTag);
	assertThat(oGamesForFirstTag, containsInAnyOrder(oFirstGame, oSecondGame));
	Collection<Game> oGamesForSecondTag = gameRepo.findByTagsContains(oSecondTag);
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
	Collection<Game> oGamesForFirstTag = gameRepo.findByTagsId(lFirstTag);
	assertThat(oGamesForFirstTag, containsInAnyOrder(oFirstGame, oSecondGame));
	Collection<Game> oGamesForSecondTag = gameRepo.findByTagsId(lSecondTag);
	assertThat(oGamesForSecondTag, containsInAnyOrder(oFirstGame));
  }
  
  @Test
  public void shouldConfirmGameToTagsRelationship()
  {
	Tag oFirstTag = tagRepo.save(new Tag("FPS"));
    Tag oSecondTag = tagRepo.save(new Tag("female protagonist"));
	Game oFirstGame = gameRepo.save(new Game("Game Name", "Game description",  "dis2.jpg", oSecondTag, oFirstTag));
	Game oSecondGame = gameRepo.save(new Game("Another Name", "better description", "skyrim.jpg", oFirstTag));
	long lFirstGameId = oFirstGame.getId();
	entityManager.flush();
	entityManager.clear();
	Optional<Game> oOptionalGame = gameRepo.findById(lFirstGameId);
	oFirstGame = oOptionalGame.get();
	assertThat(oFirstGame.getTags(), containsInAnyOrder(oFirstTag, oSecondTag));
  }
}
