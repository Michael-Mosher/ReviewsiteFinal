package com.wecancodeit.reviewssite;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class ReviewControllerTest {
  @InjectMocks
  private ReviewsiteController oControllerTested;
  @Mock
  private Game oGameMockOne;
  @Mock
  private Game oGameMockTwo;
  @Mock
  private Review oReviewMockOne;
  @Mock
  private Tag oTagMockOne;
  @Mock
  private Tag oTagMockTwo;
  @Mock
  private Review oReviewMockTwo;
  @Mock
  private IGameRepository gameRepo;
  @Mock
  private ITagRepository tagRepo;
  @Mock
  private IReviewRepository reviewRepo;
  @Mock
  private Model oMockModel;
  
  @Before
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);
  }
  
  @Test
  public void shouldAddSingleGameToModel() throws GameNotFoundException
  {
	  long lGameId = 1;
	  when(gameRepo.findById(lGameId)).thenReturn(Optional.of(oGameMockOne));
  	oControllerTested.findOneGame(lGameId, oMockModel);
	  verify(oMockModel).addAttribute("gameQueried", oGameMockOne);
  }
  
  @Test
  public void shouldAddAllGamesToModel()
  {
    Collection<Game> oExpectedCollection = Arrays.asList(new Game[] { oGameMockOne, oGameMockTwo});
    // Set handling of mock call of .findAll()
	  when(gameRepo.findByDeletedOrderByNameAsc(false)).thenReturn(oExpectedCollection);
	  oControllerTested.findAllGames(oMockModel/*,"", 0*/);
	  verify(oMockModel).addAttribute("gamesQueried", oExpectedCollection);
  }
  
  @Test
  public void shouldAddSingleTagToModel() throws TagNotFoundException
  {
	  long lTagId = 1;
	  when(tagRepo.findById(lTagId)).thenReturn(Optional.of(oTagMockOne));
	  oControllerTested.findOneTag(lTagId, oMockModel);
	  verify(oMockModel).addAttribute("tagQueried", oTagMockOne);
  }
  
  @Test
  public void shouldAddAllTagsToModel()
  {
	  Collection<Tag> oExpectedCollection = Arrays.asList(new Tag[] { oTagMockOne, oTagMockTwo});
	  when(tagRepo.findAll()).thenReturn(oExpectedCollection);
	  oControllerTested.findAllTags(oMockModel);
	  verify(oMockModel).addAttribute("tagsQueried", oExpectedCollection);
  }
  
  @Test
  public void shouldAddGamesByTag()
  {
	Collection<Game> oExpectedCollection = Arrays.asList(oGameMockOne);
	when(gameRepo.findByTagsContainsAndDeleted(oTagMockOne, false)).thenReturn(oExpectedCollection);
	oControllerTested.findGamesByTag(oTagMockOne, oMockModel);
	verify(oMockModel).addAttribute("gamesQueried", oExpectedCollection);
  }
  
  @Test
  public void shouldAddTagsByGame()
  {
    Collection<Tag> oExpectedCollection = Arrays.asList(oTagMockOne);
    when(tagRepo.findByGamesContains(oGameMockOne)).thenReturn(oExpectedCollection);
    oControllerTested.findTagsByGame(oGameMockOne, oMockModel);
    verify(oMockModel).addAttribute("tagsQueried", oExpectedCollection);
  }
  
  @Test
  public void shouldRemoveGameByName()
  {
    String gameName = "We Happy Few";
    when(oGameMockOne.getGameName()).thenReturn(gameName);
	  Optional<Game> oExpectedReturn = Optional.ofNullable(oGameMockOne);
	  when(gameRepo.findByName(gameName)).thenReturn(oExpectedReturn);
	  oControllerTested.removeGame(gameName, oMockModel);
    verify(oGameMockOne).delete();
  }
  
  @Test
  public void shouldRemoveGameById()
  {
    long gameId = oGameMockOne.getId();
    when(oGameMockOne.getId()).thenReturn(gameId);
    when(gameRepo.findById(gameId)).thenReturn(Optional.ofNullable(oGameMockOne));
    oControllerTested.removeGameById(gameId, oMockModel);
    verify(oGameMockOne).delete();
  }
  
}
