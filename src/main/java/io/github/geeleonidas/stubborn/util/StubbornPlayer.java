package io.github.geeleonidas.stubborn.util;

import io.github.geeleonidas.stubborn.Stubborn;

public interface StubbornPlayer {
    int getBimoeProgress(Stubborn.Bimoe bimoe);
    void setBimoeProgress(Stubborn.Bimoe bimoe, Integer value);
}
