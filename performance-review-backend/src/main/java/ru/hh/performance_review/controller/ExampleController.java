package ru.hh.performance_review.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Singleton
@Path("/")
public class ExampleController {

  private static final Logger logger = LoggerFactory.getLogger(ExampleController.class);

  @GET
  public void dummy() {
    logger.info("Do nothing");
  }
}
