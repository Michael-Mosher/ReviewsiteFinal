package com.wecancodeit.reviewssite;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

interface IGameRepository extends CrudRepository<Game, Long>
{
  Collection<Game> findByTagsContainsAndDeleted(Tag tag, boolean b);
  Collection<Game> findByTagsIdAndDeleted(long tagId, boolean b);
  Optional<Game> findByName(String name);
  Collection<Game> findAllByOrderByNameAsc();
  Collection<Game> findByTagsAndDeletedOrderByNameAsc(Tag tag, boolean b);
  Collection<Long> countByTags(Collection<Tag> tags);
  Collection<Game> findByDeletedOrderByNameAsc(boolean b);
}
