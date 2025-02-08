package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommentMapper {

    public CommentDto toCommentDto(Comment comment, Item item) {
        CommentDto commentDto = new CommentDto();
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());
        commentDto.setId(comment.getId());
        commentDto.setItem(item);
        return commentDto;
    }

    public Comment toComment(CommentDto comment, User author) {
        Comment convertedComment = new Comment();
        convertedComment.setText(comment.getText());
        convertedComment.setAuthor(author);
        convertedComment.setCreated(comment.getCreated());
        convertedComment.setId(comment.getId());
        convertedComment.setItem(comment.getItem());
        return convertedComment;
    }

    public List<CommentDto> toListOfCommentDto(List<Comment> comments) {
        List<CommentDto> commentsDto = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDto newComment = toCommentDto(comment, comment.getItem());
            commentsDto.add(newComment);
        }
        return commentsDto;
    }
}
