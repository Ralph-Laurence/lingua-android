package psu.signlinguaasl.ui.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.function.Consumer;

import org.javatuples.Triplet;
import psu.signlinguaasl.R;

public class NavBanner extends LinearLayout //implements View.OnClickListener
{
    // Button, Nav Title, Scroll To
    private final HashMap<String, Triplet<Button, String, Integer>> navItems = new HashMap<>();
    private final HashMap<String, Integer> navItemsActiveIcons = new HashMap<>();
    private final HashMap<String, Integer> navItemsInactiveIcons = new HashMap<>();
    private Consumer<Integer> onNavItemSelected;

    private final Context m_context;

    private int BUTTON_TEXT_COLOR_NORMAL;
    private int BUTTON_TEXT_COLOR_ACTIVE;

    // Views
    private ImageView navPageIcon;
    private Button homeButton, chatButton, profileButton;
    private TextView navTitleText;

    public NavBanner(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        inflate(context, R.layout.nav_banner, this);
        m_context = context;

        init();
    }

    public void setOnNavItemSelected(Consumer<Integer> listener)
    {
        onNavItemSelected = listener;
    }

    private void init()
    {
        BUTTON_TEXT_COLOR_NORMAL = ContextCompat.getColor(m_context, R.color.blue_grey_900);
        BUTTON_TEXT_COLOR_ACTIVE = ContextCompat.getColor(m_context, R.color.white);

        navItemsInactiveIcons.put("home",    R.drawable.nav_item_icon_home);
        navItemsInactiveIcons.put("chat",    R.drawable.nav_item_icon_chat);
        navItemsInactiveIcons.put("profile", R.drawable.nav_item_icon_profile);

        navItemsActiveIcons.put("home",    R.drawable.nav_item_icon_home_active);
        navItemsActiveIcons.put("chat",    R.drawable.nav_item_icon_chat_active);
        navItemsActiveIcons.put("profile", R.drawable.nav_item_icon_profile_active);

        homeButton      = findViewById(R.id.navbar_item_home);
        chatButton      = findViewById(R.id.navbar_item_chat);
        profileButton   = findViewById(R.id.navbar_item_profile);
        navTitleText    = findViewById(R.id.nav_banner_title_text);
        navPageIcon     = findViewById(R.id.nav_banner_page_icon);

        navItems.put("home",    new Triplet<>(homeButton, "Home", 0));
        navItems.put("chat",    new Triplet<>(chatButton, "Chat", 1));
        navItems.put("profile", new Triplet<>(profileButton, "Profile", 2));

        navItems.forEach((key, obj) -> {
            Button navItem = obj.getValue0();

            navItem.setOnClickListener(v -> handleNavItemClicked(navItem, key, obj));
        });
    }

    private void handleNavItemClicked(Button sender, String key, Triplet<Button, String, Integer> obj)
    {
        // Reset other buttons
        setNavItemInactive(homeButton, "home");
        setNavItemInactive(chatButton, "chat");
        setNavItemInactive(profileButton, "profile");

        // Emphasize the active button
        setNavItemActive(sender, key);

        // Set the nav banner title
        navTitleText.setText(obj.getValue1());

        // Set the nav banner icon
        navPageIcon.setImageResource(navItemsInactiveIcons.get(key));

        // Upon selecting a nav item, notify the consumer with target scroll pos
        onNavItemSelected.accept(obj.getValue2());
    }

    private void setNavItemActive(Button button, String iconKey)
    {
        int activeIcon = navItemsActiveIcons.get(iconKey);

        setNavItemAppearance(button, R.drawable.banner_navbar_item_active_bg, BUTTON_TEXT_COLOR_ACTIVE, activeIcon);
    }

    private void setNavItemInactive(Button button, String iconKey)
    {
        int inactiveIcon = navItemsInactiveIcons.get(iconKey);

        Log.e("console", "INACTIVE = " + inactiveIcon);

        setNavItemAppearance(button, R.drawable.banner_navbar_item_bg, BUTTON_TEXT_COLOR_NORMAL, inactiveIcon);
    }

    private void setNavItemAppearance(Button button, int backgroundResId, int textColor, int iconResId)
    {
        Drawable icon = ContextCompat.getDrawable(m_context, iconResId);

        button.setBackgroundResource(backgroundResId);
        button.setTextColor(textColor);
        button.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
    }
}
