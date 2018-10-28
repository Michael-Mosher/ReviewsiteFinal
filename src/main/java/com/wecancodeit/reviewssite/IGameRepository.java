package com.wecancodeit.reviewssite;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

interface IGameRepository extends CrudRepository<Game, Long>
{
  Collection<Game> findByTagsContains(Tag tag);
  Collection<Game> findByTagsId(long lTagId);
  Optional<Game> findByName(String name);
}
