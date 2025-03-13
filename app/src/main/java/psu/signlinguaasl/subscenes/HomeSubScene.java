package psu.signlinguaasl.subscenes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Optional;

import psu.signlinguaasl.R;
import psu.signlinguaasl.localservice.auth.AuthenticatedSession;
import psu.signlinguaasl.localservice.models.Tutor;
import psu.signlinguaasl.scene.FindTutorsActivity;
import psu.signlinguaasl.ui.viewadapters.TutorAdapter;

public class HomeSubScene extends Fragment
{
    // private TextView welcomeText;
    private Button btnFindTutors;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View inflatedView = inflater.inflate(R.layout.fragment_home_sub_scene, container, false);
        // welcomeText = inflatedView.findViewById(R.id.home_subscene_welcome_text);

        // AuthenticatedSession session = AuthenticatedSession.getInstance(inflater.getContext());
        // String msg = String.format("Welcome home, %s ... Glad to be back?", session.getUser().getFirstname());
        // String msg = "Welcome home, carl ... glad to be back?";
        //welcomeText.setText(msg);

        btnFindTutors = inflatedView.findViewById(R.id.home_subscene_btn_find_tutors);

        btnFindTutors.setOnClickListener(v -> {
            Intent intent = new Intent(inflater.getContext(), FindTutorsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            inflater.getContext().startActivity(intent);
        });

        return inflatedView;
    }
}