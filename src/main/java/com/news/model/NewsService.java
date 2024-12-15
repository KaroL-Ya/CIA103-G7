package com.news.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("newsService")
public class NewsService {

    @Autowired
    NewsRepository repository;

    // 新增消息
    public void addNews(NewsVO newsVO) {
        repository.save(newsVO);
    }

    // 更新消息
    public void updateNews(NewsVO newsVO) {
        repository.save(newsVO);
    }

    // 刪除消息
    public void deleteNews(Integer newsId) {
        if (repository.existsById(newsId)) {
            repository.deleteById(newsId);
        }
    }

    // 取得單一消息
    public NewsVO getOneNews(Integer newsId) {
        Optional<NewsVO> optional = repository.findById(newsId);
        return optional.orElse(null);
    }

    // 取得所有消息
    public List<NewsVO> getAll() {
        return repository.findAll();
    }
}