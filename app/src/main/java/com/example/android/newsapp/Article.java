package com.example.android.newsapp;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by ayoawotunde on 18/06/2018.
 */

public class Article implements List {
    private String mTitleArticle;
    private String mAuthor;
    private String mTime;
    private String mWeb;
    private String mPicture;

    public Article(String title, String author, String dateTime, String web, String picture) {
        mTitleArticle = title;
        mAuthor = author;
        mTime = dateTime;
        mWeb = web;
        mPicture = picture;
    }

    public String getmTitleArticle() {
        return mTitleArticle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmTime() {
        return mTime;
    }

    public String getmWeb() {
        return mWeb;
    }

    public String getmPicture() {
        return mPicture;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @NonNull
    @Override
    public Iterator iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public boolean add(Object o) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull Collection collection) {
        return false;
    }

    @Override
    public boolean addAll(int i, @NonNull Collection collection) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Object get(int i) {
        return null;
    }

    @Override
    public Object set(int i, Object o) {
        return null;
    }

    @Override
    public void add(int i, Object o) {

    }

    @Override
    public Object remove(int i) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @NonNull
    @Override
    public ListIterator listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator listIterator(int i) {
        return null;
    }

    @NonNull
    @Override
    public List subList(int i, int i1) {
        return null;
    }

    @Override
    public boolean retainAll(@NonNull Collection collection) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection collection) {
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection collection) {
        return false;
    }

    @NonNull
    @Override
    public Object[] toArray(@NonNull Object[] objects) {
        return new Object[0];
    }
}
