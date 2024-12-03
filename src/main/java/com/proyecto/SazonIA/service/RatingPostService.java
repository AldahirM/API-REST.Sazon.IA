package com.proyecto.SazonIA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.SazonIA.exception.UserNotFoundException;
import com.proyecto.SazonIA.model.Post;
import com.proyecto.SazonIA.model.RatingPost;
import com.proyecto.SazonIA.model.User;
import com.proyecto.SazonIA.repository.PostRepository;
import com.proyecto.SazonIA.repository.RatingPostRepository;
import com.proyecto.SazonIA.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RatingPostService {

    @Autowired
    private RatingPostRepository ratingPostRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    // Método para crear una nueva valoración
    public RatingPost createRatingPost(String postId, Integer userId, Integer value) {
        // Verificar si el usuario está registrado
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
    
        // Validación: Verificar si ya existe una valoración para el mismo postId y userId
        if (validateExistingRating(postId, userId)) {
            throw new IllegalArgumentException("User has already rated this post");
        }
    
        // Crear el objeto RatingPost
        RatingPost ratingPost = new RatingPost(postId, userId, value);
    
        // Guardar la valoración en la base de datos
        RatingPost savedRating = ratingPostRepository.save(ratingPost);
    
        // Obtener el post asociado
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            // Guardar el post actualizado
            post.addRating(value);
            postRepository.save(post);
        }
        return savedRating; // Retornar la valoración guardada
    }
    
    public RatingPost updateRating(String ratingId, Integer value) {
        Optional<RatingPost> optionalRating = ratingPostRepository.findById(ratingId);
        if (optionalRating.isPresent()) {
            RatingPost rating = optionalRating.get();
            String postId = rating.getPostId();
            int oldValue = rating.getValue();

            rating.setValue(value);
            RatingPost updatedRating = ratingPostRepository.save(rating);

            Post post = postRepository.findById(postId).orElse(null);
            if (post != null) {
                post.updateRating(oldValue, value); // Ajusta el total y la suma acumulada
                postRepository.save(post);
            }

            return updatedRating;
        } else {
            return null;
        }
    }

    // Método para eliminar una valoración por su ID
    public boolean deleteRating(String ratingId) {
        Optional<RatingPost> optionalRating = ratingPostRepository.findById(ratingId);
        if (optionalRating.isPresent()) {
            RatingPost rating = optionalRating.get();
            String postId = rating.getPostId();
            int value = rating.getValue();

            ratingPostRepository.deleteById(ratingId);

            Post post = postRepository.findById(postId).orElse(null);
            if (post != null) {
                post.removeRating(value); // Resta el valor del rating eliminado
                postRepository.save(post);
            }

            return true;
        }

        return false;
    }

    // Método para obtener una valoración por su ID
    public Optional<RatingPost> getRatingById(String ratingId) {
        return ratingPostRepository.findById(ratingId);
    }

    public boolean validateExistingRating(String postId, Integer userId) {
        List<RatingPost> existingRatings = ratingPostRepository.findByPostId(postId);
        for (RatingPost rating : existingRatings) {
            if (rating.getUserId().equals(userId)) {
                return true; // Si la valoración ya existe, retornamos true
            }
        }
        return false; // Si no existe, retornamos false
    }

    public Optional<RatingPost> getRatingByPostIdAndUserId(String postId, Integer userId) {
        return ratingPostRepository.findByPostIdAndUserId(postId, userId);
    }
    
}
