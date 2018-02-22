package com.example.Test.service;

import com.example.Test.dao.QuestionDAO;
import com.example.Test.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Controller
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    SensitiveService sensitiveService;

    public Question getById(int id) {
        return questionDAO.getById(id);
    }


    /**
     * 加入敏感词过滤
     * @param question
     * @return
     */
    public int addQuestion(Question question){
        question.setContent(HtmlUtils.htmlEscape(question.getContent())); //内容去除HTML标签
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));//标题去除HTML标签
        //敏感词过滤
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        return questionDAO.addQuestion(question)>0 ? question.getId() : 0;
    }

    public int updateCommentCount(int id, int count) {
        return questionDAO.updateCommentCount(id, count);
    }

    public List<Question> getLatestQuestions(int userId, int offset, int limit ){
        return questionDAO.selectLatestQuestions(userId, offset,limit);
    }
}
