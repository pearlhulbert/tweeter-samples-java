package edu.byu.cs.tweeter.server.dao.dynamo.domain;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class DynamoUser {
    private String alias;
    private String firstName;
    private String lastName;
    private String securePassword;
    private String imageUrl;
    private String salt;
    private int followingCount;
    private int followerCount;

    public DynamoUser() {
    }

    public DynamoUser(String alias, String firstName, String lastName, String securePassword, String imageUrl) {
        this.alias = alias;
        this.firstName = firstName;
        this.lastName = lastName;
        this.securePassword = securePassword;
        this.imageUrl = imageUrl;
    }

    @DynamoDbPartitionKey
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecurePassword() {
        return securePassword;
    }

    public void setSecurePassword(String securePassword) {
        this.securePassword = securePassword;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "alias='" + alias + '\'' +
                ", name='" + firstName + " " + lastName + '\'' +
                ", securepassword=" + securePassword +
                '}';
    }

}
