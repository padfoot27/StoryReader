package com.example.onlinetyari.storyreader.pagination;

/**
 * Created by Siddharth Verma on 7/6/16.
 */
public interface PaginationInterface {

    void layout();

    void reverseLayout();

    void addPage(CharSequence text);

    int size();

    CharSequence get(int index);
}
