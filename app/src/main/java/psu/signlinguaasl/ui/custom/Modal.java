package psu.signlinguaasl.ui.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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
}
