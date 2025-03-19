package psu.signlinguaasl.ui.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import psu.signlinguaasl.R;
import psu.signlinguaasl.localservice.utils.Str;

public class Modal
{
    public static final int MODAL_SUCCESS   = 26;
    public static final int MODAL_WARNING   = 982;
    public static final int MODAL_DANGER    = 949;
    public static final int MODAL_INFO      = 827;
    private static final int MODAL_CONFIRM  = 224;
    private static final int MODAL_LOADING = 823;

    private final Context m_context;

    public Modal(Context context)
    {
        m_context = context;
    }
    //-----------------------------------------------
    //        D I A L O G   O V E R R I D E S
    //-----------------------------------------------

    // INFORMATION

    public void ShowInfo(String message)
    {
        Show(message,"Information", MODAL_INFO, null, null);
    }

    public void ShowInfo(String message, String title)
    {
        Show(message, title, MODAL_INFO, null, null);
    }

    // WARNING

    public void ShowWarn(String message)
    {
        Show(message, "Warning", MODAL_WARNING, null, null);
    }

    public void ShowWarn(String message, String title)
    {
        Show(message, title, MODAL_WARNING, null, null);
    }

    // DANGER

    public void ShowDanger(String message)
    {
        Show(message, "Failure", MODAL_DANGER, null, null);
    }

    public void ShowDanger(String message, String title)
    {
        Show(message, title, MODAL_DANGER, null, null);
    }

    public void ShowDanger(String message, String title, Runnable onOk)
    {
        Show(message, title, MODAL_DANGER, null, onOk);
    }

    // CONFIRMATION

    public void ShowConfirm(String message, String title, Runnable onOk)
    {
        if (Str.IsNullOrEmpty(title))
            title = "Confirmation";

        Show(message, title, MODAL_CONFIRM, null, onOk);
    }

    public void ShowConfirm(String message, String title, Runnable onCancel, Runnable onOk)
    {
        if (Str.IsNullOrEmpty(title))
            title = "Confirmation";

        Show(message, title, MODAL_CONFIRM, onCancel, onOk);
    }
    //-----------------------------------------------
    //          D I A L O G   L O G I C
    //-----------------------------------------------

    private AlertDialog loadingDialogInstance;

    public void Show(String message, String title, int dialogType, Runnable onCancel, Runnable onOk)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(m_context, R.style.ModalStyle);
        View view = LayoutInflater.from(m_context).inflate(R.layout.component_modal, null);

        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        int icon        = R.drawable.modal_icon_info;
        int background  = R.drawable.modal_background_info;

        switch (dialogType)
        {
            case MODAL_SUCCESS:
                icon        = R.drawable.modal_icon_success;
                background  = R.drawable.modal_background_success;
                break;

            case MODAL_DANGER:
                icon        = R.drawable.modal_icon_danger;
                background  = R.drawable.modal_background_danger;
                break;

            case MODAL_WARNING:
                icon        = R.drawable.modal_icon_warning;
                background  = R.drawable.modal_background_warning;
                break;

            case MODAL_CONFIRM:
                icon        = R.drawable.modal_icon_confirm;
                background  = R.drawable.modal_background_confirm;

                Button cancelButton = view.findViewById(R.id.custom_modal_cancel_button);
                cancelButton.setOnClickListener(v -> {
                    dialog.dismiss();

                    if (onCancel != null)
                        onCancel.run();
                });
                cancelButton.setVisibility(View.VISIBLE);
                break;
        }

        ImageView modalIcon = view.findViewById(R.id.custom_modal_icon);
        modalIcon.setImageResource(icon);

        Button okButton = view.findViewById(R.id.custom_modal_ok_button);
        okButton.setOnClickListener(v -> {
            dialog.dismiss();

            if (onOk != null)
                onOk.run();
        });

        TextView modalText = view.findViewById(R.id.custom_modal_text);
        modalText.setText(message);

        LinearLayout modalBg = view.findViewById(R.id.custom_modal_background);
        modalBg.setBackgroundResource(background);

        TextView modalTitle = view.findViewById(R.id.custom_modal_title);
        modalTitle.setText(title);

        dialog.show();
    }

    public void ShowLoading(String message)
    {
        showLoadingGears(message, null);
    }

    public void ShowLoading(String message, String title)
    {
        showLoadingGears(message, title);
    }

    private void showLoadingGears(String message, String title)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(m_context, R.style.ModalStyle);
        View view = LayoutInflater.from(m_context).inflate(R.layout.component_loading_modal, null);

        builder.setView(view);

        loadingDialogInstance = builder.create();
        loadingDialogInstance.setCancelable(false);

        TextView modalTitle     = view.findViewById(R.id.loading_modal_title);
        TextView modalMessage   = view.findViewById(R.id.loading_modal_indefinite_message);

        if (Str.IsNullOrEmpty(title))
            title = "Hold on...";

        if (Str.IsNullOrEmpty(message))
            message = "Please wait...";

        modalTitle.setText(title);
        modalMessage.setText(message);

        animateLoadingGears(view);

        loadingDialogInstance.show();
    }

    public void showLoadingIndefinite() {
        showIndefinite(null);
    }

    public void showLoadingIndefinite(String message) {
        showIndefinite(message);
    }

    public void closeLoading()
    {
        if (loadingDialogInstance != null)
            loadingDialogInstance.dismiss();
    }

    private void showIndefinite(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(m_context, R.style.ModalStyle);
        View view = LayoutInflater.from(m_context).inflate(R.layout.component_loading_modal_indefinite, null);

        TextView modalMessage = view.findViewById(R.id.loading_modal_indefinite_message);

        if (Str.IsNullOrEmpty(message))
            message = "Loading... please wait.";

        modalMessage.setText(message);

        builder.setView(view);

        loadingDialogInstance = builder.create();
        loadingDialogInstance.setCancelable(false);
        loadingDialogInstance.show();
    }

    private void animateLoadingGears(View view)
    {
        // Get references to your gears
        ImageView smallGear     = view.findViewById(R.id.loading_modal_gear_small);
        ImageView mediumGear    = view.findViewById(R.id.loading_modal_gear_medium);
        ImageView largeGear     = view.findViewById(R.id.loading_modal_gear_large);

        // Create rotation animations for each gear
        RotateAnimation rotateSmall = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,  // pivotX is center of the gear
                Animation.RELATIVE_TO_SELF, 0.5f   // pivotY is center of the gear
        );
        rotateSmall.setDuration(3000);
        rotateSmall.setInterpolator(new LinearInterpolator());
        rotateSmall.setRepeatCount(Animation.INFINITE);

        // For medium gear - rotating in opposite direction
        RotateAnimation rotateMedium = new RotateAnimation(
                0, -360,  // negative value for opposite rotation
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateMedium.setDuration(4000);  // slightly different speed
        rotateMedium.setInterpolator(new LinearInterpolator());
        rotateMedium.setRepeatCount(Animation.INFINITE);

        // For large gear
        RotateAnimation rotateLarge = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateLarge.setDuration(5000);  // slower rotation for larger gear
        rotateLarge.setInterpolator(new LinearInterpolator());
        rotateLarge.setRepeatCount(Animation.INFINITE);

        // Apply animations
        smallGear.startAnimation(rotateSmall);
        mediumGear.startAnimation(rotateMedium);
        largeGear.startAnimation(rotateLarge);
    }
}
