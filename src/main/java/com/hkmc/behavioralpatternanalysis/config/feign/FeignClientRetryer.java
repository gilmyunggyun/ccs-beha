package com.hkmc.behavioralpatternanalysis.config.feign;

import feign.Retryer;

public class FeignClientRetryer extends Retryer.Default {

    public FeignClientRetryer(){
        super(1000, 3000, 3);
    }

}
