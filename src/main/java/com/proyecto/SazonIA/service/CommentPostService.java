package com.proyecto.SazonIA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.proyecto.SazonIA.exception.PostNotFoundException;
import com.proyecto.SazonIA.exception.UserNotFoundException;
import com.proyecto.SazonIA.model.CommentPost;
import com.proyecto.SazonIA.model.Post;
import com.proyecto.SazonIA.model.User;
import com.proyecto.SazonIA.repository.CommentPostRepository;
import com.proyecto.SazonIA.repository.PostRepository;
import com.proyecto.SazonIA.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
public class CommentPostService {

    @Autowired
    private CommentPostRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository; // Inyección del repositorio de usuarios

    public CommentPost addComment(String postId, Integer userId, String content) {
        // Verificar si el usuario está registrado
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Verificar si el post al que se añadirá el comentario existe
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        // Crear una nueva instancia de CommentPost con el usuario y el contenido
        // proporcionado
        CommentPost comment = new CommentPost(post.getPostId(), user.getUser_id(), content);

        // Generar un ID único para el comentario (opcional, ya lo hace el constructor)
        comment.setCommentId(UUID.randomUUID().toString());

        // Guardar el comentario en el repositorio de comentarios
        return commentRepository.save(comment);
    }

    public List<CommentPost> getCommentsByPostId(String postId) {
        return commentRepository.findByPostId(postId);
    }

    // Obtener un comentario por ID
    public Optional<CommentPost> getCommentById(String postId) {
        return commentRepository.findById(postId);
    }

    // Obtener comentarios paginados por postId
    public List<CommentPost> getCommentsByPostId(String postId, int page, int pageSize) {
        // Crear el objeto PageRequest para la paginación
        PageRequest pageReq = PageRequest.of(page, pageSize);

        // Obtener los comentarios paginados del repositorio
        Page<CommentPost> commentsPage = commentRepository.findByPostId(postId, pageReq);
        List<CommentPost> comments = commentsPage.getContent();

        // Rellenar el campo userFullName para cada comentario
        comments.forEach(comment -> {
            Optional<User> user = userRepository.findById(comment.getUserId());
            user.ifPresent(u -> comment.setUserFullName(u.getFullName()));
        });

        return comments;
    }

    public CommentPost editComment(String postId, String commentId, String content) {
        // Verificar si el comentario existe
        CommentPost existingComment = commentRepository.findById(commentId).orElseThrow(
                () -> new NoSuchElementException("Comment not found"));

        // Verificar si el comentario pertenece al post correcto
        if (!existingComment.getPostId().equals(postId)) {
            throw new IllegalArgumentException("Comment does not belong to the specified post");
        }

        // Actualizar solo el contenido del comentario
        existingComment.setContent(content);
        return commentRepository.save(existingComment);
    }

    public boolean deleteComment(String postId, String commentId) {
        // Buscar el comentario por su ID
        CommentPost comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));

        // Verificar si el comentario pertenece al post proporcionado
        if (!comment.getPostId().equals(postId)) {
            throw new IllegalArgumentException("Comment does not belong to the specified post");
        }

        // Eliminar el comentario
        commentRepository.deleteById(commentId);
        return true; // Eliminación exitosa
    }

}
