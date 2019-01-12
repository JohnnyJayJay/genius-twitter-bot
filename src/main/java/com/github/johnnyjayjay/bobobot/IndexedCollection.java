package com.github.johnnyjayjay.bobobot;

public interface IndexedCollection<E> {

    E get(int index);

    int size();

    boolean isEmpty();
}
