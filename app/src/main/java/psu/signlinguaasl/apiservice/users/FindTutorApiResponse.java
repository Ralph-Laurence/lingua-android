package psu.signlinguaasl.apiservice.users;

import java.util.List;

import psu.signlinguaasl.localservice.models.Tutor;

public class FindTutorApiResponse
{
    public Data data;

    // ONLY USED DURING DEVELOPMENT
    @Override
    public String toString() {
        return "Data{" +
                "data=" + data +
                '}';
    }

    public static class Data {
        public int current_page;
        public List<Tutor> data;
    }
}