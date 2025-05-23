package com.macle.estamp.seal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SealCircle {
    private Integer line;
    private Integer width;
    private Integer height;
}