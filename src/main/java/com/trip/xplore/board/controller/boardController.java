package com.trip.treaxure.board.controller;

import com.trip.treaxure.board.entity.board;
import com.trip.treaxure.board.service.boardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/boards")
public class boardController {

    @Autowired
    private boardService boardService;

    @GetMapping
    public List<board> getAllboards() {
        return boardService.getAllboards();
    }

    @GetMapping("/{id}")
    public ResponseEntity<board> getboardById(@PathVariable Long id) {
        Optional<board> board = boardService.getboardById(id);
        return board.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public board createboard(@RequestBody board board) {
        return boardService.createboard(board);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteboard(@PathVariable Long id) {
        boardService.deleteboard(id);
        return ResponseEntity.noContent().build();
    }
} 