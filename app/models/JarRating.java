package models;

import play.db.jpa.Model;

import javax.persistence.Entity;

@Entity
public class JarRating extends Model {
  public String jarName;
  public Double bullshitIndex;
  public String longestClassName;

  public JarRating(String jarName, Double bullshitIndex, String longestClassName) {
    this.jarName = jarName;
    this.bullshitIndex = bullshitIndex;
    this.longestClassName = longestClassName;
  }
}
