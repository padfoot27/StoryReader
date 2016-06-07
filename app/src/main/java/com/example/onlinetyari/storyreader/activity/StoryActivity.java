package com.example.onlinetyari.storyreader.activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.onlinetyari.storyreader.R;
import com.example.onlinetyari.storyreader.pagination.Pagination;

import java.util.Collections;

public class StoryActivity extends AppCompatActivity implements
        SeekBar.OnSeekBarChangeListener,
        View.OnClickListener {

    public TextView textView;
    public Button backButton;
    public Button forwardButton;
    public SeekBar seekBar;

    public Pagination mPagination;
    public Pagination prevPagination;
    public Pagination nextPagination;
    public CharSequence mText;
    public CharSequence prevText = "";
    public CharSequence currentText;
    public CharSequence nextText;
    public int mCurrentIndex = 0;
    public int i;
    public int oldProgress;
    public SharedPreferences mSettings;

    public static final int MIN_FONT = 12;
    public static final int MAX_FONT = 34;
    public static final int MIN_PROGRESS = 0;
    public static final int MAX_PROGRESS = 100;
    public static final int OPTIMAL_FONT = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle(R.string.app_name);

        textView = (TextView) findViewById(R.id.textView);
        assert textView != null;
        textView.setTextSize(OPTIMAL_FONT);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("This text helps to create some bulk content. " + i + " \n");
        }
        String book_content = sb.toString();
        textView.setOnClickListener(this);

        Spanned htmlString = Html.fromHtml(book_content);
        mText = TextUtils.concat(htmlString);
        mSettings = getSharedPreferences("Settings", 0);

        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Removing layout listener to avoid multiple calls
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    textView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                mPagination = new Pagination(mText,
                        textView.getWidth(),
                        textView.getHeight(),
                        textView.getPaint(),
                        textView.getLineSpacingMultiplier(),
                        textView.getLineSpacingExtra(),
                        textView.getIncludeFontPadding(),
                        true);

                prevPagination = new Pagination(prevText,
                        textView.getWidth(),
                        textView.getHeight(),
                        textView.getPaint(),
                        textView.getLineSpacingMultiplier(),
                        textView.getLineSpacingExtra(),
                        textView.getIncludeFontPadding(),
                        true);

                currentText = mPagination.mPages.get(mCurrentIndex);

                StringBuilder stringBuilder = new StringBuilder();
                for (i = 1; i < mPagination.mPages.size(); i++) {
                    stringBuilder.append(mPagination.mPages.get(i));
                }

                nextText = stringBuilder.toString();

                nextPagination = new Pagination(nextText,
                        textView.getWidth(),
                        textView.getHeight(),
                        textView.getPaint(),
                        textView.getLineSpacingMultiplier(),
                        textView.getLineSpacingExtra(),
                        textView.getIncludeFontPadding(),
                        true);

                update();
            }
        });

        backButton = (Button) findViewById(R.id.back_btn);
        forwardButton = (Button) findViewById(R.id.forward_btn);

        backButton.setOnClickListener(this);

        forwardButton.setOnClickListener(this);

        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        assert seekBar != null;
        oldProgress = getProgressFromTextSize(OPTIMAL_FONT);
        seekBar.setProgress(oldProgress);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_story, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {

        textView.setTextSize(getTextSizeFromProgress(progress));

        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (progress > oldProgress && mCurrentIndex != 0) {
                    // Removing layout listener to avoid multiple calls
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        textView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    if (mCurrentIndex == mPagination.size())
                        mCurrentIndex--;
                    currentText = mPagination.mPages.get(mCurrentIndex);

                    StringBuilder stringBuilder;
                    stringBuilder = new StringBuilder();

                    for (i = 0; i < mCurrentIndex; i++) {
                        stringBuilder.append(mPagination.get(i));
                    }

                    prevText = stringBuilder.toString();

                    stringBuilder = new StringBuilder();
                    for (i = mCurrentIndex + 1; i < mPagination.mPages.size(); i++) {
                        stringBuilder.append(mPagination.mPages.get(i));
                    }

                    nextText = stringBuilder.toString();

                    mPagination = new Pagination(mText,
                            textView.getWidth(),
                            textView.getHeight(),
                            textView.getPaint(),
                            textView.getLineSpacingMultiplier(),
                            textView.getLineSpacingExtra(),
                            textView.getIncludeFontPadding(),
                            true);
                    // update();

                    CharSequence reverse = new StringBuilder(prevText).reverse().toString();

                    prevPagination = new Pagination(reverse,
                            textView.getWidth(),
                            textView.getHeight(),
                            textView.getPaint(),
                            textView.getLineSpacingMultiplier(),
                            textView.getLineSpacingExtra(),
                            textView.getIncludeFontPadding(),
                            false);

                    Collections.reverse(prevPagination.mPages);

                    CharSequence newCurrentText = getCurrentString();

                    int startIndex = newCurrentText.length();

                    stringBuilder = new StringBuilder();
                    stringBuilder.append(currentText.subSequence(startIndex, currentText.length()));
                    stringBuilder.append(nextText);

                    nextText = stringBuilder.toString();

                    nextPagination = new Pagination(nextText,
                            textView.getWidth(),
                            textView.getHeight(),
                            textView.getPaint(),
                            textView.getLineSpacingMultiplier(),
                            textView.getLineSpacingExtra(),
                            textView.getIncludeFontPadding(),
                            true);

                    mPagination.mPages.clear();

                    if (!prevText.equals(""))
                        mPagination.mPages.addAll(prevPagination.mPages);

                    mPagination.mPages.add(newCurrentText);

                    if (!nextText.equals(""))
                        mPagination.mPages.addAll(nextPagination.mPages);

                    if (mCurrentIndex == 0)
                        mCurrentIndex = prevPagination.mPages.size() - 1;

                    else mCurrentIndex = prevPagination.mPages.size();

                    update();
                }

                if ((progress < oldProgress) || (progress > oldProgress && mCurrentIndex == 0)) {
                    // Removing layout listener to avoid multiple calls
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        textView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    StringBuilder stringBuilder;
                    CharSequence nextPageText = "";

                    try {
                        nextPageText = mPagination.mPages.get(mCurrentIndex + 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    stringBuilder = new StringBuilder();

                    try {
                        stringBuilder.append(mPagination.mPages.get(mCurrentIndex));
                        stringBuilder.append(mPagination.mPages.get(mCurrentIndex + 1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    currentText = stringBuilder.toString();

                    stringBuilder = new StringBuilder();

                    for (i = 0; i < mCurrentIndex; i++) {
                        stringBuilder.append(mPagination.get(i));
                    }

                    prevText = stringBuilder.toString();

                    stringBuilder = new StringBuilder();
                    for (i = mCurrentIndex + 1; i < mPagination.mPages.size(); i++) {
                        stringBuilder.append(mPagination.mPages.get(i));
                    }

                    nextText = stringBuilder.toString();

                    mPagination = new Pagination(mText,
                            textView.getWidth(),
                            textView.getHeight(),
                            textView.getPaint(),
                            textView.getLineSpacingMultiplier(),
                            textView.getLineSpacingExtra(),
                            textView.getIncludeFontPadding(),
                            true);

                    CharSequence reverse = new StringBuilder(prevText).reverse().toString();

                    prevPagination = new Pagination(reverse,
                            textView.getWidth(),
                            textView.getHeight(),
                            textView.getPaint(),
                            textView.getLineSpacingMultiplier(),
                            textView.getLineSpacingExtra(),
                            textView.getIncludeFontPadding(),
                            false);

                    Collections.reverse(prevPagination.mPages);

                    CharSequence newCurrentText = fitString(currentText);

                    int startIndex = currentText.length() - newCurrentText.length();
                    stringBuilder = new StringBuilder();
                    if (startIndex < nextPageText.length())
                        startIndex = nextPageText.length() - startIndex;
                    else startIndex = startIndex - nextPageText.length();

                    stringBuilder.append(nextText.subSequence(startIndex, nextText.length()));
                    nextText = stringBuilder.toString();

                    nextPagination = new Pagination(nextText,
                            textView.getWidth(),
                            textView.getHeight(),
                            textView.getPaint(),
                            textView.getLineSpacingMultiplier(),
                            textView.getLineSpacingExtra(),
                            textView.getIncludeFontPadding(),
                            true);

                    mPagination.mPages.clear();

                    if (!prevText.equals(""))
                        mPagination.mPages.addAll(prevPagination.mPages);

                    mPagination.mPages.add(newCurrentText);

                    if (!nextText.equals(""))
                        mPagination.mPages.addAll(nextPagination.mPages);

                    if (mCurrentIndex == 0)
                        mCurrentIndex = prevPagination.mPages.size() - 1;

                    else mCurrentIndex = prevPagination.mPages.size();

                    update();
                }

                oldProgress = progress;
            }
        });
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        seekBar.setVisibility(View.GONE);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void update() {
        final CharSequence text = mPagination.get(mCurrentIndex);
        if (text != null) {
            textView.setText(text);
        }

        if (mCurrentIndex == 0)
            textView.setGravity(Gravity.BOTTOM);

        else textView.setGravity(Gravity.NO_GRAVITY);
    }

    private CharSequence getCurrentString() {
        final StaticLayout layout = new StaticLayout(textView.getText(), textView.getPaint(), textView.getWidth(), Layout.Alignment.ALIGN_NORMAL, textView.getLineSpacingMultiplier(), textView.getLineSpacingExtra(), textView.getIncludeFontPadding());

        final int lines = layout.getLineCount();
        final CharSequence text = layout.getText();
        int startOffset = 0;
        int height = textView.getHeight();

        for (int i = 0; i < lines; i++) {
            if (height < layout.getLineBottom(i)) {
                // When the layout height has been exceeded
                return text.subSequence(startOffset, layout.getLineStart(i));
            }

            if (i == lines - 1) {
                // Put the rest of the text into the last page
                return text.subSequence(startOffset, layout.getLineEnd(i));
            }
        }

        return "";
    }

    private CharSequence fitString(CharSequence text) {
        final StaticLayout layout = new StaticLayout(text, textView.getPaint(), textView.getWidth(), Layout.Alignment.ALIGN_NORMAL, textView.getLineSpacingMultiplier(), textView.getLineSpacingExtra(), textView.getIncludeFontPadding());

        final int lines = layout.getLineCount();
        int startOffset = 0;
        int height = textView.getHeight();

        for (int i = 0; i < lines; i++) {
            if (height < layout.getLineBottom(i)) {
                // When the layout height has been exceeded
                return text.subSequence(startOffset, layout.getLineStart(i));
            }

            if (i == lines - 1) {
                // Put the rest of the text into the last page
                return text.subSequence(startOffset, layout.getLineEnd(i));
            }
        }

        return "";
    }

    public void resetPages() {

    }

    public float getTextSizeFromProgress(int progress) {
        float slope = ((float)(MAX_FONT - MIN_FONT) / (MAX_PROGRESS - MIN_PROGRESS));
        return (float) MIN_FONT + (slope * (progress - MIN_PROGRESS));
    }

    public int getProgressFromTextSize(int textSize) {
        float slope = ((float) (MAX_PROGRESS - MIN_PROGRESS) / (MAX_FONT - MIN_FONT));
        return MIN_PROGRESS + (int) (slope * (textSize - MIN_FONT));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_btn : if (mCurrentIndex > 0) {
                                    mCurrentIndex--;
                                    update();
                                 }
                                 break;

            case R.id.forward_btn : if (mCurrentIndex < mPagination.size() - 1) {
                                        mCurrentIndex++;
                                        update();
                                    }
                                    break;

            case R.id.textView : seekBar.setVisibility(View.VISIBLE);
                                 break;

            default : break;
        }
    }
}
