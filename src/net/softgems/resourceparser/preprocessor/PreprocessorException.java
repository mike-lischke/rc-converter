/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser.preprocessor;


/**
 * Special preprocessor exception.
 *
 * // @author Mike Lischke
 */
public class PreprocessorException extends Exception {
    //------------------------------------------------------------------------------------------------

    /**
     * Constructor of the exception class.
     *
     * @param message The message for the exception.
     */
    public PreprocessorException(String message) {
        super(message);
    }

    //------------------------------------------------------------------------------------------------
}
