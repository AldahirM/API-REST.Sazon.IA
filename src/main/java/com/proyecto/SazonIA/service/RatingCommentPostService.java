package com.proyecto.SazonIA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.SazonIA.exception.UserNotFoundException;
import com.proyecto.SazonIA.model.CommentPost;
import com.proyecto.SazonIA.model.RatingCommentPost;
import com.proyecto.SazonIA.model.User;
import com.proyecto.SazonIA.repository.CommentPostRepository;
import com.proyecto.SazonIA.repository.RatingCommentPostRepository;
import com.proyecto.SazonIA.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RatingCommentPostService {

    @Autowired
    private RatingCommentPostRepository ratingCommentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentPostRepository commentPostRepository;

    // Método para crear una nueva valoración de un comentario
    public RatingCommentPost createRatingComment(String commentId, Integer userId, Integer value) {
        // Verificar si el usuario está registrado
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        // Verificar si ya existe una valoración para el mismo commentId y userId
        if (validateExistingRating(commentId, userId)) {
            throw new IllegalArgumentException("User has already rated this post");
        }

        // Crear el objeto RatingComment
        RatingCommentPost ratingComment = new RatingCommentPost(commentId, userId, value);

        // Guardar la valoración en la base de datos
        RatingCommentPost savedRating = ratingCommentRepository.save(ratingComment);

        // Obtener el comentario asociado
        CommentPost comment = commentPostRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // Agregar la nueva valoración al comentario
        comment.addRating(value); // Asegúrate de que el método addRating exista en la clase CommentPost

        // Guardar el comentario actualizado
        commentPostRepository.save(comment);

        return savedRating; // Retornar la valoración guardada
    }

    // Método para actualizar una valoración existente
    public RatingCommentPost updateRatingComment(String ratingId, Integer value) {
        Optional<RatingCommentPost> optionalRating = ratingCommentRepository.findById(ratingId);
        if (optionalRating.isPresent()) {
            RatingCommentPost rating = optionalRating.get();
            String commentId = rating.getCommentId(); // Obtener el commentId de la valoración

            // Guardar el valor antiguo para actualizar el comentario más tarde
            int oldValue = rating.getValue();

            // Actualizar el valor de la valoración
            rating.setValue(value);
            RatingCommentPost updatedRating = ratingCommentRepository.save(rating);

            // Obtener el comentario asociado
            CommentPost comment = commentPostRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

            // Actualizar la valoración en la lista de valoraciones del comentario
            comment.updateRating(oldValue, value); // Asegúrate de que este método existe
            commentPostRepository.save(comment);

            return updatedRating; // Retornar la valoración actualizada
        } else {
            return null; // O lanza una excepción si la valoración no se encuentra
        }
    }

    // Método para eliminar una valoración por su ID
public boolean deleteRatingComment(String ratingId) {
    RatingCommentPost rating = ratingCommentRepository.findById(ratingId)
        .orElseThrow(() -> new IllegalArgumentException("Rating not found"));

    String commentId = rating.getCommentId(); // Obtener el commentId de la valoración
    int value = rating.getValue(); // Obtener el valor de la valoración

    // Eliminar la valoración
    ratingCommentRepository.deleteById(ratingId);

    // Actualizar el comentario asociado
    CommentPost comment = commentPostRepository.findById(commentId)
        .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

    comment.removeRating(value); // Resta el valor del rating eliminado
    commentPostRepository.save(comment);

    return true; // Retornar true si la eliminación fue exitosa
}


    // Método para obtener una valoración por su ID
    public Optional<RatingCommentPost> getRatingById(String ratingId) {
        return ratingCommentRepository.findById(ratingId);
    }

    public boolean validateExistingRating(String commentId, Integer userId) {
        List<RatingCommentPost> existingRatings = ratingCommentRepository.findByCommentId(commentId);
        for (RatingCommentPost rating : existingRatings) {
            if (rating.getUserId().equals(userId)) {
                return true; // Si la valoración ya existe, retornamos true
            }
        }
        return false; // Si no existe, retornamos false
    }

    public Optional<RatingCommentPost> findRatingByCommentIdAndUserId(String commentId, Integer userId) {
        List<RatingCommentPost> ratings = ratingCommentRepository.findByCommentIdAndUserId(commentId, userId);
        if (ratings.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ratings.get(0)); // Consideramos el primero si hay duplicados inesperados
    }
    
}
