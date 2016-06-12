package com.example.onlinetyari.storyreader.pagination;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Siddharth Verma on 6/6/16.
 */

public class Pagination implements
        PaginationInterface {
    private final boolean mIncludePad;
    private final int mWidth;
    private final int mHeight;
    private final float mSpacingMult;
    private final float mSpacingAdd;
    private final CharSequence mText;
    private final TextPaint mPaint;
    public final List<CharSequence> mPages;

    public Pagination(CharSequence text, int pageW, int pageH, TextPaint paint, float spacingMult, float spacingAdd, boolean inclidePad, boolean reverse) {
        this.mText = text;
        this.mWidth = pageW;
        this.mHeight = pageH;
        this.mPaint = paint;
        this.mSpacingMult = spacingMult;
        this.mSpacingAdd = spacingAdd;
        this.mIncludePad = inclidePad;
        this.mPages = new ArrayList<CharSequence>();

        if (reverse)
            layout();

        else reverseLayout();
    }

    @Override
    public void layout() {
        final StaticLayout layout = new StaticLayout(mText, mPaint, mWidth, Layout.Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, mIncludePad);

        final int lines = layout.getLineCount();
        final CharSequence text = layout.getText();
        int startOffset = 0;
        int height = mHeight;

        for (int i = 0; i < lines; i++) {

            if (i == lines - 1) {
                // Put the rest of the text into the last page
                addPage(text.subSequence(startOffset, layout.getLineEnd(i)));
                return;
            }

            if (height < layout.getLineBottom(i + 1)) {
                // When the layout height has been exceeded
                addPage(text.subSequence(startOffset, layout.getLineStart(i + 1)));
                startOffset = layout.getLineStart(i + 1);
                height = layout.getLineTop(i + 1) + mHeight;
            }

        }
    }

    @Override
    public void reverseLayout() {
        final StaticLayout layout = new StaticLayout(mText, mPaint, mWidth, Layout.Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, mIncludePad);

        final int lines = layout.getLineCount();
        final CharSequence text = layout.getText();
        int startOffset = 0;
        int height = mHeight;
        String subSeq;
        CharSequence reverse;

        for (int i = 0; i < lines; i++) {
            if (i == lines - 1) {
                // Put the rest of the text into the last page
                subSeq = text.subSequence(startOffset, layout.getLineEnd(i)).toString();
                reverse = new StringBuilder(subSeq).reverse().toString();
                addPage(reverse);
                return;
            }

            if (height < layout.getLineBottom(i + 1)) {
                // When the layout height has been exceeded
                subSeq = text.subSequence(startOffset, layout.getLineStart(i + 1)).toString();
                reverse = new StringBuilder(subSeq).reverse().toString();
                addPage(reverse);
                startOffset = layout.getLineStart(i + 1);
                height = layout.getLineTop(i + 1) + mHeight;
            }


        }
    }

    @Override
    public void addPage(CharSequence text) {
        mPages.add(text);
    }

    @Override
    public int size() {
        return mPages.size();
    }

    @Override
    public CharSequence get(int index) {
        return (index >= 0 && index < mPages.size()) ? mPages.get(index) : null;
    }
}