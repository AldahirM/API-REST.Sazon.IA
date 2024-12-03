package com.proyecto.SazonIA.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.proyecto.SazonIA.model.Follower;
import com.proyecto.SazonIA.model.User;
import com.proyecto.SazonIA.repository.FollowerRepository;
import com.proyecto.SazonIA.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FollowerService {

    @Autowired
    private FollowerRepository repo;
    @Autowired
    private UserRepository userRepository;

    // Seguir a un usuario
    public Follower followUser(User follower, User followed) {
        Follower newFollower = new Follower();
        newFollower.setUser(follower);
        newFollower.setFollowed(followed);
        return repo.save(newFollower);
    }

    // Dejar de seguir a un usuario
    public void unfollowUser(User follower, User followed) {
        Optional<Follower> existingFollower = repo.findByUserAndFollowed(follower, followed);
        if (existingFollower.isPresent()) {
            repo.delete(existingFollower.get());
        }
    }


    // Obtener lista de seguidores de un usuario con paginación
    public Page<Follower> getFollowers(User followed, int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        return repo.findByFollowed(followed, pageReq); // Asegúrate de tener este método en el repositorio
    }

    // Obtener lista de usuarios a los que sigue un usuario con paginación
    public Page<Follower> getFollowing(User follower, int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        return repo.findByUser(follower, pageReq); // Asegúrate de tener este método en el repositorio
    }

    // Método para encontrar un usuario por su ID
    public User findUserById(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public boolean isFollowing(User follower, User followed) {
        return repo.findByUserAndFollowed(follower, followed).isPresent();
    }

}
