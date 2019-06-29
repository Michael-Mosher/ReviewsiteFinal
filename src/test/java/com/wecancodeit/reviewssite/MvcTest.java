package com.wecancodeit.reviewssite;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import java.util.*;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ReviewsiteController.class)
public class MvcTest {
	@Resource
	private MockMvc mvc;
	@MockBean
	private IGameRepository gameRepo;
	@MockBean
	private ITagRepository tagRepo;
	@MockBean
	private IReviewRepository reviewRepo;
	@Mock
	private Game oMockGame;
	@Mock
	private Game oAnotherMockGame;
  @Mock
  private Tag oMockTag;
  @Mock
  private Tag oAnotherMockTag;
  @Mock
  private Review oMockReview;
  @Mock
  private Review oAnotherMockReview;
  
  @Test
  public void shouldBeOkForAllGames() throws Exception
  {
	Collection<Game> oExpectedCollection = Arrays.asList(new Game[] {oMockGame, oAnotherMockGame});
	when(gameRepo.findAll()).thenReturn(oExpectedCollection);
	mvc.perform(get("/games?tagName=&tagId=")).andExpect(status().isOk());
  }
  
  @Test
  public void assertGamesNoTagStatusOk() throws Exception
  {
    mvc.perform(get("/games")).andExpect(status().isOk());
  }
  
  @Test
  public void shouldRouteToAllGameView() throws Exception
  {
    Collection<Game> oExpectedCollection = Arrays.asList(new Game[] {oMockGame, oAnotherMockGame});
    when(gameRepo.findAll()).thenReturn(oExpectedCollection);
    mvc.perform(get("/games")).andExpect(view().name(is("games")));
  }
  
  @Test
  public void shouldBeOkForGame() throws Exception
  {
	when(gameRepo.findById(1L)).thenReturn(Optional.of(oMockGame));
	mvc.perform(get("/game?gameId=1")).andExpect(status().isOk());
  }
  
  @Test
  public void assertRouteToGameView() throws Exception
  {
	  when(gameRepo.findById(1L)).thenReturn(Optional.of( oMockGame));
    mvc.perform(get("/game?gameId=1")).andExpect(view().name(is("game")));
  }
  
  @Test(expected = java.lang.AssertionError.class)
  public void shouldBeAssertionErrorForGameNoId() throws Exception
  {
    mvc.perform(get("/game?gameId=")).andExpect(status().isNotFound());	
  }
  
  @Test
  public void assertSingleGameAddedToModel() throws Exception
  {
    long lGameId = 1;
    when(gameRepo.findById(new Long(lGameId))).thenReturn(Optional.of(oMockGame));
    mvc.perform(get("/game?gameId=1")).andExpect(model().attribute("gameQueried", is(oMockGame)));
  }
  
  @Test
  public void shouldBeOkForAllTags() throws Exception
  {
	mvc.perform(get("/tags")).andExpect(status().isOk());
  }
  
  @Test
  public void assertRouteToTagsView() throws Exception
  {
    mvc.perform(get("/tags")).andExpect(view().name(is("tags")));
  }
  
  @Test
  public void shouldRouteToAllTagsView() throws Exception
  {
    Collection<Tag> oExpectedCollection = Arrays.asList(new Tag[] {oMockTag, oAnotherMockTag});
    when(tagRepo.findAll()).thenReturn(oExpectedCollection);
    mvc.perform(get("/tags")).andExpect(view().name(is("tags")));
  }
  
  @Test
  public void shouldBeOkForTag() throws Exception
  {
	when(tagRepo.findById(1L)).thenReturn(Optional.of(oMockTag));
	mvc.perform(get("/tag?tagId=1")).andExpect(status().isOk());
  }
  
  @Test
  public void assertRouteToTagView() throws Exception
  {
	when(tagRepo.findById(1L)).thenReturn(Optional.of(oMockTag));
    mvc.perform(get("/tag?tagId=1")).andExpect(view().name(is("tag")));
  }
  
  @Test(expected = org.springframework.web.util.NestedServletException.class) // TagNotFoundException.class)
  public void shouldNotBeOkForSingleTag() throws Exception
  {
    mvc.perform(get("/tag?tagId=12345")).andExpect(status().isNotFound());	
  }
  
  @Test
  public void assertSingleTagAddedToModel() throws Exception
  {
    long lTagId = 1;
    when(tagRepo.findById(new Long(lTagId))).thenReturn(Optional.of(oMockTag));
    mvc.perform(get("/tag?tagId=1")).andExpect(model().attribute("tagQueried", is(oMockTag)));
  }

  @Test
  public void assertFindGameByTagNameReturnsGames() throws Exception
  {
    String testTagName = "Female_Protagonist";
    when(oMockTag.getTag()).thenReturn(testTagName);
    when(tagRepo.findByName(testTagName)).thenReturn(Optional.ofNullable(oMockTag));
    Collection<Game> collectionExpected = Arrays.asList(new Game[] { oMockGame, oAnotherMockGame });
    when(gameRepo.findByTagsAndDeletedOrderByNameAsc(oMockTag, false)).thenReturn(collectionExpected);
    mvc.perform(get("/find-game-by-tag-name?tagName=" + testTagName)).andExpect(
        model().attribute("gamesQueried", is(collectionExpected))
    );
    mvc.perform(get("/find-game-by-tag-name?tagName=" + testTagName)).andExpect(view().name(is("/games")));
  }

  @Test
  public void assertRemoveGameWillDelete() throws Exception
  {
    String gameNameToRemove = "We Happy Few";
    String tagNameAssociated = "FPS";
    when(gameRepo.findByName(gameNameToRemove)).thenReturn(Optional.of(oMockGame));
    when(tagRepo.findByName(tagNameAssociated)).thenReturn(Optional.of(oMockTag));
    Collection<Game> originalGameCollection = Arrays.asList(new Game[] { oMockGame, oAnotherMockGame });
    when(gameRepo.findByTagsAndDeletedOrderByNameAsc(oMockTag, false)).thenReturn(originalGameCollection);
    mvc.perform(get("/find-game-by-tag-name?tagName=FPS")).andExpect(model().attribute(
        "gamesQueried",
        is(originalGameCollection))
    );
    Collection<Tag> oTagsToRemove = Arrays.asList(new Tag[] { oMockTag, oAnotherMockTag });
    when(tagRepo.findByGamesContains(oMockGame)).thenReturn(oTagsToRemove);
    when(gameRepo.findByTagsContainsAndDeleted(oMockTag, false)).thenReturn(Arrays.asList(new Game[] { oMockGame, oAnotherMockGame }));
    mvc.perform(get("/remove-game?gameName="+gameNameToRemove)).andExpect(view().name(is("redirect:/games")));
    // gameRepo.delete(oMockGame);
    when(gameRepo.findByName(gameNameToRemove)).thenReturn(Optional.empty());
    when(gameRepo.findByTagsAndDeletedOrderByNameAsc(oMockTag, false)).thenReturn(Arrays.asList(new Game[] { oAnotherMockGame }));
    mvc.perform(get("/find-game-by-tag-name?tagName=FPS")).andExpect(model().attribute(
        "gamesQueried",
        is(Arrays.asList(new Game[] { oAnotherMockGame })))
    );
  }
    
  /*  "remove-game")
  public String removeGame(String gameName)
  {
    Optional<Game> oGameCheck = oGameRepository.findByName(gameName);
    if(oGameCheck.isPresent()) {
	  System.out.println("Controller.removeGame. The game is present. We found: " + oGameCheck.get());
	  Collection<Tag> tagsToRemove =  oGameCheck.get().getTags();
	  for (Tag someTag : tagsToRemove) {
		  oGameCheck.get().removeTag(someTag);
	  }
	  oGameRepository.delete(oGameCheck.get());
    }
    return "redirect:/games";
  }*/
  
}
