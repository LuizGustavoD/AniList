package com.anilist.backend.server.controller.exceptions.handler;

import java.time.Instant;
import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;

import com.anilist.backend.server.controller.exceptions.StandartError;
import com.anilist.backend.server.service.exceptions.custom.AnimeNotFoundException;
import com.anilist.backend.server.service.exceptions.custom.DuplicateEmailException;
import com.anilist.backend.server.service.exceptions.custom.EmailAlreadyUsedException;
import com.anilist.backend.server.service.exceptions.custom.EntityNotFoundException;
import com.anilist.backend.server.service.exceptions.custom.FriendshipRequestException;
import com.anilist.backend.server.service.exceptions.custom.InvalidTokenException;
import com.anilist.backend.server.service.exceptions.custom.ResourceAlreadyExistsException;
import com.anilist.backend.server.service.exceptions.custom.TimeOutTokenException;
import com.anilist.backend.server.service.exceptions.custom.UnauthorizedActionException;
import com.anilist.backend.server.service.exceptions.custom.UserForbiddenAccessException;
import com.anilist.backend.server.service.exceptions.custom.UserNotFoundException;
import com.anilist.backend.server.service.exceptions.custom.ValidationException;


@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<StandartError> handleDuplicateEmailException(DuplicateEmailException ex, HttpServletRequest request) {
        StandartError error = new StandartError();
        error.setTimestamp(Instant.now());
        error.setStatus(409);
        error.setError("Duplicate email error");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        error.setErrors(Collections.singletonList(ex.getMessage()));
        return ResponseEntity.status(409).body(error);
    }
    

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandartError> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        StandartError error = new StandartError();
        error.setTimestamp(Instant.now());
        error.setStatus(404);
        error.setError("Entity Not Found");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(AnimeNotFoundException.class)
    public ResponseEntity<StandartError> handleAnimeNotFoundException(AnimeNotFoundException ex, HttpServletRequest request) {
        StandartError error = new StandartError();
        error.setTimestamp(Instant.now());
        error.setStatus(404);
        error.setError("Anime Not Found");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<StandartError> handleInvalidTokenException(InvalidTokenException ex, HttpServletRequest request){
        StandartError error = new StandartError();
        error.setTimestamp(Instant.now());
        error.setStatus(401);
        error.setError("Invalid Token");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(401).body(error);
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<StandartError> handleEmailAlreadyUsedException(EmailAlreadyUsedException ex, HttpServletRequest request) {
        StandartError error = new StandartError();
        error.setTimestamp(Instant.now());
        error.setStatus(409);
        error.setError("Email Already Used");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<StandartError> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        StandartError error = new StandartError();
        error.setTimestamp(Instant.now());
        error.setStatus(404);
        error.setError("User Not Found");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(FriendshipRequestException.class)
    public ResponseEntity<StandartError> handleFriendshipRequestException(FriendshipRequestException ex, HttpServletRequest request) {
        StandartError error = new StandartError();
        error.setTimestamp(Instant.now());
        error.setStatus(400);
        error.setError("Friendship Request Error");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<StandartError> handleUnauthorizedActionException(UnauthorizedActionException ex, HttpServletRequest request) {
        StandartError error = new StandartError();
        error.setTimestamp(Instant.now());
        error.setStatus(403);
        error.setError("Unauthorized Action");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(403).body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<StandartError> handleValidationException(ValidationException ex, HttpServletRequest request) {
        StandartError error = new StandartError();
        error.setTimestamp(Instant.now());
        error.setStatus(422);
        error.setError("Validation Error");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        error.setErrors(ex.getErrors() != null ? ex.getErrors() : Collections.singletonList(ex.getMessage()));
        return ResponseEntity.status(422).body(error);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<StandartError> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex, HttpServletRequest request) {
        StandartError error = new StandartError();
        error.setTimestamp(Instant.now());
        error.setStatus(409);
        error.setError("Resource Already Exists");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(UserForbiddenAccessException.class)
    public ResponseEntity<StandartError> handleUserForbiddenAccessException(UserForbiddenAccessException ex, HttpServletRequest request) {
        StandartError error = new StandartError();
        error.setTimestamp(Instant.now());
        error.setStatus(403);
        error.setError("User Forbidden Access");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(403).body(error);
    }

    @ExceptionHandler(TimeOutTokenException.class)
    public ResponseEntity<StandartError> handleTimeOutTokenException(TimeOutTokenException ex, HttpServletRequest request) {
        StandartError error = new StandartError();
        error.setTimestamp(Instant.now());
        error.setStatus(408);
        error.setError("Time Out Token");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(408).body(error);
    }
}
