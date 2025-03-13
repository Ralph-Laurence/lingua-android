package psu.signlinguaasl.apiservice.auth;

import java.util.List;

import psu.signlinguaasl.localservice.models.Tutor;

public class TutorResponse
{
    public Data data;

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