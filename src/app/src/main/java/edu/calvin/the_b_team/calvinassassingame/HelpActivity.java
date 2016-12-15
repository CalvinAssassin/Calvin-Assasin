package edu.calvin.the_b_team.calvinassassingame;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the Help activity for the application.
 * It displays some basic help information. Clicking on the icons takes the user to a detailed description.
 *
 */

public class HelpActivity extends AppCompatActivity {

    static public final String ARG_TEXT_ID = "text_id";

    /**
     * onCreate - called when the activity is first created.
     * Called when the activity is first created.
     * This is where you should do all of your normal static set up: create views, bind data to lists, etc.
     * This method also provides you with a Bundle containing the activity's previously frozen state, if there was one.
     *
     * Always followed by onStart().
     *
     */

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setupActionBar();

        // Set up so that formatted text can be in the help_page_intro text and so that html links are handled.
        TextView textView = (TextView) findViewById (R.id.help_page_intro);
        if (textView != null) {
            textView.setMovementMethod (LinkMovementMethod.getInstance());
            textView.setText (Html.fromHtml (getString (R.string.help_page_intro_html)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

/**
 */
// Methods

    /**
     * Handle the click of one of the help buttons on the page.
     * Start an activity to display the help text for the topic selected.
     *
     * @param v View
     * @return void
     */


    /**
     * Show a string on the screen via Toast.
     *
     * @param msg String
     * @param longLength boolean - show message a long time
     * @return void
     */

    public void toast (String msg, boolean longLength)
    {
        Toast.makeText (getApplicationContext(), msg,
                (longLength ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)
        ).show ();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


} // end class
