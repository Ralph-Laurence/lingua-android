package psu.signlinguaasl.localservice.models;

import psu.signlinguaasl.localservice.utils.Str;

public class Tutor
{
    String tutorId;
    String name;
    String photo;
    String bio;
    int totalLearners;
    int ratings;

    public String getTutorId() {
        return tutorId;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getBio() {
        return bio;
    }

    public int getTotalLearners() {
        return totalLearners;
    }

    public int getRatings() {
        return ratings;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setTotalLearners(int learners) {
        this.totalLearners = learners;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}