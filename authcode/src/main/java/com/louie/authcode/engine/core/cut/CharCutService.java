package com.louie.authcode.engine.core.cut;

import com.louie.authcode.engine.model.Letter;

import java.util.Set;

/**
 * Created by liuhongyu.louie on 2016/8/23.
 */
public interface CharCutService {

    Set<Letter> divideToLetters(int[][] RGB);

}
