package com.wecancodeit.reviewssite;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ITagRepository extends CrudRepository<Tag, Long>
{
  Collection<Tag> findByGamesContains(Game game);
  Optional<Tag> findByName(String string);
}
