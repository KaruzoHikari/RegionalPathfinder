package com.mattymatty.RegionalPathfinder.exceptions;

import com.mattymatty.RegionalPathfinder.api.region.Region;

public class AsyncException extends RegionException {
    public AsyncException(Region region) {
        super(region);
    }

    public AsyncException(String message, Region region) {
        super(message, region);
    }

    public AsyncException(String message, Throwable cause, Region region) {
        super(message, cause, region);
    }

    public AsyncException(Throwable cause, Region region) {
        super(cause, region);
    }

    public AsyncException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Region region) {
        super(message, cause, enableSuppression, writableStackTrace, region);
    }
}
