package controllers;

import bullshitrater.BullshitIndex;
import bullshitrater.JarInfo;
import models.JarRating;
import play.mvc.Controller;

import java.io.File;
import java.util.List;

public class Application extends Controller {
  public static void index() {
    List<JarRating> ratings = JarRating.all().<JarRating>fetch();

    render(ratings);
  }

  public static void rate(File jar) {
    JarInfo jarInfo = BullshitIndex.ofJar(jar);

    new JarRating(jar.getName(), jarInfo.bullshitIndex, jarInfo.longestClassName).save();

    redirect("Application.index");
  }

}