package com.monkila_tech.mokopay_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FactureServiceImpl implements FactureService {
    @Autowired
    private PostRepository postRepository;

    @Override
    public Post savePost(Post post) throws Exception {
        return postRepository.save(post);
    }

    @Override
    public List<Post> fetchPostList() throws Exception {
        return (List<Post>) postRepository.findAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Post> fetchPostSponsorise() throws Exception {
        return (List<Post>) postRepository.getPostSponsorise();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Post> fetchPostListByUserId(String userId) throws Exception {
        return (List<Post>) postRepository.getByUserId(userId);
    }

    @Override
    public Post updatePost(Post post, Long postId) throws Exception {

        Post postDB = postRepository.findById(postId)
                .get();

        if (Objects.nonNull(post.getNom())
                && !"".equalsIgnoreCase(
                        post.getNom())) {
            postDB.setNom(
                    post.getNom());
        }

        if (Objects.nonNull(post.getDescription())
                && !"".equalsIgnoreCase(
                        post.getDescription())) {
            postDB.setDescription(
                    post.getDescription());
        }

        if (Objects.nonNull(post.getLocalisation())
                && !"".equalsIgnoreCase(
                        post.getLocalisation())) {
            postDB.setLocalisation(
                    post.getLocalisation());
        }

        // if (Objects.nonNull(post.getProprietaire())
        // && !"".equalsIgnoreCase(
        // post.getProprietaire())) {
        // postDB.setProprietaire(
        // post.getProprietaire());
        // }

        // if (Objects.nonNull(post.getContact())
        // && !"".equalsIgnoreCase(
        // post.getContact())) {
        // postDB.setContact(
        // post.getContact());
        // }

        if (Objects.nonNull(post.getCreatedAt())
                && !"".equalsIgnoreCase(
                        post.getCreatedAt())) {
            postDB.setCreatedAt(
                    post.getCreatedAt());
        }

        if (Objects.nonNull(post.getPhotos1())
                && !"".equalsIgnoreCase(
                        post.getPhotos1())) {
            postDB.setPhotos1(
                    post.getPhotos1());
        }

        if (Objects.nonNull(post.getPhotos2())
                && !"".equalsIgnoreCase(
                        post.getPhotos2())) {
            postDB.setPhotos2(
                    post.getPhotos2());
        }

        if (Objects.nonNull(post.getPhotos3())
                && !"".equalsIgnoreCase(
                        post.getPhotos3())) {
            postDB.setPhotos3(
                    post.getPhotos3());
        }

        // postDB.setPhotos(post.getPhotos());

        postDB.setUser(post.getUser());

        return postRepository.save(postDB);
    }

    @Override
    public Post getPostById(Long postId) throws Exception {
        return postRepository.findById(postId).get();
    }

    // @SuppressWarnings("unchecked")
    // @Override
    // public List<Post> getPostByProprietaire(String proprietaire) {
    // return (List<Post>) postRepository.getByProprietaire(proprietaire);
    // }

    @SuppressWarnings("unchecked")
    @Override
    public List<Post> getPostByLocolisation(String localisation) {
        return (List<Post>) postRepository.getByLocalisation(localisation);
    }

    // @SuppressWarnings("unchecked")
    // @Override
    // public List<Post> getPostByCategorie(String categorie) {
    // return (List<Post>) postRepository.getByCategorie(categorie);
    // }

    @SuppressWarnings("unchecked")
    @Override
    public List<Post> getPostByUsername(String username) {
        return (List<Post>) postRepository.getByUsername(username);
    }

    @Override
    public Boolean deletePostById(Long postId) throws Exception {

        Optional<Post> post = this.postRepository.findById(postId);

        if (post.isEmpty())
            return false;

        this.postRepository.deleteById(postId);

        Optional<Post> postChecked = this.postRepository.findById(postId);

        if (postChecked.isEmpty())
            return true;
        return false;

    }

    @Override
    public Boolean deletePostByIdAndUserId(Long userId, Long planId) throws Exception {

        Optional<Post> post = this.postRepository.findById(planId);

        if (post.isEmpty())
            return false;

        this.postRepository.deleteByUserId(userId);

        Optional<Post> postChecked = this.postRepository.findById(planId);

        if (postChecked.isEmpty())
            return true;
        return false;

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Post> fetchPostByDate() throws Exception {
        return (List<Post>) postRepository.getByDate();
    }
}
