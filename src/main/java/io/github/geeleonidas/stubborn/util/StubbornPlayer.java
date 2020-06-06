package io.github.geeleonidas.stubborn.util;

import io.github.geeleonidas.stubborn.Bimoe;
import io.github.geeleonidas.stubborn.Stubborn;

public interface StubbornPlayer {
    int getBimoeProgress(Bimoe bimoe);
    void setBimoeProgress(Bimoe bimoe, Integer value);

    int getCurrentDialog(Bimoe bimoe);
    void setCurrentDialog(Bimoe bimoe, Integer value);

    int getCurrentEntry(Bimoe bimoe);
    void setCurrentEntry(Bimoe bimoe, Integer value);

    int getBimoeTextLength(Bimoe bimoe);
    void setBimoeTextLength(Bimoe bimoe, Integer value);

    Integer getDeathCount();
}
