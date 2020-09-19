package io.github.geeleonidas.stubborn.util;

import io.github.geeleonidas.stubborn.Bimoe;

public interface StubbornPlayer {
    int getBimoeProgress(Bimoe bimoe);
    void updateBimoeProgress(Bimoe bimoe, Integer delta);

    String getCurrentAwayDialog(Bimoe bimoe);
    void setCurrentAwayDialog(Bimoe bimoe, String value);

    String getCurrentDialog(Bimoe bimoe);
    void setCurrentDialog(Bimoe bimoe, String value);

    int getCurrentEntry(Bimoe bimoe);
    void setCurrentEntry(Bimoe bimoe, Integer value);

    int getDeathCount();
}
