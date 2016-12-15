package edu.calvin.the_b_team.calvinassassingame;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * This activity displays some help information on a topic.
 * It displays some text and provides a way to get back to the home activity.
 * The text to be displayed is determined by the text id argument passed to the activity.
 *
 */

public class TopicActivity extends AppCompatActivity {

    int mTextResourceId = 0;

    /**
     * onCreate
     *
     * @param savedInstanceState Bundle
     */

    protected void onCreate(Bundle savedInstanceState) {
        setupActionBar();
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_topic);

        // Read the arguments from the Intent object.
        Intent in = getIntent ();
        mTextResourceId = in.getIntExtra (HelpActivity.ARG_TEXT_ID, 0);
        if (mTextResourceId <= 0) mTextResourceId = R.string.no_help_available;

        TextView textView = (TextView) findViewById (R.id.topic_text);
        textView.setMovementMethod (LinkMovementMethod.getInstance());
        textView.setText (Html.fromHtml (getString (mTextResourceId)));
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
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


} // end class