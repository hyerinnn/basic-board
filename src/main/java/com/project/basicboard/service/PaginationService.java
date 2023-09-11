package com.project.basicboard.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {

    private static final int BAR_LENGTH = 5;

    public List<Integer> getPaginationBarNumbers(int currentPageNumber,int totalPages){
        //중앙값 찾기  -> 0이랑 비교해서 음수가 아닌 더 큰 수를 쓰겠다.
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 0);
        // 총페이지수보다 작은 값까지.
        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPages);

        return IntStream.range(startNumber, endNumber).boxed().toList();
    }

    public int currentBarLength(){

        return BAR_LENGTH;
    }
}
