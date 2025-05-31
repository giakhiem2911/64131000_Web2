package khiem.nhg.Project_64131000.service;

import khiem.nhg.Project_64131000.model.Comment;
import khiem.nhg.Project_64131000.model.Interaction;
import khiem.nhg.Project_64131000.model.InteractionType;
import khiem.nhg.Project_64131000.model.User;
import khiem.nhg.Project_64131000.model.UserActivityDTO;
import khiem.nhg.Project_64131000.repository.CommentRepository;
import khiem.nhg.Project_64131000.repository.InteractionRepository;
import khiem.nhg.Project_64131000.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private InteractionRepository interactionRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User save(User user) {
        if (user.getPasswordHash() != null && !user.getPasswordHash().startsWith("$2a$")) {
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        }
        return userRepository.save(user);
    }
    
    public User updateUser(Long id, User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmail(userDetails.getEmail());
            user.setPasswordHash(userDetails.getPasswordHash());
            user.setFullName(userDetails.getFullName());
            user.setRole(userDetails.getRole());
            user.setBio(userDetails.getBio());
            user.setAvatar(userDetails.getAvatar());
            user.setUpdatedAt(userDetails.getUpdatedAt());
            return userRepository.save(user);
        }
        return null;
    }
    public List<UserActivityDTO> getRecentActivities(User user) {
        List<UserActivityDTO> activities = new ArrayList<>();

        // Lấy comment
        List<Comment> comments = commentRepository.findTop5ByUserOrderByCreatedAtDesc(user);
        for (Comment c : comments) {
            String desc = "Bạn đã bình luận: \"" + c.getContent() + "\" trong bài viết '" + c.getArticle().getTitle() + "'";
            activities.add(new UserActivityDTO(desc, c.getCreatedAt()));
        }

        // Lấy likes
        List<Interaction> likes = interactionRepository.findTop5ByUserAndTypeOrderByCreatedAtDesc(user, InteractionType.LIKE);
        for (Interaction i : likes) {
            String desc = "Bạn đã thích bài viết '" + i.getArticle().getTitle() + "'";
            activities.add(new UserActivityDTO(desc, i.getCreatedAt()));
        }

        // Sắp xếp thời gian giảm dần
        activities.sort(Comparator.comparing(UserActivityDTO::getTimestamp).reversed());

        return activities.stream().limit(5).toList(); // Trả về top 5 hoạt động mới nhất
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}