package com.jingchen.pulltorefreshandload.pullableview;

/**
 * Created by Nstd on 2016/1/6.
 */
public class PullableConfig {

    private boolean canPullUp = true;
    private boolean canPullDown = true;

    public boolean canUserPullDown() {
        return canPullDown;
    }

    public boolean canUserPullUp() {
        return canPullUp;
    }

    public void disablePullDown() {
        canPullDown = false;
    }

    public void disablePullUp() {
        canPullUp = false;
    }

    public void disablePull(){
        disablePullDown();
        disablePullUp();
    }

    public void enablePullDown() {
        canPullDown = true;
    }

    public void enablePullUp() {
        canPullUp = true;
    }

    public void enablePull() {
        enablePullDown();
        enablePullUp();
    }

    public void setPullDownState(boolean canPullDown) {
        this.canPullDown = canPullDown;
    }

    public void setPullUpState(boolean canPullUp) {
        this.canPullUp = canPullUp;
    }
}
