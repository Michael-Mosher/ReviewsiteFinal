package com.wecancodeit.reviewssite;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

public interface IReviewRepository extends CrudRepository<Review, Long>
{
  Collection<Review> findByGame(Game game);
}
