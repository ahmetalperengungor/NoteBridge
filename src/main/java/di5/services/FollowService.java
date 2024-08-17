package di5.services;

import di5.data.dao.FollowDao;

import java.sql.SQLException;
import java.util.List;

public class FollowService {
    private final FollowDao followDao;

    public FollowService(FollowDao fallowDao) throws ClassNotFoundException, SQLException {
        this.followDao = fallowDao;
    }

    public void followUser(String followerId, String followedId) throws SQLException {
        followDao.followUser(followerId, followedId);
    }

    public void unfollowUser(String followerId, String followedId) throws SQLException {
        followDao.unfollowUser(followerId, followedId);
    }

    public List<String> getFollowers(String userId) throws SQLException {
        return followDao.getFollowers(userId);
    }

    public List<String> getFollowing(String userId) throws SQLException {
        return followDao.getFollowing(userId);
    }
}
