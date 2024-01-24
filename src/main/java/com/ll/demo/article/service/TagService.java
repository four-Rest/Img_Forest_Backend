package com.ll.demo.article.service;

import com.ll.demo.article.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ll.demo.article.entity.Tag;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;

    public Tag create(String tagName) {
            Tag tag = new Tag();
            tag.setTagName(tagName);
            tagRepository.save(tag);
            return tag;
    }


    public List<Tag> parseTagStringIntoList(String tagString) {
        List<Tag> tags = new ArrayList<>();
        String[] tagArray = tagString.split(" ");
        for (String tagName : tagArray) {
            if (tagRepository.findByTagName(tagName) != null) {
                tags.add(tagRepository.findByTagName(tagName));
            } else {
                tags.add(create(tagName));
            }
        }
        return tags;
    }

}
