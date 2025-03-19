package psu.signlinguaasl.apiservice.users;

import psu.signlinguaasl.localservice.models.Tutor;

public class TutorDetailsApiResponse
{
    @Override
    public String toString() {
        return "Data{" +
                "data=" + data +
                '}';
    }

    public String message;
    public Tutor data;
}
