package com.synacy.gradprogram.spock.demo;

// NOTE: This is a mock class used for demonstration purposes only
public class UserRepository {

  public void saveUser(User user) {
    // This method saves the given User to the database
  }

  public User fetchUserById(Long id) {
    // This method finds and returns the corresponding User for the given id from the database
    return new User();
  }
}
