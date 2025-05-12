package com.trip.treaxure.board.service;

import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.board.repository.boardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class boardService {

    @Autowired
    private boardRepository boardRepository;

    public List<Board> getAllboards() {
        return boardRepository.findAll();
    }

    public Optional<Board> getboardById(Long id) {
        return boardRepository.findById(id);
    }

    public Board createboard(Board board) {
        return boardRepository.save(board);
    }

    public void deleteboard(Long id) {
        boardRepository.deleteById(id);
    }
} 