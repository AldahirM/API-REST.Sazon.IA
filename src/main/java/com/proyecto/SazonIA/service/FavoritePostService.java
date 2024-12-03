package com.proyecto.SazonIA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.proyecto.SazonIA.exception.PostNotFoundException;
import com.proyecto.SazonIA.model.FavoritePost;
import com.proyecto.SazonIA.model.FavoritePostId;
import com.proyecto.SazonIA.model.Post;
import com.proyecto.SazonIA.repository.FavoritePostRepository;
import com.proyecto.SazonIA.repository.PostRepository;

@Service
public class FavoritePostService {

    @Autowired
    private FavoritePostRepository favoritePostRepository;

    @Autowired
    private PostRepository postRepository; // El repositorio de MongoDB para las publicaciones
    /*
     * // Método para obtener las publicaciones guardadas como favoritas por un
     * usuario
     * public List<Post> getContentFavoritePostsByUserId(Integer userId) {
     * // Buscar todas las relaciones de favoritos para el usuario
     * List<FavoritePost> favoritePosts =
     * favoritePostRepository.findByIdUserId(userId);
     * 
     * // Obtener los IDs de las publicaciones favoritas
     * List<String> postIds = favoritePosts.stream()
     * .map(favoritePost -> favoritePost.getId().getPostId())
     * .collect(Collectors.toList());
     * 
     * // Recuperar el contenido de las publicaciones favoritas desde MongoDB
     * return postRepository.findAllById(postIds); // Método de MongoRepository para
     * obtener varias publicaciones por sus IDs
     * }
     */

    // Método para obtener el contenido de una publicación favorita específica de un
    // usuario
    public Post getContentFavoritePostByUserIdAndPostId(Integer userId, String postId) {
        // Verificar si existe una relación de favorito para el usuario y el postId
        boolean isFavorite = favoritePostRepository.existsById(new FavoritePostId(userId, postId));

        if (isFavorite) {
            // Buscar y devolver el contenido completo de la publicación favorita desde
            // MongoDB
            return postRepository.findById(postId)
                    .orElseThrow(() -> new PostNotFoundException(postId));
        } else {
            throw new PostNotFoundException("Favorite post not found for user with id " + userId);
        }
    }

    // Guardar una publicación favorita
    public FavoritePost saveFavoritePost(FavoritePost favoritePost) {
        String postId = favoritePost.getId().getPostId();

        // Verificar si el post existe
        if (postRepository.existsById(postId)) {
            // Guardar como favorito si el post existe
            return favoritePostRepository.save(favoritePost);
        } else {
            throw new PostNotFoundException(postId);
        }
    }

    // Obtener todas las publicaciones favoritas de un usuario
    public Page<FavoritePost> getFavoritePostsByUserId(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return favoritePostRepository.findByIdUserId(userId, pageable);
    }

    // Eliminar una publicación favorita por ID de usuario y publicación
    public void removeFavoritePost(Integer userId, String postId) {
        FavoritePostId favoritePostId = new FavoritePostId(userId, postId);
        favoritePostRepository.deleteById(favoritePostId);
    }

    // Verificar si una publicación ya está marcada como favorita por un usuario
    public boolean isFavorite(Integer userId, String postId) {
        FavoritePostId favoritePostId = new FavoritePostId(userId, postId);
        return favoritePostRepository.existsById(favoritePostId);
    }

}
