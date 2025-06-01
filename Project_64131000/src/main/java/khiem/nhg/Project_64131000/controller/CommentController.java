//package khiem.nhg.Project_64131000.controller;
//
//import java.security.Principal;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import khiem.nhg.Project_64131000.model.Comment;
//import khiem.nhg.Project_64131000.repository.CommentRepository;
//import khiem.nhg.Project_64131000.service.CommentService;
//
//@Controller
//public class CommentController{
//	@Autowired
//	public CommentRepository commentRepository;
//	
//	@Autowired
//	public CommentService commentService;
//	
//	@DeleteMapping("articles/comments/{id}/delete")
//	@ResponseBody
//	public ResponseEntity<?> deleteComment(@PathVariable Long id, Principal principal) {
//	    Optional<Comment> optionalComment = commentRepository.findById(id);
//
//	    if (optionalComment.isEmpty()) {
//	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy bình luận.");
//	    }
//
//	    Comment comment = optionalComment.get();
//	    if (!comment.getUser().getEmail().equals(principal.getName())) {
//	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền xóa bình luận này.");
//	    }
//
//	    commentService.deleteComment(id);
//	    return ResponseEntity.ok("Xóa thành công.");
//	}
//	@PutMapping("articles/comments/{id}/edit")
//	@ResponseBody
//	public ResponseEntity<?> editComment(@PathVariable Long id,
//	                                     @RequestParam("content") String content,
//	                                     Principal principal) {
//	    Optional<Comment> optionalComment = commentRepository.findById(id);
//
//	    if (optionalComment.isEmpty()) {
//	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy bình luận.");
//	    }
//
//	    Comment comment = optionalComment.get();
//	    if (!comment.getUser().getEmail().equals(principal.getName())) {
//	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền sửa bình luận này.");
//	    }
//
//	    comment.setContent(content);
//	    comment.setUpdatedAt(LocalDateTime.now());
//	    commentService.saveComment(comment);
//
//	    Map<String, Object> response = new HashMap<>();
//	    response.put("updatedAt", comment.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
//	    response.put("content", comment.getContent());
//
//	    return ResponseEntity.ok(response);
//	}
//
//}