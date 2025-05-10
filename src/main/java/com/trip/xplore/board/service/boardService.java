package com.trip.treaxure.board.service;

import com.trip.treaxure.board.entity.board;
import com.trip.treaxure.board.repository.boardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class boardService {

    @Autowired
    private boardRepository boardRepository;

    public List<board> getAllboards() {
        return boardRepository.findAll();
    }

    public Optional<board> getboardById(Long id) {
        return boardRepository.findById(id);
    }

    public board createboard(board board) {
        return boardRepository.save(board);
    }

    public void deleteboard(Long id) {
        boardRepository.deleteById(id);
    }
} 