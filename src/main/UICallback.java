package main;

import java.util.concurrent.locks.Lock;

/**
 * Created by Mkhitar on 19.03.2015.
 */
public interface UICallback {

    public void refresh(final int position, int[] params);
}
