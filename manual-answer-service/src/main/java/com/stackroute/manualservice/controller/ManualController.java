package com.stackroute.manualservice.controller;

import com.stackroute.manualservice.domain.Query;
import com.stackroute.manualservice.domain.UserQuery;
import com.stackroute.manualservice.exception.QueryNotFoundException;
import com.stackroute.manualservice.listener.ProducerService;
import com.stackroute.manualservice.service.ManualService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class ManualController {

    //Declaration

    private ManualService manualService;
    private ProducerService producerService;

    private final Logger logger = LoggerFactory.getLogger(ManualController.class);

    @Autowired
    public ManualController(ManualService manualService, ProducerService producerService) {
        this.manualService = manualService;
        this.producerService = producerService;

    }

    // Get  Request for getting all the questions

    @GetMapping("/questions")
    public ResponseEntity<List<UserQuery>> getAllQuestions() {

        List<UserQuery> questionList = manualService.getListOfQuestions();

        return new ResponseEntity<List<UserQuery>>(questionList, HttpStatus.OK);

    }

    //Get Question by Topic Name

    @GetMapping("/question/{topic_name}")
    public ResponseEntity<UserQuery>getByTopicName(@PathVariable("topic_name") String topic_name) throws QueryNotFoundException {
        ResponseEntity responseEntity;

        UserQuery queryList = manualService.getQuestionsByTopicName(topic_name);
        responseEntity = new ResponseEntity<UserQuery>(queryList, HttpStatus.ACCEPTED);
        return responseEntity;

    }
    //Delete Request

    @PostMapping("/question/{concept}")
    public ResponseEntity<String> updateQuestion(@RequestBody Query query,@PathVariable("concept") String concept) throws QueryNotFoundException {

        UserQuery updateQuestion = manualService.updateQuestion(query,concept);

        logger.info("Updated Questions:" + updateQuestion);

        // send data back to the bot service
        producerService.sendTemplate(updateQuestion);

        //Delete that question from Consumer side

        manualService.deleteQuestion(query,concept);

        return new ResponseEntity<String >("Query Deleted Successfully", HttpStatus.CREATED);
    }

}
