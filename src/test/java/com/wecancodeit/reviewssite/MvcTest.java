package com.wecancodeit.reviewssite;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

import javax.annotation.Resource;

import java.util.*;
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
  
  @Test(expected = GameNotFoundException.class)
  public void assertGamesStatusNotFound() throws Exception
  {
    mvc.perform(get("/games")).andExpect(status().isNotFound());
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
  
  @Test(expected = GameNotFoundException.class)
  public void shouldNotBeOkForSingleGame() throws Exception
  {
    mvc.perform(get("/game?gameId=1")).andExpect(status().isNotFound());	
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
  
  @Test(expected = TagNotFoundException.class)
  public void shouldNotBeOkForSingleTag() throws Exception
  {
    mvc.perform(get("/tag?tagId=1")).andExpect(status().isNotFound());	
  }
  
  @Test
  public void assertSingleTagAddedToModel() throws Exception
  {
    long lTagId = 1;
    when(tagRepo.findById(new Long(lTagId))).thenReturn(Optional.of(oMockTag));
    mvc.perform(get("/tag?tagId=1")).andExpect(model().attribute("tagQueried", is(oMockTag)));
  }
}
