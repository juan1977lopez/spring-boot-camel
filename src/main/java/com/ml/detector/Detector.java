package com.ml.detector;

import com.ml.exception.ValidationException;

public interface Detector {

    boolean isMutant(String[] dna) throws ValidationException;
}
