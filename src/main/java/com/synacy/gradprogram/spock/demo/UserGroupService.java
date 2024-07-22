package com.synacy.gradprogram.spock.demo;

public class UserGroupService {

  private final UserGroupRepository userGroupRepository;
  private final DateService dateService;

  public UserGroupService(UserGroupRepository userGroupRepository, DateService dateService) {
    this.userGroupRepository = userGroupRepository;
    this.dateService = dateService;
  }

  // TODO: Add method with the parameters id, userGroupName, totalUserInGroup, and date.
  //  Method should save the created UserGroup to the database. Then return the created UserGroup.

  public void userAddedToGroup(UserGroup userGroup) {
    int newUserTotal = userGroup.getTotalUsersInGroup() + 1;
    userGroup.setTotalUsersInGroup(newUserTotal);
    userGroup.setLastUserAddedDate(dateService.getDateNow());

    userGroupRepository.updateUserGroup(userGroup);
  }

  // TODO: Add method with the parameter UserGroup. The method should remove 1 to the UserGroup's total users.
  //  The method should throw an Exception if the UserGroup has no users (0 total users).
}
