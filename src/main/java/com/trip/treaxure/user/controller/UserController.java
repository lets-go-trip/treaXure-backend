package com.trip.treaxure.user.controller;

import com.trip.treaxure.user.entity.User;
import com.trip.treaxure.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "사용자 관리를 위한 API")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "전체 사용자 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 리스트 조회 성공")
    })
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "사용자 ID로 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "사용자 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 생성 성공")
    })
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @Operation(summary = "사용자 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "사용자 삭제 성공")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
} 