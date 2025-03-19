package psu.signlinguaasl.subscenes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import psu.signlinguaasl.R;
import psu.signlinguaasl.localservice.interfaces.OnFragmentBackPressedListener;
import psu.signlinguaasl.scene.FindTutorsActivity;
import psu.signlinguaasl.scene.MyTutorsActivity;
import psu.signlinguaasl.ui.custom.Modal;

public class HomeSubScene extends Fragment implements OnFragmentBackPressedListener
{
    private Modal modal;
    private Context m_context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        m_context = inflater.getContext();

        View inflatedView = inflater.inflate(R.layout.fragment_home_sub_scene, container, false);
        // welcomeText = inflatedView.findViewById(R.id.home_subscene_welcome_text);

        // AuthenticatedSession session = AuthenticatedSession.getInstance(inflater.getContext());
        // String msg = String.format("Welcome home, %s ... Glad to be back?", session.getUser().getFirstname());
        //welcomeText.setText(msg);

        Button btnFindTutors = inflatedView.findViewById(R.id.home_subscene_btn_find_tutors);
        Button btnMyTutors = inflatedView.findViewById(R.id.home_subscene_btn_my_tutors);

        btnFindTutors.setOnClickListener(v -> launchScene(FindTutorsActivity.class));
        btnMyTutors.setOnClickListener(v -> launchScene(MyTutorsActivity.class));

        modal = new Modal(inflater.getContext());

        return inflatedView;
    }

    @Override
    public void onBackPressed()
    {
        modal.ShowConfirm("Do you wish to exit the app?", "Confirm Exit", () -> {
            Activity activity =  getActivity();

            if (activity != null)
                activity.finishAndRemoveTask();

            System.exit(0);
        });
    }

    private void launchScene(Class<?> scene)
    {
        Intent intent = new Intent(m_context, scene);
        // permanently close the last activity to prevent going back
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // inflater.getContext().startActivity(intent);
        m_context.startActivity(intent);
    }
}